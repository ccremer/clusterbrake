package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Inject;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.InitializedStateTrigger;
import net.chrigel.clusterbrake.settings.DirectorySettings;

/**
 *
 */
class InitialState
        extends AbstractState {

    private final DirectorySettings settings;

    @Inject
    InitialState(StateContext context, DirectorySettings settings) {
        super(context);
        this.settings = settings;
    }

    @Override
    protected void enterState() {

        logger.info("Creating base dirs...");
        settings.getManualInputBaseDir().mkdirs();
        settings.getAutomaticInputBaseDir().mkdirs();
        settings.getCustomPresetDir().mkdirs();

        settings.getStageManualDir().mkdirs();
        settings.getStageAutomaticDir().mkdirs();
        settings.getStageQueueDir().mkdirs();
        settings.getStageCurrentDir().mkdirs();
        settings.getStageFinishedDir().mkdirs();

        fireStateTrigger(new InitializedStateTrigger());
    }

    @Override
    protected void exitState() {
        
    }

}
