package jason.architecture.mindinspectorapi;

import jason.architecture.AgArch;
import jason.architecture.mindinspectorapi.wrapper.extractor.AgentWrapperExtractor;
import jason.architecture.mindinspectorapi.wrapper.model.AgentWrapper;

/**
 * Arquitetura do Mind Inspector API.
 */
public class MindInspectorApiAgArch extends AgArch {

    /** Contador de ciclo. */
    private int cycleCounter;

    /**
     * Construtor.
     */
    public MindInspectorApiAgArch() {
        MindInspectorApi.init();
    }

    /**
     * Atualiza o histório do agente.
     */
    private void updateHistory() {
        AgentWrapper agent = new AgentWrapperExtractor().extract(super.getTS().getAg());
        agent.setCurrentCycleNumber(this.cycleCounter);
        AgentStateHistoryHolder.getInstance().add(agent);
    }

    @Override
    public void reasoningCycleStarting() {
        this.cycleCounter++;
        super.reasoningCycleStarting();
        //long t1 = System.currentTimeMillis();
        this.updateHistory();
        //long t2 = System.currentTimeMillis();
        //System.out.printf("[%s] Time to add agent state: %s%n", super.getTS().getUserAgArch().getAgName(), t2 - t1);
    }
}
