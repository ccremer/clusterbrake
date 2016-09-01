package net.chrigel.clusterbrake.statemachine;

/**
 * Represents a state. The state must not (de)activate by itself, it must be activated using enter/exit methods from
 * outside. If a state should deactivate, consider an EventListener Model where the outsiders can register and listen
 * for events.
 */
public interface State {

    /**
     * Enters the state. This method needs to return as soon as the state has entered. The state must remain active,
     * even when the method returns.
     */
    void enter();

    /**
     * Exits the state. The Implementation must stop all activities related to the state.
     */
    void exit();

    /**
     * Indicates whether the state is active.
     *
     * @return true if active.
     */
    boolean isActive();
}
