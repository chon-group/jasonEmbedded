package jason.architecture.mindinspectorapi;

import jason.architecture.AgArch;
import jason.architecture.mindinspectorapi.wrapper.extractor.AgentWrapperExtractor;
import jason.architecture.mindinspectorapi.wrapper.model.AgentWrapper;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Arquitetura do Mind Inspector API.
 */
public class MindInspectorApiAgArch extends AgArch {

    /** Tamanho máximo da fila. */
    private static final int QUEUE_MAX_SIZE = 1000;

    /** Histórico do agente. */
    private final LinkedList<AgentWrapper> history;

    /** Executor em Thread. */
    private final Executor executor;

    /**
     * Construtor.
     */
    public MindInspectorApiAgArch() {
        this.history = new LinkedList<>();
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Atualiza o histório do agente.
     */
    private void updateHistory() {
        AgentWrapper agent = new AgentWrapperExtractor().extract(super.getTS().getAg());

        if (!this.history.isEmpty() && this.history.getFirst().equals(agent)) {
            return;
        }

        if (this.history.size() == QUEUE_MAX_SIZE) {
            this.history.poll();
        }
        this.history.add(agent);

        agent.setCurrentCycleNumber(this.history.size());

        MindInspectorApi.get().setHistory(super.getTS().getUserAgArch().getAgName(), this.history);
    }

    @Override
    public void reasoningCycleStarting() {
        super.reasoningCycleStarting();
        this.executor.execute(this::updateHistory);
    }
}
