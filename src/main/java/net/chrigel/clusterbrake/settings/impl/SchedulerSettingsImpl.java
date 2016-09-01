package net.chrigel.clusterbrake.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.concurrent.TimeUnit;
import net.chrigel.clusterbrake.settings.SchedulerSettings;

/**
 *
 */
public class SchedulerSettingsImpl
        implements SchedulerSettings {

    private final long delay;

    @Inject
    SchedulerSettingsImpl(
            @Named("workflow.scheduler.interval") long delay
    ) {
        this.delay = delay;
    }

    @Override
    public long getDelay() {
        return delay;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return TimeUnit.MINUTES;
    }

    @Override
    public boolean useRepeat() {
        return false;
    }

}
