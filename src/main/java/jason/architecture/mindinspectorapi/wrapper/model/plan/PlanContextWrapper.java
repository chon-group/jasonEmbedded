package jason.architecture.mindinspectorapi.wrapper.model.plan;

import jason.architecture.mindinspectorapi.wrapper.model.value.ValueWrapper;

import java.util.List;
import java.util.Objects;

public class PlanContextWrapper {

    private final String text;

    private final List<ValueWrapper> values;

    public PlanContextWrapper(String text, List<ValueWrapper> values) {
        this.text = text;
        this.values = values;
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

    @Override
    public int hashCode() {
        return Objects.hash(this.text, this.values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlanContextWrapper that = (PlanContextWrapper) o;
        return Objects.equals(this.text, that.text) && Objects.equals(this.values, that.values);
    }
}
