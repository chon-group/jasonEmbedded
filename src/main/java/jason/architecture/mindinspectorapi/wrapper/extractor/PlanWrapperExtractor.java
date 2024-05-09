package jason.architecture.mindinspectorapi.wrapper.extractor;

import jason.architecture.mindinspectorapi.wrapper.model.SourceWrapper;
import jason.architecture.mindinspectorapi.wrapper.model.plan.PlanContextWrapper;
import jason.architecture.mindinspectorapi.wrapper.model.plan.PlanLineWrapper;
import jason.architecture.mindinspectorapi.wrapper.model.plan.PlanParametersWrapper;
import jason.architecture.mindinspectorapi.wrapper.model.plan.PlanWrapper;
import jason.architecture.mindinspectorapi.wrapper.model.value.ValueWrapper;
import jason.asSemantics.Agent;
import jason.asSemantics.IntendedMeans;
import jason.asSemantics.Intention;
import jason.asSemantics.Unifier;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.Plan;
import jason.asSyntax.PlanBody;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Extraí os planos de um agente.
 */
public class PlanWrapperExtractor {

    /** Prefixo dos planos KQML. */
    private static final String KQML_PREFIX = "@kqml";

    /** Prefixo do plano. */
    private static final String PLAN_SUFIX = "l__\\d+";

    /** Caractere de separação de ações do plano. */
    private static final String PLAN_ACTIONS_CHARACTER_SEPARATOR = ";";

    /** Caractere de finalização de ações do plano. */
    private static final String PLAN_ACTIONS_CHARACTER_FINISH = ".";

    /**
     * Pega todos os componentes do plano e, caso possível, os valores das variáveis (Através do unificador).
     *
     * @param plan    Plano.
     * @param agent   Agente.
     * @param unifier Unificador.
     * @return Plano.
     */
    public PlanWrapper getPlan(Plan plan, Agent agent, Unifier unifier) {
        SourceWrapperExtractor sourceWrapperExtractor = new SourceWrapperExtractor();
        ValuesWrapperExtractor valuesWrapperExtractor = new ValuesWrapperExtractor();

        String planStructure = plan.getTrigger().toString() + plan.getLabel().toString().replaceAll(PLAN_SUFIX, "");

        SourceWrapper source = sourceWrapperExtractor.extract(planStructure, agent.getTS().getUserAgArch().getAgName());
        planStructure = sourceWrapperExtractor.removeAllSourcesFromStructure(planStructure);

        List<ValueWrapper> values = new ArrayList<>();
        if (unifier != null) {
            List<Term> terms = plan.getTrigger().getLiteral().getTerms();
            for (Term term : terms) {
                valuesWrapperExtractor.extract(values, term, unifier);
            }
        }

        String parametersStructure = null;
        int parametersBeginIndex = planStructure.indexOf("(");
        if (parametersBeginIndex != -1) {
            parametersStructure = planStructure.substring(parametersBeginIndex);
            planStructure = planStructure.replace(parametersStructure, "");
        }

        PlanParametersWrapper parameters = new PlanParametersWrapper(parametersStructure, values);

        PlanContextWrapper context = null;
        if (plan.getContext() != null) {
            String contextText = Optional.ofNullable(plan.getContext()).map(LogicalFormula::toString).orElse("");
            planStructure = planStructure.replace(contextText, "");

            List<ValueWrapper> contextValues = new ArrayList<>();
            if (unifier != null) {
                valuesWrapperExtractor.extract(contextValues, plan.getContext(), unifier);
            }
            context = new PlanContextWrapper(contextText, contextValues);
        }

        List<PlanLineWrapper> body = new ArrayList<>();
        PlanBody pb = plan.getBody();
        while (pb != null) {
            String line = null;
            if (pb.getBodyTerm() != null) {
                line = pb.getBodyType().toString() + pb.getBodyTerm().toString();
            }

            List<ValueWrapper> lineValues = new ArrayList<>();
            if (unifier != null) {
                valuesWrapperExtractor.extract(lineValues, pb.getBodyTerm(), unifier);
            }

            pb = pb.getBodyNext();
            if (line != null) {
                if (pb != null) {
                    line = line + PLAN_ACTIONS_CHARACTER_SEPARATOR;
                } else {
                    line = line + PLAN_ACTIONS_CHARACTER_FINISH;
                }
            }

            body.add(new PlanLineWrapper(line, lineValues));
        }

        return new PlanWrapper(planStructure, source, parameters, context, body, plan.getSrcInfo().getBeginSrcLine(),
                plan.getSrcInfo().getEndSrcLine());
    }

    /**
     * Retorna os planos do agente.
     *
     * @param agent Agente.
     * @return Planos do agente.
     */
    public List<PlanWrapper> extract(Agent agent) {
        List<PlanWrapper> planWrappers = new ArrayList<>();

        List<IntendedMeans> intendedMeans = new ArrayList<>();

        Intention selectedIntention = agent.getTS().getC().getSelectedIntention();
        if (selectedIntention != null) {
            for (IntendedMeans means : selectedIntention) {
                intendedMeans.add(means);
            }
        }

        HashSet<Plan> alreadyProcessedPlans = new HashSet<>();
        for (IntendedMeans i : intendedMeans) {
            Plan plan = i.getPlan();
            if (plan.toASString().startsWith(KQML_PREFIX)) {
                continue;
            }

            alreadyProcessedPlans.add(plan);

            PlanWrapper planWrapper = this.getPlan(plan, agent, i.getUnif());
            planWrapper.setTriggered(true);

            planWrappers.add(planWrapper);
        }

        List<Plan> allPlans = agent.getPL().getPlans();
        for (Plan plan : allPlans) {
            if (plan.toASString().startsWith(KQML_PREFIX) || alreadyProcessedPlans.contains(plan)) {
                continue;
            }

            PlanWrapper planWrapper = this.getPlan(plan, agent, null);
            planWrappers.add(planWrapper);
        }

        planWrappers = planWrappers.stream().sorted((p1, p2) -> {
            if (p1.isTriggered() && p2.isTriggered() || !p1.isTriggered() && !p2.isTriggered()) {
                return 0;
            } else if (p1.isTriggered() && !p2.isTriggered()) {
                return -1;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());

        return planWrappers;
    }

}
