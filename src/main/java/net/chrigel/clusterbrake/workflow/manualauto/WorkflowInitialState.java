package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import net.chrigel.clusterbrake.settings.DirectorySettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;

/**
 *
 */
public class WorkflowInitialState
        extends AbstractState {

    private final DirectorySettings dirSettings;
    private final InputSettings inputSettings;

    @Inject
    public WorkflowInitialState(
            StateContext context,
            DirectorySettings dirSettings,
            InputSettings inputSettings) {
        super(context);
        this.dirSettings = dirSettings;
        this.inputSettings = inputSettings;
    }

    @Override
    protected void enterState() {

    }

    @Override
    protected void exitState() {
    }

}