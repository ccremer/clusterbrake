package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.IOException;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;
import net.chrigel.clusterbrake.statemachine.trigger.ErrorTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.ListResultTrigger;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.Video;

/**
 *
 */
public class ScanManualInputDirState
        extends AbstractState {

    private final InputSettings inputSettings;
    private final Provider<FileScanner<Video>> videoScannerProvider;

    @Inject
    ScanManualInputDirState(
            StateContext context,
            InputSettings inputSettings,
            Provider<FileScanner<Video>> videoScannerProvider
    ) {
        super(context);
        this.inputSettings = inputSettings;
        this.videoScannerProvider = videoScannerProvider;
    }

    @Override
    protected void enterState() {
        try {
            fireStateTrigger(new ListResultTrigger(
                    videoScannerProvider.get()
                    .search(inputSettings.getManualInputDirectory())
                    .withFileExtensionFilter(inputSettings.getVideoExtensions())
                    .scan()));
        } catch (IOException ex) {
            logger.error(ex);
            fireStateTrigger(new ErrorTrigger());
        }
    }

    @Override
    protected void exitState() {

    }

}
