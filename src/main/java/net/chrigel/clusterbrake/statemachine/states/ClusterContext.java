package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.chrigel.clusterbrake.settings.SchedulerSettings;
import net.chrigel.clusterbrake.statemachine.State;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.InitializedStateTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.TranscodingFinishedTrigger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
class ClusterContext
        implements StateContext {

    private State currentState;
    private final ExecutorService executor;
    private final Logger logger;

    @Inject
    ClusterContext(
            InitialState initialState,
            ManualInputState manualInputState,
            TranscodingState transcodingState,
            CleanupState cleanupState,
            SchedulerState schedulerState,
            SchedulerSettings schedulerSettings
    ) {
        this.logger = LogManager.getLogger(getClass());
        this.executor = Executors.newSingleThreadExecutor();

        initialState.bindStateToTrigger(manualInputState, InitializedStateTrigger.class);
        schedulerState.setSettings(schedulerSettings);
        transcodingState.bindStateToTrigger(cleanupState, TranscodingFinishedTrigger.class);

        setState(initialState);
    }

    @Override
    public final void setState(State state) {
        // an executor is needed to avoid a stackoverflow.
        executor.submit(() -> {
            if (currentState != null) {
                logger.debug("Shutting down current state {}...", this.currentState);
                currentState.exit();
            }

            logger.info("Setting state to {}...", state);
            currentState = state;
            currentState.enter();
        });
    }

    @Override
    public final State getState() {
        return currentState;
    }

}
