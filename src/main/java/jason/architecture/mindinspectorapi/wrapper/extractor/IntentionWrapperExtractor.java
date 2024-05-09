package jason.architecture.mindinspectorapi.wrapper.extractor;

import jason.architecture.mindinspectorapi.wrapper.model.IntentionWrapper;
import jason.architecture.mindinspectorapi.wrapper.model.SourceWrapper;
import jason.asSemantics.Agent;
import jason.asSemantics.Circumstance;
import jason.asSemantics.IntendedMeans;
import jason.asSemantics.Intention;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Extraí as intenções de um agente.
 */
public class IntentionWrapperExtractor {

    /**
     * Retorna as intenções do agente.
     *
     * @param agent Agente.
     * @return Intenções do agente.
     */
    public List<IntentionWrapper> extract(Agent agent) {
        List<String> intentionsNames = new LinkedList<>();

        Circumstance circumstance = agent.getTS().getC();
        Queue<Intention> currentIntentions = circumstance.getIntentions();

        // Recupera as intenções atuais.
        for (Intention intention : currentIntentions) {
            String intentionName1 = this.getIntentionName(intention);
            if (intentionName1 == null) {
                continue;
            }
            if (!intentionsNames.contains(intentionName1)) {
                intentionsNames.add(intentionName1);
            }
        }

        // Recupera a intenção selecionada para ser executada no proximo ciclo.
        Intention selectedIntention = circumstance.getSelectedIntention();
        if (selectedIntention != null) {
            String selectedIntentionName = this.getIntentionName(selectedIntention);
            if (selectedIntentionName != null) {
                if (!intentionsNames.contains(selectedIntentionName)) {
                    intentionsNames.add(selectedIntentionName);
                }
            }
        }

        // Recupera as intenções que estão pendentes para serem executadas em algum momento em ordem.
        Map<String, Intention> pendingIntentions = circumstance.getPendingIntentions();
        if (pendingIntentions != null && !pendingIntentions.isEmpty()) {

            List<Integer> intentionsNumberList = new ArrayList<>();
            for (String key : pendingIntentions.keySet()) {
                String[] intentionNumberSplit = key.split("\\/");
                int intentionNumber = Integer.parseInt(intentionNumberSplit[0]);
                intentionsNumberList.add(intentionNumber);
            }

            intentionsNumberList = intentionsNumberList.stream().sorted().collect(Collectors.toList());
            for (Integer intentionNumber : intentionsNumberList) {
                for (String key : pendingIntentions.keySet()) {
                    if (key.startsWith(intentionNumber + "/")) {
                        String intentionName = this.getIntentionName(pendingIntentions.get(key));
                        if (intentionName == null) {
                            continue;
                        }
                        if (!intentionsNames.contains(intentionName)) {
                            intentionsNames.add(intentionName);
                        }
                        break;
                    }
                }
            }
        }

        SourceWrapperExtractor sourceWrapperExtractor = new SourceWrapperExtractor();

        return intentionsNames.stream().map(intentionStructure -> {
            List<SourceWrapper> sources = sourceWrapperExtractor.extractAll(intentionStructure,
                    agent.getTS().getUserAgArch().getAgName());
            intentionStructure = sourceWrapperExtractor.removeAllSourcesFromStructure(intentionStructure);
            return new IntentionWrapper(intentionStructure, sources);
        }).collect(Collectors.toList());
    }

    /**
     * Retorna o nome da intenção.
     *
     * @param intention Intenção.
     * @return Nome da intenção.
     */
    private String getIntentionName(Intention intention) {
        IntendedMeans peek = intention.peek();
        if (peek == null) {
            return null;
        }
        String intentionNameAndImplementation = peek.toString();
        String[] intentionNameAndImplementationSplit = intentionNameAndImplementation.split("<-");
        String intentionName = intentionNameAndImplementationSplit[0];
        intentionName = intentionName.replaceAll("!", "");
        intentionName = intentionName.replaceAll("\\+", "");
        intentionName = intentionName.replaceAll("-", "");
        if (intentionName.contains(" ")) {
            intentionName = intentionName.replaceAll(" ", "");
        }
        return intentionName;
    }

}
