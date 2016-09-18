package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.settings.NodeSettings;
import net.chrigel.clusterbrake.settings.constraint.Constraint;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;
import net.chrigel.clusterbrake.statemachine.trigger.MessageTrigger;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.statemachine.trigger.QueueResultTrigger;
import net.chrigel.clusterbrake.settings.JobSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.FinishedJobSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.OutputSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.QueueSettings;
import org.apache.commons.io.FilenameUtils;

/**
 *
 */
public class QueueSelectState
        extends AbstractState {

    private List<VideoPackage> videos;
    private List<Constraint> constraints;
    private final JobSettings jobSettings;
    private final JobSettings finishedSettings;
    private final NodeSettings nodeSettings;
    private final Provider<Job> jobProvider;
    private final OutputSettings outputSettings;

    @Inject
    QueueSelectState(
            StateContext context,
            @QueueSettings JobSettings jobSettings,
            NodeSettings nodeSettings,
            @FinishedJobSettings JobSettings finishedJobs,
            Provider<Job> queueProvider,
            OutputSettings outputSettings
    ) {
        super(context);
        this.jobSettings = jobSettings;
        this.finishedSettings = finishedJobs;
        this.nodeSettings = nodeSettings;
        this.jobProvider = queueProvider;
        this.outputSettings = outputSettings;
    }

    public void setVideoPackageList(List<VideoPackage> list) {
        this.videos = list;
    }

    public void setConstraints(Constraint... constraints) {
        this.constraints = Arrays.asList(constraints);
    }

    public void clearQueue() {
        logger.debug("Clearing jobs...");
        jobSettings.setJobs(cleanUpQueue(jobSettings.getJobs()));
    }

    @Override
    protected void enterState() {

        try {
            List<Job> queue = cleanUpQueue(jobSettings.getJobs());
            List<Job> finished = finishedSettings.getJobs();

            VideoPackage videoPackage = videos.stream()
                    .filter(pkg -> {
                        return finished.stream().allMatch(job -> {
                            FileContainer source = job.getVideoPackage().getSourceFile();
                            boolean found = source.equals(pkg.getSourceFile());
                            if (found) {
                                logger.info("Skipping because it is already converted: {}:{}",
                                        source.getType(),
                                        source.getFile());
                            }
                            return !found;
                        });
                    })
                    .filter(pkg -> {
                        return queue.stream().allMatch(job -> {
                            FileContainer source = job.getVideoPackage().getSourceFile();
                            boolean found = source.equals(pkg.getSourceFile());
                            if (found) {
                                logger.info("Skipping because {} is already encoding it: {}",
                                        job.getNodeID(), source.getFullPath());
                            }
                            return !found;
                        });
                    })
                    .sorted((o1, o2) -> {
                        /**
                         * Put MANUAL input dirs first in line.
                         */
                        return Boolean.compare(
                                !DirTypes.INPUT_MANUAL.equals(o1.getSourceFile().getType()),
                                !DirTypes.INPUT_MANUAL.equals(o2.getSourceFile().getType()));
                    })
                    .filter(pkg -> {
                        if (constraints == null) {
                            return true;
                        } else {
                            return constraints.stream().allMatch(constraint -> {
                                boolean accepted = constraint.accept(pkg);
                                if (!accepted) {
                                    logger.info("Skipping because it has not been accepted by constraint {}: {}",
                                            constraint, pkg.getSourceFile().getFullPath());
                                }
                                return accepted;
                            });
                        }
                    })
                    .findFirst().get();
            logger.info("Selected job: {}", videoPackage.getSourceFile().getFullPath());
            videoPackage.setOutputFile(getOutputFile(videoPackage));

            Job job = jobProvider.get();
            job.setNodeID(nodeSettings.getNodeID());
            job.setVideoPackage(videoPackage);
            job.setStartTime(LocalDateTime.now(Clock.systemDefaultZone()));
            queue.add(job);
            jobSettings.setJobs(queue);
            fireStateTrigger(new QueueResultTrigger(job));
        } catch (NoSuchElementException ex) {
            logger.info("No sources found to queue");
            fireStateTrigger(new MessageTrigger("No sources found to queue"));
        }

    }

    private List<Job> cleanUpQueue(List<Job> queues) {
        return queues.stream().filter(queue -> {
            return !nodeSettings.getNodeID().equals(queue.getNodeID());
        }).collect(Collectors.toList());
    }

    private FileContainer getOutputFile(VideoPackage video) {
        String extension = FilenameUtils.getExtension(video.getSourceFile().getFile().getName());
        String fileName = FilenameUtils.removeExtension(video.getSourceFile().getFile().getName());
        File output = video.getSourceFile().getFile();
        if (!extension.equalsIgnoreCase(outputSettings.getDefaultExtension())) {
            String newFileName = fileName + "." + outputSettings.getDefaultExtension();
            if (output.getParentFile() == null) {
                output = new File(newFileName);
            } else {
                output = new File(output.getParentFile(), newFileName);
            }
        }
        return new FileContainer(DirTypes.TMP, output);
    }

    @Override
    protected void exitState() {
    }

}
