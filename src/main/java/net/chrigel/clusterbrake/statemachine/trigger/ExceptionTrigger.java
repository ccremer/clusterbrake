package net.chrigel.clusterbrake.statemachine.trigger;

/**
 *
 */
public class ExceptionTrigger
        extends MessageTrigger {

    private final Throwable ex;
    private Class source;

    public ExceptionTrigger() {
        this(null, null);
    }

    public ExceptionTrigger(Throwable ex) {
        this(null, ex);
    }

    public ExceptionTrigger(String message) {
        this(message, null);
    }

    public ExceptionTrigger(String message, Throwable ex) {
        this(message, ex, null);
    }

    public ExceptionTrigger(String message, Throwable ex, Class source) {
        super(message);
        this.ex = ex;
        this.source = source;
    }

    public Throwable getThrowable() {
        return ex;
    }

    public Class getSource() {
        return source;
    }
}
