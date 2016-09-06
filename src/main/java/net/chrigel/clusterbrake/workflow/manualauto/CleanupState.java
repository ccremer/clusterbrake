package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import java.io.File;
import net.chrigel.clusterbrake.settings.DirectorySettings;
import net.chrigel.clusterbrake.settings.JobSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;

/**
 *
 */
public class CleanupState
        extends AbstractState {

    private final JobSettings jobSettings;

    @Inject
    CleanupState(
            StateContext context,
            JobSettings jobSettings,
            DirectorySettings dirSettings
    ) {
        super(context);
        this.jobSettings = jobSettings;
        this.jobSettings.setSettingsFile(new File(dirSettings.getConfigBaseDir(), "finished.json"));
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
