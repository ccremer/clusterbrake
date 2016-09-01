package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.chrigel.clusterbrake.settings.SchedulerSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettingsImpl;
import net.chrigel.clusterbrake.workflow.manualauto.settings.SchedulerSettingsImpl;

/**
 *
 */
public class WorkflowModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(StateContext.class).to(ManualAutoWorkflow.class).in(Singleton.class);
        
        bind(InputSettings.class).to(InputSettingsImpl.class);
        bind(SchedulerSettings.class).to(SchedulerSettingsImpl.class);
    }

}
