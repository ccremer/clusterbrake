package net.chrigel.clusterbrake.workflow.manualauto;

import net.chrigel.clusterbrake.statemachine.states.AbstractOptionParseState;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.GenericCollectionTrigger;
import net.chrigel.clusterbrake.workflow.manualauto.settings.OptionDirVideoPair;

/**
 *
 */
class ManualParseOptionsFileState
        extends AbstractOptionParseState {

    private List<OptionDirVideoPair> list;

    @Inject
    ManualParseOptionsFileState(
            StateContext context,
            Provider<VideoOptionPackage> optionPackageProvider,
            Provider<OptionsFileParser> optionParserProvider,
            Provider<VideoPackage> videoPackageProvider
    ) {
        super(context, optionPackageProvider, optionParserProvider, videoPackageProvider);
    }

    public void setOptionDirList(List<OptionDirVideoPair> list) {
        this.list = list;
    }

    @Override
    protected void enterState() {

        List<VideoPackage> packageList = new LinkedList<>();
        /**
         * Find and apply the options file for each template dir.
         */
        list.forEach(pair -> {
            try {
                FileContainer templateContainer = new FileContainer(
                        DirTypes.TEMPLATE,
                        new File(pair.getOptionDir().getName() + ".conf"));
                if (templateContainer.getFullPath().exists()) {
                    packageList.addAll(
                            applyOptionsTemplate(
                                    templateContainer,
                                    DirTypes.INPUT_MANUAL,
                                    pair.getVideoList()));
                } else {
                    logger.warn("Skipping all files in {} because template file {} does not exist!",
                            pair.getOptionDir().getAbsolutePath(), templateContainer.getFullPath());
                }
            } catch (ParseException | IOException ex) {
                logger.warn("Could not read options: {}", ex);
            }
        });
        fireStateTrigger(new GenericCollectionTrigger(packageList));
    }

   

    @Override
    protected void exitState() {

    }

}
