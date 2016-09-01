package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.chrigel.clusterbrake.settings.SchedulerSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.ScheduledActionTrigger;

/**
 *
 */
class SchedulerState
        extends AbstractState {

    private ScheduledExecutorService executor;
    private SchedulerSettings settings;
    private Runnable task;

    @Inject
    SchedulerState(StateContext context) {
        super(context);
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    public final void setSettings(SchedulerSettings settings) {
        this.settings = settings;
    }
    
    @Override
    protected void enterState() {
        this.executor = Executors.newSingleThreadScheduledExecutor();
        if (settings.useRepeat()) {
            this.executor.scheduleAtFixedRate(() -> {
                runTask();
            }, 0, settings.getDelay(), settings.getTimeUnit());
        } else {
            this.executor.schedule(() -> {
                runTask();
            }, settings.getDelay(), settings.getTimeUnit());
        }
    }

    @Override
    protected void exitState() {
        if (isActive()) {
            try {
                this.executor.shutdownNow();
                this.executor.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                logger.warn("Waiting for scheduler shutdown timed out: {}", ex);
            }
        }
    }

    private void runTask() {
        if (task != null) {
            task.run();
        }
        fireStateTrigger(new ScheduledActionTrigger());
    }

}
