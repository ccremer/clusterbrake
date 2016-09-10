package net.chrigel.clusterbrake.workflow.manualauto.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.chrigel.clusterbrake.workflow.manualauto.settings.WorkflowTemplateSettings;

/**
 *
 */
class WorkflowTemplateSettingsImpl
        implements WorkflowTemplateSettings {

    private final String defaultAutoTemplate;

    @Inject
    WorkflowTemplateSettingsImpl(
            @Named("workflow.template.auto.defaultFilename") String autoFileName
    ) {
        this.defaultAutoTemplate = autoFileName;
    }

    @Override
    public String getDefaultAutoTemplate() {
        return defaultAutoTemplate;
    }

}
