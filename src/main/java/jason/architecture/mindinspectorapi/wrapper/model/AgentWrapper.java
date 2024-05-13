package jason.architecture.mindinspectorapi.wrapper.model;

import jason.architecture.mindinspectorapi.wrapper.model.plan.PlanWrapper;

import java.util.List;
import java.util.Objects;

public class AgentWrapper {

    private final String name;

    private final List<BeliefWrapper> beliefs;

    private final List<IntentionWrapper> intentions;

    private final List<PlanWrapper> plans;

    private int newerCycleNumber;

    private int olderCycleNumber;

    private int currentCycleNumber;

    public AgentWrapper(String name, List<BeliefWrapper> beliefs, List<IntentionWrapper> intentions,
                        List<PlanWrapper> plans) {
        this.name = name;
        this.beliefs = beliefs;
        this.intentions = intentions;
        this.plans = plans;
    }

    public int getOlderCycleNumber() {
        return olderCycleNumber;
    }

    public void setOlderCycleNumber(int olderCycleNumber) {
        this.olderCycleNumber = olderCycleNumber;
    }

    /**
     * @return {@link #newerCycleNumber}
     */
    public int getNewerCycleNumber() {
        return this.newerCycleNumber;
    }

    /**
     * @param newerCycleNumber {@link #newerCycleNumber}
     */
    public void setNewerCycleNumber(int newerCycleNumber) {
        this.newerCycleNumber = newerCycleNumber;
    }

    /**
     * @return {@link #currentCycleNumber}
     */
    public int getCurrentCycleNumber() {
        return this.currentCycleNumber;
    }

    /**
     * @param currentCycleNumber {@link #currentCycleNumber}
     */
    public void setCurrentCycleNumber(int currentCycleNumber) {
        this.currentCycleNumber = currentCycleNumber;
    }

    /**
     * @return {@link #name}
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return {@link #beliefs}
     */
    public List<BeliefWrapper> getBeliefs() {
        return this.beliefs;
    }

    /**
     * @return {@link #intentions}
     */
    public List<IntentionWrapper> getIntentions() {
        return this.intentions;
    }

    /**
     * @return {@link #plans}
     */
    public List<PlanWrapper> getPlans() {
        return this.plans;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.beliefs, this.intentions, this.plans);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentWrapper that = (AgentWrapper) o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.beliefs, that.beliefs) && Objects.equals(
                this.intentions, that.intentions) && Objects.equals(this.plans, that.plans);
    }
}
