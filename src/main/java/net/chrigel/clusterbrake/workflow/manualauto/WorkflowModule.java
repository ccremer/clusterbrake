package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.workflow.manualauto.settings.impl.WorkflowSettingsModule;

/**
 *
 */
public class WorkflowModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(StateContext.class).to(ManualAutoWorkflow.class).in(Singleton.class);

        install(new WorkflowSettingsModule());

    }

}
