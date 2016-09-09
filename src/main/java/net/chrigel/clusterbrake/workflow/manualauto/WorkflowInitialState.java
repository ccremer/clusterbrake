package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.io.File;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;
import net.chrigel.clusterbrake.statemachine.trigger.ExceptionTrigger;
import net.chrigel.clusterbrake.statemachine.trigger.InitializedStateTrigger;

/**
 *
 */
public class WorkflowInitialState
        extends AbstractState {

    @Inject
    public WorkflowInitialState(
            StateContext context,
            @Named("dir.input") String inputDir,
            @Named("dir.output") String outputDir,
            @Named("dir.config") String configDir,
            @Named("dir.templates") String templateDir,
            @Named("workflow.queue.dir.temp") String tempDir,
            @Named("workflow.input.dir.auto") String autoInputDir,
            @Named("workflow.input.dir.manual") String manualInputDir,
            @Named("workflow.script.dir") String scriptDir
    ) {
        super(context);

        File inputFolder = new File(inputDir);
        File outputFolder = new File(outputDir);
        File configFolder = new File(configDir);

        DirTypes.CONFIG.setDir(new File(configDir));
        DirTypes.INPUT.setDir(inputFolder);
        DirTypes.INPUT_AUTO.setDir(new File(inputFolder, autoInputDir));
        DirTypes.INPUT_MANUAL.setDir(new File(inputFolder, manualInputDir));
        DirTypes.OUTPUT.setDir(outputFolder);
        DirTypes.OUTPUT_AUTO.setDir(new File(outputFolder, autoInputDir));
        DirTypes.OUTPUT_MANUAL.setDir(new File(outputFolder, manualInputDir));
        DirTypes.TMP.setDir(new File(tempDir));
        DirTypes.TEMPLATE.setDir(new File(configFolder, templateDir));
        DirTypes.SCRIPT.setDir(new File(scriptDir));
    }

    @Override
    protected void enterState() {

        logger.debug("Checking if base dirs exist...");
        if (!DirTypes.INPUT_AUTO.getBase().exists()) {
            fireStateTrigger(new ExceptionTrigger("Input directory 'auto' does not exist!"));
        }
        if (!DirTypes.INPUT_MANUAL.getBase().exists()) {
            fireStateTrigger(new ExceptionTrigger("Input directory 'manual' does not exist!"));
        }
        if (!DirTypes.OUTPUT.getBase().exists()) {
            logger.error("Output directory does not exist!");
        }
        DirTypes.TMP.getBase().mkdirs();

        fireStateTrigger(new InitializedStateTrigger());
    }

    @Override
    protected void exitState() {
    }

}
