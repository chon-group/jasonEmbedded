package jason.architecture.mindinspectorapi.wrapper.extractor;

import jason.architecture.mindinspectorapi.wrapper.model.SourceWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extraí as informações de fonte de uma estrutura.
 */
public class SourceWrapperExtractor {

    /** Padrão da fonte da estrutura do agente. */
    private static final Pattern SOURCE_PATTERN = Pattern.compile("source\\(([a-zA-Z]+)\\)");

    /** Autorreferência do agente. */
    private static final String AGENT_SELF_NAME = "self";

    /**
     * Retorna todas as fontes da estrutura.
     *
     * @param structure Estrutura.
     * @param agentName Nome do agente.
     * @return Todas as fontes da estrutura.
     */
    public List<SourceWrapper> extractAll(String structure, String agentName) {
        List<SourceWrapper> sources = new ArrayList<>();
        while (true) {
            SourceWrapper source = this.extract(structure, agentName);
            if (source == null) {
                break;
            }
            structure = structure.replace(source.getText(), "");
            sources.add(source);
        }
        return sources;
    }

    /**
     * Retorna a fonte da estrutura.
     *
     * @param structure Estrutura.
     * @return Fonte.
     */
    public SourceWrapper extract(String structure, String agentName) {
        Matcher matcher = SOURCE_PATTERN.matcher(structure);
        if (matcher.find()) {
            String from = matcher.group(1);
            return new SourceWrapper(String.format("[%s]", matcher.group(0)),
                    from.equals(AGENT_SELF_NAME) ? agentName : from);
        }
        return null;
    }

    /**
     * Remove todos os sources de uma estrutura.
     *
     * @param structure Estrutura.
     * @return Estrutura sem os sources.
     */
    public String removeAllSourcesFromStructure(String structure) {
        while (true) {
            Matcher matcher = SOURCE_PATTERN.matcher(structure);
            if (matcher.find()) {
                structure = structure.replace(matcher.group(0), "");
            } else {
                break;
            }
        }
        structure = structure.replaceAll("\\[[, ]+\\]|\\[\\]", "");
        return structure;
    }

}
