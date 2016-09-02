package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.settings.SchedulerSettings;
import net.chrigel.clusterbrake.statemachine.states.SchedulerState;
import net.chrigel.clusterbrake.statemachine.states.StartupState;
import net.chrigel.clusterbrake.statemachine.states.TranscodingState;
import net.chrigel.clusterbrake.statemachine.trigger.InitializedStateTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.ListResultTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.TranscodingFinishedTrigger;
import net.chrigel.clusterbrake.util.UnsafeCastUtil;
import net.chrigel.clusterbrake.workflow.AbstractStateContext;

/**
 *
 */
public class ManualAutoWorkflow
        extends AbstractStateContext {

    @Inject
    ManualAutoWorkflow(
            StartupState startupState,
            WorkflowInitialState initialState,
            ScanManualInputDirState scanManualInputState,
            OptionsFileSearchState optionsFileSearchState,
       //     TranscodingState transcodingState,
            CleanupState cleanupState,
            SchedulerState schedulerState,
            SchedulerSettings schedulerSettings
    ) {

        startupState.bindNextStateToTrigger(initialState, InitializedStateTrigger.class);
        initialState.bindNextStateToTrigger(scanManualInputState, InitializedStateTrigger.class);
        scanManualInputState.bindNextStateToTrigger(optionsFileSearchState, ListResultTrigger.class, trigger -> {
            optionsFileSearchState.setVideoList(UnsafeCastUtil.cast(trigger.getList()));
            return null;
        });
        schedulerState.setSettings(schedulerSettings);
      //  transcodingState.bindStateToTrigger(cleanupState, TranscodingFinishedTrigger.class);

        setStartupState(startupState);
    }

}
