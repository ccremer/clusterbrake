package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;

/**
 *
 */
public class CleanupState
        extends AbstractState {

    @Inject
    CleanupState(StateContext context) {
        super(context);
    }

    @Override
    protected void enterState() {

        // move config file to dir.stage.finished
// move video from dir.stage.temp to dir.stage.finished  or dir.output.manual
    }

    @Override
    protected void exitState() {
    }

}
