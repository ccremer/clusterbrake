package net.chrigel.clusterbrake.statemachine.trigger;

/**
 *
 */
public class ScriptExecutedTrigger
        extends GenericPayloadTrigger<Integer> {

    public ScriptExecutedTrigger(Integer returnCode) {
        super(returnCode);
    }

}
