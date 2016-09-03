package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.settings.TemplateSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;
import net.chrigel.clusterbrake.statemachine.trigger.ErrorTrigger;
import net.chrigel.clusterbrake.workflow.manualauto.settings.OptionDirVideoPair;
import net.chrigel.clusterbrake.workflow.manualauto.settings.WorkflowTemplateSettings;

/**
 *
 */
public class ParseOptionsFileState
        extends AbstractState {

    private List<OptionDirVideoPair> list;
    private final TemplateSettings templateSettings;
    private final WorkflowTemplateSettings workflowTemplateSettings;
    private final Provider<FileScanner<VideoOptionPackage>> optionScannerProvider;
    private final Provider<VideoOptionPackage> optionPackageProvider;
    private final Provider<OptionsFileParser> optionParserProvider;
    private final Provider<VideoPackage> videoPackageProvider;

    @Inject
    ParseOptionsFileState(
            StateContext context,
            TemplateSettings templateSettings,
            WorkflowTemplateSettings workflowTemplateSettings,
            Provider<FileScanner<VideoOptionPackage>> optionScannerProvider,
            Provider<VideoOptionPackage> optionPackageProvider,
            Provider<OptionsFileParser> optionParserProvider,
            Provider<VideoPackage> videoPackageProvider
    ) {
        super(context);
        this.templateSettings = templateSettings;
        this.workflowTemplateSettings = workflowTemplateSettings;
        this.optionScannerProvider = optionScannerProvider;
        this.optionPackageProvider = optionPackageProvider;
        this.optionParserProvider = optionParserProvider;
        this.videoPackageProvider = videoPackageProvider;
    }

    public void setVideoList(List<OptionDirVideoPair> list) {
        this.list = list;
    }

    @Override
    protected void enterState() {

        if (!workflowTemplateSettings.getDefaultManualTemplate().exists()) {
            logger.error("Default template for manual encoding does not exist!");
            fireStateTrigger(new ErrorTrigger("Default template for manual encoding does not exist!"));
        }
        List<VideoPackage> packageList = new LinkedList<>();
        /**
         * Find and apply the options file for each template dir.
         */
        list.forEach(pair -> {
            try {
                File template = new File(templateSettings.getTemplateDir(), pair.getOptionDir().getName() + ".conf");
                if (template.exists()) {
                    VideoOptionPackage pkg = optionPackageProvider.get();
                    pkg.setOptionFile(template);
                    pkg = optionParserProvider.get().parseFile(pkg);
                }
                
            } catch (ParseException | IOException ex) {
                logger.error("Could not read options: {}", ex);
            }
        });
    }

    @Override
    protected void exitState() {

    }

}
