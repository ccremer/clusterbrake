package net.chrigel.clusterbrake.workflow.manualauto.settings.impl;

import com.google.inject.AbstractModule;
import net.chrigel.clusterbrake.settings.SchedulerSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.CleanupSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.WorkflowTemplateSettings;

/**
 *
 */
public class WorkflowSettingsModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(InputSettings.class).to(InputSettingsImpl.class);
        bind(SchedulerSettings.class).to(SchedulerSettingsImpl.class);
        bind(WorkflowTemplateSettings.class).to(WorkflowTemplateSettingsImpl.class);
        bind(CleanupSettings.class).to(CleanupSettingsImpl.class);
    }

}
