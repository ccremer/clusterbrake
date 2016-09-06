package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Inject;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.ExceptionTrigger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class ErrorState
        extends AbstractState {

    private ExceptionTrigger trigger;
    private boolean exitApplicationOnError;
    private boolean loggingEnabled = true;

    @Inject
    public ErrorState(StateContext context) {
        super(context);
    }

    public void setExceptionTrigger(ExceptionTrigger trigger) {
        this.trigger = trigger;
    }

    public void setApplicationExitEnabled(boolean enabled) {
        this.exitApplicationOnError = enabled;
    }

    public void setLoggingEnabled(boolean enabled) {
        this.loggingEnabled = enabled;
    }

    @Override
    protected void enterState() {
        if (loggingEnabled && trigger != null) {
            logException(trigger, getLogger(trigger));
        }
        if (exitApplicationOnError) {
            System.exit(1);
        }
    }

    private Logger getLogger(ExceptionTrigger trigger) {
        if (trigger.getSource() == null) {
            return logger;
        } else {
            return LogManager.getLogger(trigger.getSource());
        }
    }

    private void logException(ExceptionTrigger trigger, Logger errorLogger) {
        if (trigger.getThrowable() == null) {
            errorLogger.error(trigger.getMessage());
        } else {
            errorLogger.error(trigger.getMessage(), trigger.getThrowable());
        }
    }

    @Override
    protected void exitState() {
    }

}
