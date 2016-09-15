package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.util.LinkedList;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.settings.SchedulerSettings;
import net.chrigel.clusterbrake.settings.constraint.FileSizeConstraint;
import net.chrigel.clusterbrake.settings.constraint.TimeConstraint;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;
import net.chrigel.clusterbrake.statemachine.states.ErrorState;
import net.chrigel.clusterbrake.statemachine.states.ExternalScriptState;
import net.chrigel.clusterbrake.statemachine.states.SchedulerState;
import net.chrigel.clusterbrake.statemachine.states.TranscodingState;
import net.chrigel.clusterbrake.statemachine.trigger.ExceptionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.GenericCollectionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.InitializedStateTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.MessageTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.QueueResultTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.ScheduledActionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.ScriptExecutedTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.TranscodingFinishedTrigger;
import net.chrigel.clusterbrake.workflow.AbstractStateContext;
import net.chrigel.clusterbrake.workflow.manualauto.settings.OptionDirVideoPair;
import net.chrigel.clusterbrake.workflow.manualauto.settings.WorkflowScriptSettings;
import net.chrigel.clusterbrake.workflow.manualauto.triggers.NoResultTrigger;

/**
 *
 */
public class ManualAutoWorkflow
        extends AbstractStateContext {

    @Inject
    ManualAutoWorkflow(
            ErrorState errorState,
            WorkflowInitialState initialState,
            ScanManualInputDirState scanManualInputState,
            ScanAutoInputDirState scanAutoInputState,
            ManualParseOptionsFileState manualParseOptionsState,
            AutoParseOptionsFileState autoParseOptionsState,
            QueueSelectState queueSelectState,
            TranscodingState transcodingState,
            CleanupState cleanupState,
            SchedulerState schedulerState,
            Provider<ExternalScriptState> scriptProvider,
            SchedulerSettings schedulerSettings,
            FileSizeConstraint fileSizeConstraint,
            TimeConstraint timeConstraint,
            WorkflowScriptSettings scriptSettings
    ) {
        bindErrorStates(errorState,
                initialState,
                scanManualInputState,
                scanAutoInputState,
                manualParseOptionsState,
                autoParseOptionsState,
                queueSelectState,
                transcodingState,
                cleanupState);

        // set static state settings
        queueSelectState.setConstraints(fileSizeConstraint, timeConstraint);
        schedulerState.setSettings(schedulerSettings);

        // WorkflowInit --> Manual Video Scan
        initialState.bindNextStateToTrigger(scanManualInputState, InitializedStateTrigger.class);

        // manual video scan [results found] --> parse option files
        scanManualInputState.bindNextStateToTrigger(manualParseOptionsState, GenericCollectionTrigger.class,
                trigger -> {
                    manualParseOptionsState.setOptionDirList(trigger.getList(OptionDirVideoPair.class));
                    return null;
                });

        // manual video scan [no results found] --> auto video scan
        scanManualInputState.bindNextStateToTrigger(scanAutoInputState, NoResultTrigger.class, trigger -> {
            scanAutoInputState.setVideoList(new LinkedList<>());
            return null;
        });

        // manual parse options --> select job to queue
        manualParseOptionsState.bindNextStateToTrigger(scanAutoInputState, GenericCollectionTrigger.class, trigger -> {
            scanAutoInputState.setVideoList(trigger.getList(VideoPackage.class));
            return null;
        });

        // auto video scan --> parse option files
        scanAutoInputState.bindNextStateToTrigger(autoParseOptionsState, GenericCollectionTrigger.class, trigger -> {
            autoParseOptionsState.setVideoList(trigger.getList(VideoPackage.class));
            return null;
        });

        // auto parse options --> select job to queue
        autoParseOptionsState.bindNextStateToTrigger(queueSelectState, GenericCollectionTrigger.class, trigger -> {
            queueSelectState.setVideoPackageList(trigger.getList(VideoPackage.class));
            return null;
        });

        // queue select [no result found] --> schedule next scan
        queueSelectState.bindNextStateToTrigger(schedulerState, MessageTrigger.class);
            
        // after some minutes --> manual scan
        schedulerState.bindNextStateToTrigger(scanManualInputState, ScheduledActionTrigger.class);

        ExternalScriptState postQueueState = scriptProvider.get();
        postQueueState.setScriptFile(new File(DirTypes.SCRIPT.getBase(), scriptSettings.getPostQueueScriptName()));

        // queue select [result found] --> post queue
        queueSelectState.bindNextStateToTrigger(postQueueState, QueueResultTrigger.class, trigger -> {
            transcodingState.setJob(trigger.getPayload());
            return null;
        });

        // post queue script --> transcode
        postQueueState.bindNextStateToTrigger(transcodingState, ScriptExecutedTrigger.class);

        // transcoding finished --> cleanup
        transcodingState.bindNextStateToTrigger(cleanupState, TranscodingFinishedTrigger.class, trigger -> {
            cleanupState.setFinishedJob(trigger.getPayload());
            queueSelectState.clearQueue();
            return null;
        });

        ExternalScriptState postCleanupState = scriptProvider.get();
        postCleanupState.setScriptFile(new File(DirTypes.SCRIPT.getBase(), scriptSettings.getPostCleanupScriptName()));

        // cleanup --> post cleanup
        cleanupState.bindNextStateToTrigger(postCleanupState, MessageTrigger.class);

        // post cleanup --> manual scan
        postCleanupState.bindNextStateToTrigger(scanManualInputState, ScriptExecutedTrigger.class);

        bindErrorStates(errorState, postCleanupState, postQueueState);

        setStartupState(initialState);
    }

    private void bindErrorStates(ErrorState errorState, AbstractState... states) {
        errorState.setApplicationExitEnabled(true);
        errorState.setLoggingEnabled(true);
        for (AbstractState state : states) {
            state.bindNextStateToTrigger(errorState, ExceptionTrigger.class, trigger -> {
                errorState.setExceptionTrigger(trigger);
                return null;
            });
        }
    }

}
