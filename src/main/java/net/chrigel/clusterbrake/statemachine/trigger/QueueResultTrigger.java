package net.chrigel.clusterbrake.statemachine.trigger;

import net.chrigel.clusterbrake.settings.Job;

/**
 *
 */
public class QueueResultTrigger
        extends GenericPayloadTrigger<Job> {

    public QueueResultTrigger(Job payload) {
        super(payload);
    }

}
