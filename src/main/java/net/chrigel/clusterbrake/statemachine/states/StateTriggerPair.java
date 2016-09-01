package net.chrigel.clusterbrake.statemachine.states;

import javafx.util.Callback;
import net.chrigel.clusterbrake.statemachine.State;
import net.chrigel.clusterbrake.statemachine.Trigger;

/**
 *
 */
class StateTriggerPair {

    private final State state;
    private final Callback<? extends Trigger, Void> callback;

    StateTriggerPair(State state, Callback<? extends Trigger, Void> callback) {
        this.state = state;
        this.callback = callback;
    }

    State getState() {
        return state;
    }

    Callback<Trigger, Void> getCallback() {
        return (Callback<Trigger, Void>) callback;
    }
}
