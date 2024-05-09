package jason.architecture.mindinspectorapi.wrapper.model.plan;

import jason.architecture.mindinspectorapi.wrapper.model.SourceWrapper;

import java.util.List;
import java.util.Objects;

public class PlanWrapper {

    private final String text;

    private final PlanParametersWrapper parameters;

    private final PlanContextWrapper context;

    private final List<PlanLineWrapper> bodyLines;

    private final SourceWrapper source;

    private final int srcBeginLine;

    private final int srcEndLine;

    private boolean triggered;

    public PlanWrapper(String text, SourceWrapper source, PlanParametersWrapper parameters, PlanContextWrapper context,
                       List<PlanLineWrapper> bodyLines, int srcBeginLine, int srcEndLine) {
        this.text = text;
        this.source = source;
        this.parameters = parameters;
        this.context = context;
        this.bodyLines = bodyLines;
        this.srcBeginLine = srcBeginLine;
        this.srcEndLine = srcEndLine;
    }

    /**
     * @return {@link #triggered}
     */
    public boolean isTriggered() {
        return this.triggered;
    }

    /**
     * @param triggered {@link #triggered}
     */
    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    /**
     * @return {@link #text}
     */
    public String getText() {
        return this.text;
    }

    /**
     * @return {@link #parameters}
     */
    public PlanParametersWrapper getParameters() {
        return this.parameters;
    }

    /**
     * @return {@link #context}
     */
    public PlanContextWrapper getContext() {
        return this.context;
    }

    /**
     * @return {@link #bodyLines}
     */
    public List<PlanLineWrapper> getBodyLines() {
        return this.bodyLines;
    }

    /**
     * @return {@link #source}
     */
    public SourceWrapper getSource() {
        return this.source;
    }

    /**
     * @return {@link #srcBeginLine}
     */
    public int getSrcBeginLine() {
        return this.srcBeginLine;
    }

    /**
     * @return {@link #srcEndLine}
     */
    public int getSrcEndLine() {
        return this.srcEndLine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.text, this.parameters, this.context, this.bodyLines, this.source, this.srcBeginLine,
                this.srcEndLine, this.triggered);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlanWrapper that = (PlanWrapper) o;
        return this.srcBeginLine == that.srcBeginLine && this.srcEndLine == that.srcEndLine
                && this.triggered == that.triggered && Objects.equals(this.text, that.text) && Objects.equals(
                this.parameters, that.parameters) && Objects.equals(this.context, that.context) && Objects.equals(
                this.bodyLines, that.bodyLines) && Objects.equals(this.source, that.source);
    }
}
