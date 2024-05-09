package jason.architecture.mindinspectorapi.wrapper.model;

import jason.architecture.mindinspectorapi.wrapper.model.value.VarWrapper;

import java.util.List;
import java.util.Objects;

public class BodyStructureWrapper {

    private final String text;

    private final List<VarWrapper> vars;

    public BodyStructureWrapper(String text, List<VarWrapper> vars) {
        this.text = text;
        this.vars = vars;
    }

    /**
     * @return {@link #text}
     */
    public String getText() {
        return this.text;
    }

    /**
     * @return {@link #vars}
     */
    public List<VarWrapper> getVars() {
        return this.vars;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.text, this.vars);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BodyStructureWrapper that = (BodyStructureWrapper) o;
        return Objects.equals(this.text, that.text) && Objects.equals(this.vars, that.vars);
    }
}
