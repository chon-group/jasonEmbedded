package jason.architecture.mindinspectorapi.wrapper.model.value;

import java.util.Objects;

public class VarWrapper extends ValueWrapper{

    private final String name;

    public VarWrapper(String name, Object value) {
        super(value);
        this.name = name;
    }

    /**
     * @return {@link #name}
     */
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        VarWrapper that = (VarWrapper) o;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.name);
    }
}
