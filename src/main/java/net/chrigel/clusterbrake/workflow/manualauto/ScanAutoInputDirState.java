package net.chrigel.clusterbrake.workflow.manualauto;

import net.chrigel.clusterbrake.statemachine.states.AbstractVideoScanState;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.IOException;
import java.util.List;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.ExceptionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.GenericCollectionTrigger;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;

/**
 *
 */
public class ScanAutoInputDirState
        extends AbstractVideoScanState {

    private final InputSettings inputSettings;
    private List<VideoPackage> videos;

    @Inject
    ScanAutoInputDirState(
            StateContext context,
            InputSettings inputSettings,
            Provider<FileScanner<VideoPackage>> videoScannerProvider
    ) {
        super(context, videoScannerProvider);
        this.inputSettings = inputSettings;
    }

    public void setVideoList(List<VideoPackage> list) {
        this.videos = list;
    }

    @Override
    protected void enterState() {
        logger.debug("Looking for input directories in {}", DirTypes.INPUT_AUTO);

        try {
            videos.addAll(scanForVideoFiles(
                    DirTypes.INPUT_AUTO.getBase(),
                    DirTypes.INPUT_AUTO, inputSettings.getVideoExtensions(), true));
            fireStateTrigger(new GenericCollectionTrigger(videos));
        } catch (IOException ex) {
            logger.error(ex);
            fireStateTrigger(new ExceptionTrigger("Could not scan for files.", ex));
        }
    }

    @Override
    protected void exitState() {
    }

}
