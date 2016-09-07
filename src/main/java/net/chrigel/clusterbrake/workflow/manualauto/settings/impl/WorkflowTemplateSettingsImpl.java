package net.chrigel.clusterbrake.workflow.manualauto.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.io.File;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.workflow.manualauto.DirTypes;
import net.chrigel.clusterbrake.workflow.manualauto.settings.WorkflowTemplateSettings;

/**
 *
 */
class WorkflowTemplateSettingsImpl
        implements WorkflowTemplateSettings {

    private final FileContainer defaultAutoTemplate;

    @Inject
    WorkflowTemplateSettingsImpl(
            @Named("workflow.template.auto.defaultFilename") String autoFileName
    ) {
        this.defaultAutoTemplate = new FileContainer(DirTypes.TEMPLATE, new File(autoFileName));
    }

    @Override
    public FileContainer getDefaultAutoTemplate() {
        return defaultAutoTemplate;
    }

}
