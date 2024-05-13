package jason.architecture.mindinspectorapi;

import jason.architecture.mindinspectorapi.wrapper.model.AgentWrapper;

import java.util.*;

/**
 * Singleton de gerenciamento do histórico do estados dos agentes por ciclo.
 */
public class AgentStateHistoryHolder {

    /** Tamanho máximo do histórico por agente. */
    private static final int QUEUE_MAX_SIZE = 1000;

    /** Instância. */
    private static AgentStateHistoryHolder instance;

    /** Estado do agente mapeado pelo número do ciclo mapeado pelo nome do agente. */
    private final Map<String, TreeMap<Integer, AgentWrapper>> stateHistoryByAgentName;

    /**
     * Construtor.
     */
    public AgentStateHistoryHolder() {
        this.stateHistoryByAgentName = new HashMap<>();
    }

    /**
     * Retorna a instância de container de histórico.
     *
     * @return Instância única.
     */
    public synchronized static AgentStateHistoryHolder getInstance() {
        if (instance == null) {
            instance = new AgentStateHistoryHolder();
        }
        return instance;
    }

    /**
     * Retorna o estado mais recentes de todos os agentes.
     *
     * @return Uma lista com o estado mais recente de todos os agentes.
     */
    public List<AgentWrapper> getAll() {
        List<AgentWrapper> returnAgentWrappers = new ArrayList<>();
        for (String agentName : this.stateHistoryByAgentName.keySet()) {
            AgentWrapper agentState = this.get(agentName, null);
            returnAgentWrappers.add(agentState);
        }
        return returnAgentWrappers;
    }

    /**
     * Retorna o estado do agente em determinado ciclo.
     *
     * @param agentName Nome do agente.
     * @param cycle Ciclo.
     * @return Estado do agente.
     */
    public AgentWrapper get(String agentName, Integer cycle) {
        TreeMap<Integer, AgentWrapper> history = this.stateHistoryByAgentName.get(agentName);
        if (history == null) {
            return null;
        }

        int newerCycleNumber = history.lastEntry().getValue().getCurrentCycleNumber();
        if (cycle == null) {
            cycle = newerCycleNumber;
        }

        AgentWrapper agentWrapper = history.get(cycle);
        if (agentWrapper == null) {
            return null;
        }

        int olderCycleNumber = history.firstEntry().getValue().getCurrentCycleNumber();
        agentWrapper.setNewerCycleNumber(newerCycleNumber);
        agentWrapper.setOlderCycleNumber(olderCycleNumber);

        return agentWrapper;
    }

    /**
     * Adiciona um estado de agente histórico.
     *
     * @param agent Estado do agente.
     */
    public void add(AgentWrapper agent) {
        TreeMap<Integer, AgentWrapper> history = this.stateHistoryByAgentName.computeIfAbsent(agent.getName(), k -> new TreeMap<>());

        if (history.size() == QUEUE_MAX_SIZE) {
            history.pollFirstEntry();
        }

        history.put(agent.getCurrentCycleNumber(), agent);
    }
}
