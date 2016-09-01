package net.chrigel.clusterbrake.statemachine;

/**
 * Represents a state.
 */
public interface State {

    /**
     * Binds another state to a trigger. If the trigger activates, then the passed state will be transitioned to.
     *
     * @param state the state.
     * @param trigger the trigger class.
     * @throws IllegalStateException if trigger is already bound.
     */
    void bindStateToTrigger(State state, Class<? extends Trigger> trigger);

    /**
     * Enters the state. This method needs to return as soon as the state has entered. The state must remain active. The
     * state must not, under any circumstances, activate by itself.
     */
    void enter();

    /**
     * Exits the state. The Implementation must stop all activities related to the state. The state must not, under any
     * circumstances, deactivate by itself.
     */
    void exit();

    /**
     * Indicates whether the state is active.
     *
     * @return true if active.
     */
    boolean isActive();
}
