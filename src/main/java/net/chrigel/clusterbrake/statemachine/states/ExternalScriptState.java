package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.ExceptionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.ScriptExecutedTrigger;

/**
 *
 */
public class ExternalScriptState
        extends AbstractState {

    private File file;
    private Process process;
    private List<String> arguments;

    @Inject
    public ExternalScriptState(StateContext context) {
        super(context);
    }

    public void setScriptFile(File file) {
        this.file = file;
        
    }

    public void setArguments(List<String> arguments) {     
        this.arguments = arguments;
    }

    @Override
    protected void enterState() {

        if (file != null && file.exists()) {

            ProcessBuilder processBuilder = new ProcessBuilder(addOptionsToArguments(arguments));

            processBuilder.inheritIO();

            logger.info("Invoking external script: {}", file);
            try {
                this.process = processBuilder.start();
                int retVal = this.process.waitFor();
                logger.info("Script return code: {}", retVal);
                fireStateTrigger(new ScriptExecutedTrigger(retVal));
            } catch (IOException | InterruptedException ex) {
                fireStateTrigger(new ExceptionTrigger(ex));
            }
        } else {
            fireStateTrigger(new ScriptExecutedTrigger(-1));
        }
    }

    @Override
    protected void exitState() {
        if (process != null) {
            try {
                process.destroyForcibly();
                process.waitFor(1, TimeUnit.MINUTES);
            } catch (InterruptedException ex) {
                logger.warn("Could not destroy child process: {}", ex);
            }
        }
    }

    private List<String> addOptionsToArguments(List<String> options) {
        List<String> list = new LinkedList<>();
        list.add(file.getPath());
        if (options != null) {
            list.addAll(options);
        }
        return list;
    }

}
