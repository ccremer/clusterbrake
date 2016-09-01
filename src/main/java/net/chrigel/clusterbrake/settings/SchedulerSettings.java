package net.chrigel.clusterbrake.settings;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public interface SchedulerSettings {

    /**
     * Gets the interval value.
     *
     * @return
     */
    long getDelay();

    /**
     * Gets the time unit for these settings.
     *
     * @return the time unit.
     */
    TimeUnit getTimeUnit();

    /**
     * If true, the scheduler is instructed to repeat its task with the configured delay.
     *
     * @return true if repeat enabled.
     */
    boolean useRepeat();
}
