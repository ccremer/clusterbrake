package net.chrigel.clusterbrake.workflow.manualauto;

import net.chrigel.clusterbrake.statemachine.states.AbstractScanState;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.ExceptionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.GenericCollectionTrigger;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.OptionDirVideoPair;

/**
 *
 */
public class ScanManualInputDirState
        extends AbstractScanState {

    private final InputSettings inputSettings;

    @Inject
    ScanManualInputDirState(
            StateContext context,
            InputSettings inputSettings,
            Provider<FileScanner<Video>> videoScannerProvider
    ) {
        super(context, videoScannerProvider);
        this.inputSettings = inputSettings;
    }

    @Override
    protected void enterState() {
        try {

            logger.debug("Looking for input directories in {}", inputSettings.getManualInputDirectory());
            List<File> optionDirs = getOptionDirs(inputSettings.getManualInputDirectory());

            List<OptionDirVideoPair> pairList = new LinkedList<>();
            /**
             * load all videos which are in subfolders. Each subfolder name is the name of the template.
             */
            optionDirs.forEach(optionDir -> {
                try {
                    pairList.add(new OptionDirVideoPair(
                            optionDir,
                            scanForVideoFiles(optionDir, inputSettings.getVideoExtensions(), true)));
                } catch (IOException ex) {
                    logger.warn(ex);
                }
            });
            /**
             * Add the video files which are in the dir and need the last-resort template.
             */
            pairList.add(new OptionDirVideoPair(
                    null,
                    scanForVideoFiles(
                            inputSettings.getManualInputDirectory(),
                            inputSettings.getVideoExtensions(),
                            false)));
            fireStateTrigger(new GenericCollectionTrigger(pairList));

        } catch (IOException ex) {
            logger.error(ex);
            fireStateTrigger(new ExceptionTrigger(ex));
        }
    }

    private List<File> getOptionDirs(File baseDir) {
        return Arrays.asList(
                baseDir.listFiles((File pathname) -> {
                    return pathname.isDirectory();
                }));
    }

    @Override
    protected void exitState() {
    }

}
