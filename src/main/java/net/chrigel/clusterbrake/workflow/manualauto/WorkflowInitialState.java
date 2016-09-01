package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import java.io.File;
import net.chrigel.clusterbrake.settings.DirectorySettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;

/**
 *
 */
public class WorkflowInitialState
        extends AbstractState {

    private final DirectorySettings inputSettings;

    @Inject
    public WorkflowInitialState(StateContext context, DirectorySettings inputSettings) {
        super(context);
        this.inputSettings = inputSettings;
    }

    @Override
    protected void enterState() {
        
        
    }

    @Override
    protected void exitState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
