package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import net.chrigel.clusterbrake.settings.SchedulerSettings;
import net.chrigel.clusterbrake.statemachine.states.SchedulerState;
import net.chrigel.clusterbrake.statemachine.states.StartupState;
import net.chrigel.clusterbrake.statemachine.states.TranscodingState;
import net.chrigel.clusterbrake.statemachine.trigger.InitializedStateTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.TranscodingFinishedTrigger;
import net.chrigel.clusterbrake.workflow.AbstractStateContext;

/**
 *
 */
public class ManualAutoWorkflow
        extends AbstractStateContext {

    @Inject
    ManualAutoWorkflow(
            StartupState initialState,
            ManualInputState manualInputState,
       //     TranscodingState transcodingState,
            CleanupState cleanupState,
            SchedulerState schedulerState,
            SchedulerSettings schedulerSettings
    ) {

        initialState.bindStateToTrigger(manualInputState, InitializedStateTrigger.class);
        schedulerState.setSettings(schedulerSettings);
      //  transcodingState.bindStateToTrigger(cleanupState, TranscodingFinishedTrigger.class);

        setStartupState(initialState);
    }

}
