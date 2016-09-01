package net.chrigel.clusterbrake.statemachine;

/**
 * Represents a state context.
 */
public interface StateContext {

    void setState(State state);

    State getState();

}
