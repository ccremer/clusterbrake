package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.chrigel.clusterbrake.media.DirType;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.settings.JobSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;
import net.chrigel.clusterbrake.statemachine.trigger.MessageTrigger;
import net.chrigel.clusterbrake.workflow.manualauto.settings.CleanupSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.FinishedJobSettings;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

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
            @FinishedJobSettings JobSettings jobSettings,
            CleanupSettings cleanupSettings
    ) {
        super(context);
        this.jobSettings = jobSettings;
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
        finishedJob.getVideoPackage().getOutputFile().setDirType(DirTypes.OUTPUT);
        File finalOutput = finishedJob.getVideoPackage().getOutputFile().getFullPath();
        jobSettings.setJobs(jobs);
        if (cleanupSettings.isAsyncMoveEnabled()) {
            executor.submit(() -> {
                moveFile(temp, finalOutput);
                cleanDir();
            });
        } else {
            moveFile(temp, finalOutput);
            cleanDir();
        }

        fireStateTrigger(new MessageTrigger(String.format("Finished job %1$s", source.getPath())));
    }

    private void moveFile(File from, File to) {
        File dest = to;
        if (to.exists()) {
            File parent = to.getParentFile();
            String fileName = to.getName();
            String extension = FilenameUtils.getExtension(fileName);
            String newfileName = fileName + new SimpleDateFormat("yyyy-MM-dd-hh-mm").format(new Date()) + extension;
            dest = new File(parent, newfileName);
        }
        logger.info("Moving file {} to {}", from, dest);
        try {
            FileUtils.moveFile(from, dest);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    private void cleanDir() {
        try {
            FileUtils.cleanDirectory(DirTypes.TEMP.getBase());
        } catch (IOException ex) {
            logger.warn(ex);
        }
    }

    @Override
    protected void exitState() {
    }

}
