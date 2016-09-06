package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.settings.SchedulerSettings;
import net.chrigel.clusterbrake.settings.constraint.FileSizeConstraint;
import net.chrigel.clusterbrake.settings.constraint.TimeConstraint;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;
import net.chrigel.clusterbrake.statemachine.states.ErrorState;
import net.chrigel.clusterbrake.statemachine.states.SchedulerState;
import net.chrigel.clusterbrake.statemachine.states.StartupState;
import net.chrigel.clusterbrake.statemachine.states.TranscodingState;
import net.chrigel.clusterbrake.statemachine.trigger.ExceptionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.GenericCollectionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.InitializedStateTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.MessageTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.QueueResultTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.TranscodingFinishedTrigger;
import net.chrigel.clusterbrake.workflow.AbstractStateContext;
import net.chrigel.clusterbrake.workflow.manualauto.settings.OptionDirVideoPair;

/**
 *
 */
public class ManualAutoWorkflow
        extends AbstractStateContext {

    @Inject
    ManualAutoWorkflow(
            StartupState startupState,
            ErrorState errorState,
            WorkflowInitialState initialState,
            ScanManualInputDirState scanManualInputState,
            ManualParseOptionsFileState parseOptionsState,
            QueueSelectState queueSelectState,
            TranscodingState transcodingState,
            CleanupState cleanupState,
            SchedulerState schedulerState,
            SchedulerSettings schedulerSettings,
            FileSizeConstraint fileSizeConstraint,
            TimeConstraint timeConstraint
    ) {
        bindErrorStates(errorState,
                initialState,
                scanManualInputState,
                parseOptionsState,
                queueSelectState,
                transcodingState,
                cleanupState);

        // set static state settings
        queueSelectState.setConstraints(fileSizeConstraint, timeConstraint);
        schedulerState.setSettings(schedulerSettings);

        // startup --> Initialize Workflow
        startupState.bindNextStateToTrigger(initialState, InitializedStateTrigger.class);

        // WorkflowInit --> Manual Video Scan
        initialState.bindNextStateToTrigger(scanManualInputState, InitializedStateTrigger.class);

        // manual video scan --> parse option files
        scanManualInputState.bindNextStateToTrigger(parseOptionsState, GenericCollectionTrigger.class, listTrigger -> {
            parseOptionsState.setOptionDirList(listTrigger.getList(OptionDirVideoPair.class));
            return null;
        });

        // parse options --> select job to queue
        parseOptionsState.bindNextStateToTrigger(queueSelectState, GenericCollectionTrigger.class, trigger -> {
            queueSelectState.setVideoPackageList(trigger.getList(VideoPackage.class));
            return null;
        });

        // queue select [no result found] --> schedule next scan
        queueSelectState.bindNextStateToTrigger(schedulerState, MessageTrigger.class);

        // queue select [result found] --> transcode
        queueSelectState.bindNextStateToTrigger(transcodingState, QueueResultTrigger.class, trigger -> {
            transcodingState.setJob(trigger.getPayload());
            return null;
        });

        // transcoding finished --> cleanup
        transcodingState.bindNextStateToTrigger(cleanupState, TranscodingFinishedTrigger.class, trigger -> {
            cleanupState.setFinishedJob(trigger.getPayload());
            return null;
        });

        setStartupState(startupState);
    }

    private void bindErrorStates(ErrorState errorState, AbstractState... states) {
        errorState.setApplicationExitEnabled(false);
        errorState.setLoggingEnabled(true);
        for (AbstractState state : states) {
            state.bindNextStateToTrigger(errorState, ExceptionTrigger.class, trigger -> {
                errorState.setExceptionTrigger(trigger);
                return null;
            });
        }
    }

}
