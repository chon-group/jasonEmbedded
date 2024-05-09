package jason.architecture.mindinspectorapi.wrapper.model.value;

import java.util.Objects;

public class ValueWrapper {

    private final Object value;

    public ValueWrapper(Object value) {
        this.value = value;
    }

    /**
     * @return {@link #value}
     */
    public Object getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValueWrapper that = (ValueWrapper) o;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
}
