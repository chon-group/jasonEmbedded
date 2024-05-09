package jason.architecture.mindinspectorapi.wrapper.extractor;

import jason.architecture.mindinspectorapi.wrapper.model.AgentWrapper;
import jason.architecture.mindinspectorapi.wrapper.model.BeliefWrapper;
import jason.architecture.mindinspectorapi.wrapper.model.IntentionWrapper;
import jason.architecture.mindinspectorapi.wrapper.model.plan.PlanWrapper;
import jason.asSemantics.Agent;

import java.util.List;

/**
 * Transforma um {@link Agent} em um {@link AgentWrapper}.
 */
public class AgentWrapperExtractor {

    /**
     * Transforma o agente num objeto de trasnfência de dados.
     *
     * @param agent Agente.
     * @return Objeto de transferência de dados de agente.
     */
    public AgentWrapper extract(Agent agent) {
        BeliefWrapperExtractor beliefWrapperExtractor = new BeliefWrapperExtractor();
        IntentionWrapperExtractor intentionWrapperExtractor = new IntentionWrapperExtractor();
        PlanWrapperExtractor planWrapperExtractor = new PlanWrapperExtractor();

        List<BeliefWrapper> beliefs = beliefWrapperExtractor.extract(agent);
        List<IntentionWrapper> intentions = intentionWrapperExtractor.extract(agent);
        List<PlanWrapper> plans = planWrapperExtractor.extract(agent);

        return new AgentWrapper(agent.getTS().getUserAgArch().getAgName(), beliefs, intentions, plans);
    }

}
