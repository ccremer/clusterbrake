package net.chrigel.clusterbrake.statemachine.trigger;

/**
 *
 */
public class ErrorTrigger
        extends MessageTrigger {

    private final Throwable ex;

    public ErrorTrigger() {
        this(null, null);
    }

    public ErrorTrigger(Throwable ex) {
        this(null, ex);
    }

    public ErrorTrigger(String message) {
        this(message, null);
    }

    public ErrorTrigger(String message, Throwable ex) {
        super(message);
        this.ex = ex;
    }

    public Throwable getThrowable() {
        return ex;
    }
}
