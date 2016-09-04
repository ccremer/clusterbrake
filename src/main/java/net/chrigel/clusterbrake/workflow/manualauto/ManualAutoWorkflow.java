package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.settings.SchedulerSettings;
import net.chrigel.clusterbrake.settings.constraint.FileSizeConstraint;
import net.chrigel.clusterbrake.settings.constraint.TimeConstraint;
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
            ParseOptionsFileState parseOptionsState,
            QueueSelectState queueSelectState,
            //     TranscodingState transcodingState,
            CleanupState cleanupState,
            SchedulerState schedulerState,
            SchedulerSettings schedulerSettings,
            FileSizeConstraint fileSizeConstraint,
            TimeConstraint timeConstraint
    ) {

        // startup --> Initialize Workflow
        startupState.bindNextStateToTrigger(initialState, InitializedStateTrigger.class);

        // WorkflowInit --> Manual Video Scan
        initialState.bindNextStateToTrigger(scanManualInputState, InitializedStateTrigger.class);

        // manual video scan --> parse option files
        scanManualInputState.bindNextStateToTrigger(parseOptionsState, ListResultTrigger.class, listTrigger -> {
            parseOptionsState.setVideoList(UnsafeCastUtil.cast(listTrigger.getList()));
            return null;
        });

        // parse options --> select job to queue
        parseOptionsState.bindNextStateToTrigger(queueSelectState, ListResultTrigger.class, trigger -> {
            queueSelectState.setVideoPackageList(trigger.getList());
            return null;
        });

        queueSelectState.setConstraints(fileSizeConstraint, timeConstraint);

        schedulerState.setSettings(schedulerSettings);
        //  transcodingState.bindStateToTrigger(cleanupState, TranscodingFinishedTrigger.class);

        setStartupState(startupState);
    }

}
