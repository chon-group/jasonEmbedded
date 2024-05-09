package jason.architecture.mindinspectorapi.wrapper.model;

import jason.architecture.mindinspectorapi.wrapper.model.value.ValueWrapper;

import java.util.List;
import java.util.Objects;

public class BeliefWrapper {

    private final String text;

    private final List<ValueWrapper> values;

    private final List<SourceWrapper> sources;

    private final int srcBeginLine;

    private final int srcEndLine;

    public BeliefWrapper(String text, List<ValueWrapper> values, List<SourceWrapper> sources, int srcBeginLine,
                         int srcEndLine) {
        this.text = text;
        this.values = values;
        this.sources = sources;
        this.srcBeginLine = srcBeginLine;
        this.srcEndLine = srcEndLine;
    }

    /**
     * @return {@link #values}
     */
    public List<ValueWrapper> getValues() {
        return this.values;
    }

    /**
     * @return {@link #text}
     */
    public String getText() {
        return this.text;
    }

    /**
     * @return {@link #sources}
     */
    public List<SourceWrapper> getSources() {
        return this.sources;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BeliefWrapper that = (BeliefWrapper) o;
        return this.srcBeginLine == that.srcBeginLine && this.srcEndLine == that.srcEndLine && Objects.equals(this.text,
                that.text) && Objects.equals(this.sources, that.sources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.text, this.sources, this.srcBeginLine, this.srcEndLine);
    }
}
