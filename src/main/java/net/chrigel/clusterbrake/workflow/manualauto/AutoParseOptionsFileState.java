package net.chrigel.clusterbrake.workflow.manualauto;

import net.chrigel.clusterbrake.statemachine.states.AbstractOptionParseState;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.ExceptionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.GenericCollectionTrigger;
import net.chrigel.clusterbrake.transcode.TranscoderSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.WorkflowTemplateSettings;

/**
 *
 */
class AutoParseOptionsFileState
        extends AbstractOptionParseState {

    private final WorkflowTemplateSettings workflowTemplateSettings;
    private List<VideoPackage> videos;
    private final TranscoderSettings transcoderSettings;

    @Inject
    AutoParseOptionsFileState(
            StateContext context,
            WorkflowTemplateSettings workflowTemplateSettings,
            Provider<VideoOptionPackage> optionPackageProvider,
            Provider<OptionsFileParser> optionParserProvider,
            TranscoderSettings transcoderSettings
    ) {
        super(context, optionPackageProvider, optionParserProvider);
        this.workflowTemplateSettings = workflowTemplateSettings;
        this.transcoderSettings = transcoderSettings;
    }

    public void setVideoList(List<VideoPackage> videos) {
        this.videos = videos;
    }

    @Override
    protected void enterState() {
        FileContainer container = new FileContainer(DirTypes.TEMPLATE,
                workflowTemplateSettings.getDefaultAutoTemplate() + "." + transcoderSettings.getOptionsFileExtension());
        File defaultTemplate = container.getFullPath();
        if (!defaultTemplate.exists()) {
            fireStateTrigger(new ExceptionTrigger(
                    "Default template for auto encoding does not exist!",
                    new FileNotFoundException(defaultTemplate.getAbsolutePath()),
                    getClass()));
        }
        try {
            List<VideoPackage> packageList = applyOptionsTemplate(
                    container, videos);
            fireStateTrigger(new GenericCollectionTrigger(packageList));
        } catch (IOException | ParseException ex) {
            fireStateTrigger(new ExceptionTrigger("Could not apply template file.", ex, getClass()));
        }

    }

    @Override
    protected void exitState() {
    }

}
