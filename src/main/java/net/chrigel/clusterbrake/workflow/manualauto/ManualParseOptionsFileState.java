package net.chrigel.clusterbrake.workflow.manualauto;

import net.chrigel.clusterbrake.statemachine.states.AbstractOptionParseState;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.settings.TemplateSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.ExceptionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.GenericCollectionTrigger;
import net.chrigel.clusterbrake.workflow.manualauto.settings.OptionDirVideoPair;
import net.chrigel.clusterbrake.workflow.manualauto.settings.WorkflowTemplateSettings;

/**
 *
 */
class ManualParseOptionsFileState
        extends AbstractOptionParseState {

    private List<OptionDirVideoPair> list;
    private final TemplateSettings templateSettings;
    private final WorkflowTemplateSettings workflowTemplateSettings;

    @Inject
    ManualParseOptionsFileState(
            StateContext context,
            TemplateSettings templateSettings,
            WorkflowTemplateSettings workflowTemplateSettings,
            Provider<VideoOptionPackage> optionPackageProvider,
            Provider<OptionsFileParser> optionParserProvider,
            Provider<VideoPackage> videoPackageProvider
    ) {
        super(context, optionPackageProvider, optionParserProvider, videoPackageProvider);
        this.templateSettings = templateSettings;
        this.workflowTemplateSettings = workflowTemplateSettings;
    }

    public void setOptionDirList(List<OptionDirVideoPair> list) {
        this.list = list;
    }

    @Override
    protected void enterState() {

        if (!workflowTemplateSettings.getDefaultManualTemplate().exists()) {
            logger.error("Default template for manual encoding does not exist!");
            fireStateTrigger(new ExceptionTrigger("Default template for manual encoding does not exist!"));
        }
        List<VideoPackage> packageList = new LinkedList<>();
        /**
         * Find and apply the options file for each template dir.
         */
        list.forEach(pair -> {
            try {
                if (pair.getOptionDir() == null) {
                    packageList.addAll(
                            applyOptionsTemplate(
                                    workflowTemplateSettings.getDefaultManualTemplate(),
                                    pair.getVideoList()));
                } else {
                    File template = new File(
                            templateSettings.getTemplateDir(),
                            pair.getOptionDir().getName() + ".conf");
                    if (template.exists()) {
                        packageList.addAll(
                                applyOptionsTemplate(
                                        template,
                                        pair.getVideoList()));
                    } else {
                        packageList.addAll(
                                applyOptionsTemplate(
                                        workflowTemplateSettings.getDefaultManualTemplate(),
                                        pair.getVideoList()));
                    }
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
