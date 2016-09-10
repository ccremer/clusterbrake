package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.chrigel.clusterbrake.process.CommandLineInterpreter;
import net.chrigel.clusterbrake.process.ExternalProcess;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.ExceptionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.ScriptExecutedTrigger;

/**
 *
 */
public class ExternalScriptState
        extends AbstractState {

    private File file;
    private ExternalProcess process;
    private List<String> arguments;
    private final Provider<ExternalProcess> processProvider;
    private final CommandLineInterpreter interpreter;

    @Inject
    public ExternalScriptState(StateContext context,
            Provider<ExternalProcess> processProvider,
            CommandLineInterpreter interpreter) {
        super(context);
        this.processProvider = processProvider;
        this.interpreter = interpreter;
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
            try {
                this.process = processProvider.get();
                int retVal = process
                        .withIORedirected(true)
                        .withArguments(addFileToArguments(file, arguments))
                        .withPath(interpreter.getPath())
                        .start();
                logger.info("Script return code: {}", retVal);
                fireStateTrigger(new ScriptExecutedTrigger(retVal));
            } catch (IOException | InterruptedException ex) {
                fireStateTrigger(new ExceptionTrigger(ex));
            }
        } else {
            fireStateTrigger(new ScriptExecutedTrigger(-1));
        }
    }

    private List<String> addFileToArguments(File file, List<String> args) {
        List<String> list = new LinkedList<>();
        list.add(file.getPath());
        if (args != null) {
            list.addAll(args);
        }
        return list;
    }

    @Override
    protected void exitState() {
        if (process != null) {
            try {
                process.destroy(1, TimeUnit.MINUTES);
            } catch (InterruptedException ex) {
                logger.warn("Could not destroy child process: {}", ex);
            }
        }
    }

}
