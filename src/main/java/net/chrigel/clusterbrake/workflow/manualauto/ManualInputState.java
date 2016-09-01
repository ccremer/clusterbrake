package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.IOException;
import net.chrigel.clusterbrake.media.VideoScanner;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;
import net.chrigel.clusterbrake.statemachine.trigger.ErrorTrigger;

/**
 *
 */
public class ManualInputState
        extends AbstractState {

    private final InputSettings inputSettings;
    private final Provider<VideoScanner> videoScannerProvider;

    @Inject
    ManualInputState(
            StateContext context,
            InputSettings inputSettings,
            Provider<VideoScanner> videoScannerProvider
    ) {
        super(context);
        this.inputSettings = inputSettings;
        this.videoScannerProvider = videoScannerProvider;
    }

    @Override
    protected void enterState() {
        try {
            videoScannerProvider.get()
                    .searchIn(inputSettings.getManualInputDirectory())
                    .withFileExtensionFilter(inputSettings.getVideoExtensions())
                    .scan();
        } catch (IOException ex) {
            logger.error(ex);
            fireStateTrigger(new ErrorTrigger());
        }
    }

    @Override
    protected void exitState() {

    }

}
