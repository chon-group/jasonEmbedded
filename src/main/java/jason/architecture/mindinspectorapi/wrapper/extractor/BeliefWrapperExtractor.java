package jason.architecture.mindinspectorapi.wrapper.extractor;

import jason.architecture.mindinspectorapi.wrapper.model.BeliefWrapper;
import jason.architecture.mindinspectorapi.wrapper.model.SourceWrapper;
import jason.architecture.mindinspectorapi.wrapper.model.value.ValueWrapper;
import jason.asSemantics.Agent;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;

import java.util.ArrayList;
import java.util.List;

/**
 * Extraí as crenças de um agente.
 */
public class BeliefWrapperExtractor {

    /**
     * Retorna as crenças do agente.
     *
     * @param agent Agente.
     * @return Crenças do agente.
     */
    public List<BeliefWrapper> extract(Agent agent) {
        List<BeliefWrapper> beliefs = new ArrayList<>();

        SourceWrapperExtractor sourceWrapperExtractor = new SourceWrapperExtractor();
        ValuesWrapperExtractor valuesWrapperExtractor = new ValuesWrapperExtractor();

        for (Literal belief : agent.getTS().getAg().getBB()) {
            String beliefStructure = belief.toString();

            List<SourceWrapper> sources = sourceWrapperExtractor.extractAll(beliefStructure,
                    agent.getTS().getUserAgArch().getAgName());
            beliefStructure = sourceWrapperExtractor.removeAllSourcesFromStructure(beliefStructure);

            List<ValueWrapper> values = new ArrayList<>();
            valuesWrapperExtractor.extract(values, belief, new Unifier());

            try {
                if (values.isEmpty()) {
                    values = null;
                } else {
                    Object value = values.get(0).getValue();
                    if (value instanceof ValueWrapper) {
                        values.clear();
                        values.add((ValueWrapper) value);
                    } else {
                        values = (List<ValueWrapper>) value;
                    }
                }
            } catch (Exception e) {
                values = null;
            }

            beliefs.add(new BeliefWrapper(beliefStructure, values, sources, belief.getSrcInfo().getBeginSrcLine(),
                    belief.getSrcInfo().getEndSrcLine()));
        }

        return beliefs;
    }

}
