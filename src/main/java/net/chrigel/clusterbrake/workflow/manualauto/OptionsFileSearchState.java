package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.List;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.settings.DirectorySettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;
import net.chrigel.clusterbrake.workflow.manualauto.settings.OptionDirVideoPair;

/**
 *
 */
public class OptionsFileSearchState
        extends AbstractState {

    private List<OptionDirVideoPair> list;
    private final DirectorySettings dirSettings;
    private final Provider<FileScanner<VideoOptionPackage>> optionScannerProvider;

    @Inject
    OptionsFileSearchState(
            StateContext context,
            DirectorySettings dirSettings,
            Provider<FileScanner<VideoOptionPackage>> optionScannerProvider) {
        super(context);
        this.dirSettings = dirSettings;
        this.optionScannerProvider = optionScannerProvider;
    }

    public void setVideoList(List<OptionDirVideoPair> list) {
        this.list = list;
    }

    @Override
    protected void enterState() {

    }

    @Override
    protected void exitState() {

    }

}
