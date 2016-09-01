package net.chrigel.clusterbrake.workflow.manualauto;

import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;

/**
 *
 */
public class WorkflowInitialState
        extends AbstractState {

    public WorkflowInitialState(StateContext context) {
        super(context);
    }

    @Override
    protected void enterState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void exitState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
