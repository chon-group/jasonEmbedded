package jason.architecture.mindinspectorapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpServer;
import jason.architecture.mindinspectorapi.wrapper.model.AgentWrapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * Singleton que gerencia a API do Mindinspetor.
 */
public class MindInspectorApi {

    /** Instância Gson. */
    private static final Gson GSON_INSTANCE = new GsonBuilder().serializeNulls().create();

    /** Caminho da API. */
    private static final String API_PATH = "/mindinspector/agents";

    /** Porta da API. */
    private static final int SERVER_PORT = 3275;

    /** Instância. */
    public static MindInspectorApi instance;

    /** Histórico do agente mapeado pelo nome do agente. */
    private final Map<String, TreeMap<Integer, AgentWrapper>> historyByAgent;

    /** Servidor Web. */
    private HttpServer httpServer;

    /**
     * Construtor.
     */
    public MindInspectorApi() {
        this.historyByAgent = new HashMap<>();
    }

    /**
     * Retorna uma instância única da API.
     *
     * @return Instância única da API.
     */
    public static synchronized MindInspectorApi get() {
        if (instance == null) {
            instance = new MindInspectorApi();
            instance.start();
            instance.mountAgentsContext();
        }
        return instance;
    }

    /**
     * Seta o histórico de estados do agente.
     *
     * @param agentName     Nome do agente.
     * @param agentWrappers Histórico de estados do agente.
     */
    public void setHistory(String agentName, TreeMap<Integer, AgentWrapper> agentWrappers) {
        this.historyByAgent.put(agentName, agentWrappers);
    }

    /**
     * Monta o contexto que recebe as requisições do cliente.
     */
    public synchronized void mountAgentsContext() {
        this.httpServer.createContext(API_PATH, exchange -> {
            String requestMethod = exchange.getRequestMethod();

            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin," + "Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, " + "Access-Control-Request-Headers, Authorization, Access-Control-Allow-Origin");

            if (requestMethod.equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if (requestMethod.equalsIgnoreCase("GET")) {
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                OutputStream responseBody = exchange.getResponseBody();

                Map<String, String> parameters = this.getParameters(exchange.getRequestURI().getQuery());

                String json;
                if (parameters != null) {
                    String name = parameters.get("agent");

                    if (name == null) {
                        exchange.sendResponseHeaders(400, 0);
                        return;
                    }

                    TreeMap<Integer, AgentWrapper> agentByCycle = this.historyByAgent.get(name);
                    if (agentByCycle == null) {
                        exchange.sendResponseHeaders(404, 0);
                        return;
                    }

                    exchange.sendResponseHeaders(200, 0);

                    int cycle = 0;
                    int lastAgentCycle = agentByCycle.lastEntry().getValue().getCurrentCycleNumber();
                    if (parameters.containsKey("cycle")) {
                        try {
                            cycle = Integer.parseInt(parameters.get("cycle"));
                            if (cycle > lastAgentCycle) {
                                cycle = lastAgentCycle;
                            }
                        } catch (NumberFormatException ignored) {
                            cycle = lastAgentCycle;
                        }
                    }

                    AgentWrapper agentWrapper = agentByCycle.get(cycle);
                    agentWrapper.setTotalCycleNumber(lastAgentCycle);

                    json = GSON_INSTANCE.toJson(agentWrapper);
                } else {
                    exchange.sendResponseHeaders(200, 0);

                    List<AgentWrapper> returnAgentWrappers = new ArrayList<>();

                    for (String agentName : this.historyByAgent.keySet()) {
                        TreeMap<Integer, AgentWrapper> integerAgentWrapperTreeMap = this.historyByAgent.get(agentName);
                        AgentWrapper agent = integerAgentWrapperTreeMap.lastEntry().getValue();
                        agent.setTotalCycleNumber(agent.getCurrentCycleNumber());
                        returnAgentWrappers.add(agent);
                    }

                    json = GSON_INSTANCE.toJson(returnAgentWrappers, new TypeToken<ArrayList<AgentWrapper>>() {
                    }.getType());
                }

                responseBody.write(json.getBytes());
                responseBody.close();
            }
        });
    }

    /**
     * Retorna os parâmetros da requisição.
     *
     * @param query Pedido da requisição.
     * @return Parâmetros da requisição.
     */
    public Map<String, String> getParameters(String query) {
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    /**
     * Inicia o servidor Web.
     */
    private synchronized void start() {
        try {
            try {
                this.httpServer = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.httpServer.setExecutor(Executors.newCachedThreadPool());
            this.httpServer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
