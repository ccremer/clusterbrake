package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.chrigel.clusterbrake.media.DirType;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.settings.JobSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;
import net.chrigel.clusterbrake.workflow.manualauto.settings.CleanupSettings;
import org.apache.commons.io.FileUtils;

/**
 *
 */
public class CleanupState
        extends AbstractState {

    private final JobSettings jobSettings;
    private Job finishedJob;
    private final CleanupSettings cleanupSettings;
    private final ExecutorService executor;

    @Inject
    CleanupState(
            StateContext context,
            JobSettings jobSettings,
            CleanupSettings cleanupSettings
    ) {
        super(context);
        this.jobSettings = jobSettings;
        this.jobSettings.setSettingsFile(new File(DirTypes.CONFIG.getBase(), "finished.json"));
        this.cleanupSettings = cleanupSettings;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void setFinishedJob(Job job) {
        this.finishedJob = job;
    }

    @Override
    protected void enterState() {

        List<Job> jobs = jobSettings.getJobs();
        File source = this.finishedJob.getVideoPackage().getVideo().getSourceFile().getFullPath();
        if (cleanupSettings.isSourceDeletionEnabled()) {
            logger.info("Deleting {}", source);
            source.delete();
        } else {
            jobs.add(finishedJob);
        }
        File temp = finishedJob.getVideoPackage().getOutputFile().getFullPath();
        DirType outputType = finishedJob.getVideoPackage().getOutputFile().getType();
        finishedJob.getVideoPackage().getOutputFile().setDirType(convertOutputMode(outputType));
        File finalOutput = finishedJob.getVideoPackage().getOutputFile().getFullPath();
        jobSettings.setJobs(jobs);
        if (cleanupSettings.isAsyncMoveEnabled()) {
            executor.submit(() -> {
                moveFile(temp, finalOutput);
            });
        } else {
            moveFile(temp, finalOutput);
        }

    }

    private void moveFile(File from, File to) {

        logger.info("Moving file {} to {}", from, to);
        try {
            FileUtils.moveFile(from, to);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    private DirType convertOutputMode(DirType type) {
        if (type instanceof DirTypes) {
            switch ((DirTypes) type) {
                case INPUT_AUTO:
                    return DirTypes.OUTPUT_AUTO;
                case INPUT_MANUAL:
                    return DirTypes.OUTPUT_MANUAL;
                default:
                    return DirTypes.OUTPUT;
            }
        } else {
            return DirTypes.OUTPUT;
        }
    }

    @Override
    protected void exitState() {
    }

}
