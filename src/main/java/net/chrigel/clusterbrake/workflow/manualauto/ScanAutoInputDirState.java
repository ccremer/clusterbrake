package net.chrigel.clusterbrake.workflow.manualauto;

import net.chrigel.clusterbrake.statemachine.states.AbstractScanState;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.IOException;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.ExceptionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.GenericCollectionTrigger;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;

/**
 *
 */
public class ScanAutoInputDirState
        extends AbstractScanState {

    private final InputSettings inputSettings;

    @Inject
    ScanAutoInputDirState(
            StateContext context,
            InputSettings inputSettings,
            Provider<FileScanner<Video>> videoScannerProvider
    ) {
        super(context, videoScannerProvider);
        this.inputSettings = inputSettings;
    }

    @Override
    protected void enterState() {
        logger.debug("Looking for input directories in {}", inputSettings.getAutoInputDirectory());

        try {
            fireStateTrigger(new GenericCollectionTrigger(scanForVideoFiles(
                    inputSettings.getAutoInputDirectory(), inputSettings.getVideoExtensions(), true)));
        } catch (IOException ex) {
            logger.error(ex);
            fireStateTrigger(new ExceptionTrigger("Could not scan for files.", ex));
        }
    }

    @Override
    protected void exitState() {
    }

}
