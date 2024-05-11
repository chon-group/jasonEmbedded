package jason.architecture.mindinspectorapi;

import jason.architecture.AgArch;
import jason.architecture.mindinspectorapi.wrapper.extractor.AgentWrapperExtractor;
import jason.architecture.mindinspectorapi.wrapper.model.AgentWrapper;
import jason.asSemantics.Agent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * Arquitetura do Mind Inspector API.
 */
public class MindInspectorApiAgArch extends AgArch {

    private static final Logger LOGGER = Logger.getLogger(MindInspectorApiAgArch.class.getName());

    /** Tamanho máximo da fila. */
    private static final int QUEUE_MAX_SIZE = 1000;

    /** Histórico do agente. */
    private final TreeMap<Integer, AgentWrapper> history;

    /** Contador de ciclo. */
    private int cycleCounter;

    /**
     * Construtor.
     */
    public MindInspectorApiAgArch() {
        this.history = new TreeMap<>();
    }

    /**
     * Atualiza o histório do agente.
     */
    private void updateHistory(Agent ag) {
        AgentWrapper agent = new AgentWrapperExtractor().extract(ag);

        if (this.history.size() == QUEUE_MAX_SIZE) {
            this.history.pollFirstEntry();
        }

        agent.setCurrentCycleNumber(this.cycleCounter);
        this.history.put(this.cycleCounter, agent);

        MindInspectorApi.get().setHistory(ag.getTS().getUserAgArch().getAgName(), this.history);
    }

    @Override
    public void reasoningCycleStarting() {
        super.reasoningCycleStarting();
        //long t1 = System.currentTimeMillis();
        this.updateHistory(super.getTS().getAg());
        //long t2 = System.currentTimeMillis();
        //System.out.printf("[%s] Time to add agent state: %s%n", super.getTS().getUserAgArch().getAgName(), t2 - t1);
        this.cycleCounter++;
    }
}
