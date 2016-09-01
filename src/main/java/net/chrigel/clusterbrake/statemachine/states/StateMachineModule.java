package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.chrigel.clusterbrake.statemachine.StateContext;

/**
 *
 */
public class StateMachineModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(StateContext.class).to(ClusterContext.class).in(Singleton.class);
    }

}
