package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.chrigel.clusterbrake.statemachine.StateContext;

/**
 *
 */
public class WorkflowModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(StateContext.class).to(ManualAutoWorkflow.class).in(Singleton.class);
    }

}
