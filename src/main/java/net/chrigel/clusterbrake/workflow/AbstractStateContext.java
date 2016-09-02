package net.chrigel.clusterbrake.workflow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.chrigel.clusterbrake.statemachine.State;
import net.chrigel.clusterbrake.statemachine.StateContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public abstract class AbstractStateContext
        implements StateContext {

    private State initialState;
    private State currentState;
    private final ExecutorService executor;
    private final Logger logger;

    protected AbstractStateContext() {
        this.logger = LogManager.getLogger(getClass());
        this.executor = Executors.newSingleThreadExecutor();
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
    public final void reset() {
        setState(initialState);
    }

    protected final void setStartupState(State state) {
        this.initialState = state;
    }

}
