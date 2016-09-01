package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Inject;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.InitializedStateTrigger;
import net.chrigel.clusterbrake.settings.DirectorySettings;

/**
 *
 */
public class StartupState
        extends AbstractState {

    private final DirectorySettings settings;

    @Inject
    public StartupState(StateContext context, DirectorySettings settings) {
        super(context);
        this.settings = settings;
    }

    @Override
    protected void enterState() {

        logger.debug("Checking if base dirs exist...");
        if (!settings.getInputBaseDir().exists()) {
            logger.error("Input directory does not exist!");
        }
        if (!settings.getOutputBaseDir().exists()) {
            logger.error("Output directory does not exist!");
        }
        
        fireStateTrigger(new InitializedStateTrigger());
    }

    @Override
    protected void exitState() {
        
    }

}
