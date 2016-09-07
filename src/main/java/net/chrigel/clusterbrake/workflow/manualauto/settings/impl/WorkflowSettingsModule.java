package net.chrigel.clusterbrake.workflow.manualauto.settings.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.owlike.genson.Converter;
import java.io.File;
import net.chrigel.clusterbrake.media.DirType;
import net.chrigel.clusterbrake.settings.JobSettings;
import net.chrigel.clusterbrake.settings.SchedulerSettings;
import net.chrigel.clusterbrake.settings.impl.JobSettingsImpl;
import net.chrigel.clusterbrake.workflow.manualauto.DirTypes;
import net.chrigel.clusterbrake.workflow.manualauto.settings.CleanupSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.FinishedJobSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.QueueSettings;
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
        bind(new TypeLiteral<Converter<DirType>>() {
        }).to(DirTypeConverter.class);
    }

    @Provides
    @QueueSettings
    private JobSettings provideQueueSettings(JobSettingsImpl impl) {
        File settingsFile = new File(DirTypes.CONFIG.getBase(), "queue.json");
        impl.setSettingsFile(settingsFile);
        return impl;
    }

    @Provides
    @FinishedJobSettings
    private JobSettings provideFinishedSettings(JobSettingsImpl impl) {
        File settingsFile = new File(DirTypes.CONFIG.getBase(), "finished.json");
        impl.setSettingsFile(settingsFile);
        return impl;
    }

}
