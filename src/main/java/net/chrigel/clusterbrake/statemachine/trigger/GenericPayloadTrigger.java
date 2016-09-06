package net.chrigel.clusterbrake.statemachine.trigger;

import net.chrigel.clusterbrake.statemachine.Trigger;

/**
 *
 * @param <T>
 */
public class GenericPayloadTrigger<T>
        implements Trigger {

    private final T payload;

    public GenericPayloadTrigger(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }
    
    public <T> T getPayload(Class<T> clazz) {
        return (T) payload;
    }
    
}
