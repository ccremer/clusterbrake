package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.chrigel.clusterbrake.settings.DirectorySettings;
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
    private final DirectorySettings dirSettings;

    @Inject
    CleanupState(
            StateContext context,
            JobSettings jobSettings,
            DirectorySettings dirSettings,
            CleanupSettings cleanupSettings
    ) {
        super(context);
        this.jobSettings = jobSettings;
        this.jobSettings.setSettingsFile(new File(dirSettings.getConfigBaseDir(), "finished.json"));
        this.dirSettings = dirSettings;
        this.cleanupSettings = cleanupSettings;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void setFinishedJob(Job job) {
        this.finishedJob = job;
    }

    @Override
    protected void enterState() {

        List<Job> jobs = jobSettings.getJobs();
        File source = this.finishedJob.getVideoPackage().getVideo().getSourceFile();
        if (cleanupSettings.isSourceDeletionEnabled()) {
            logger.info("Deleting {}", source);
            source.delete();
        } else {
            jobs.add(finishedJob);
        }

        File finalOutput = new File(
                dirSettings.getOutputBaseDir(),
                finishedJob.getVideoPackage().getVideo().getSourceFile().getPath());
        finishedJob.getVideoPackage().setOutputFile(finalOutput);
        jobSettings.setJobs(jobs);
        if (cleanupSettings.isAsyncMoveEnabled()) {
            executor.submit(() -> {
                moveFile(finishedJob.getVideoPackage().getOutputFile(), finalOutput);
            });
        } else {
            moveFile(finishedJob.getVideoPackage().getOutputFile(), finalOutput);
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

    @Override
    protected void exitState() {
    }

}
