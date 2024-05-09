package jason.architecture.mindinspectorapi.wrapper.model;

import java.util.List;
import java.util.Objects;

public class IntentionWrapper {

    private final String text;

    private final List<SourceWrapper> sources;

    public IntentionWrapper(String text, List<SourceWrapper> source) {
        this.text = text;
        this.sources = source;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IntentionWrapper that = (IntentionWrapper) o;
        return Objects.equals(this.text, that.text) && Objects.equals(this.sources, that.sources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.text, this.sources);
    }
}
