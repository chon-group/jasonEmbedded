package jason.architecture.mindinspectorapi.wrapper.model;

import java.util.Objects;

public class SourceWrapper {

    private final String text;

    private final String from;

    public SourceWrapper(String text, String from) {
        this.text = text;
        this.from = from;
    }

    /**
     * @return {@link #from}
     */
    public String getFrom() {
        return this.from;
    }

    /**
     * @return {@link #text}
     */
    public String getText() {
        return this.text;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.text, this.from);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SourceWrapper that = (SourceWrapper) o;
        return Objects.equals(this.text, that.text) && Objects.equals(this.from, that.from);
    }
}
