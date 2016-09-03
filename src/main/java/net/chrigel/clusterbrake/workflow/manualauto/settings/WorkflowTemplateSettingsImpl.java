package net.chrigel.clusterbrake.workflow.manualauto.settings;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.io.File;
import net.chrigel.clusterbrake.settings.TemplateSettings;

/**
 *
 */
public class WorkflowTemplateSettingsImpl
        implements WorkflowTemplateSettings {

    private final File defaultAutoTemplate;
    private final File defaultManualTemplate;

    @Inject
    WorkflowTemplateSettingsImpl(
            @Named("workflow.template.manual.defaultFilename") String manualFileName,
            @Named("workflow.template.auto.defaultFilename") String autoFileName,
            TemplateSettings settings
    ) {
        this.defaultAutoTemplate = new File(settings.getTemplateDir(), autoFileName);
        this.defaultManualTemplate = new File(settings.getTemplateDir(), manualFileName);
    }

    @Override
    public File getDefaultManualTemplate() {
        return defaultManualTemplate;
    }

    @Override
    public File getDefaultAutoTemplate() {
        return defaultAutoTemplate;
    }

}
