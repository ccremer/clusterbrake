package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.Video;
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
    private final JobSettings queueSettings;
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
        this.queueSettings = jobSettings;
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
        List<Job> queue = queueSettings.getJobs().stream().filter(job -> {
            return !job.getNodeID().equals(nodeSettings.getNodeID());
        }).collect(Collectors.toList());
        queueSettings.setJobs(queue);
    }
    
    @Override
    protected void enterState() {

        try {
            List<Job> queue = cleanUpQueue(queueSettings.getJobs());
            List<Job> finished = finishedSettings.getJobs();

            VideoPackage videoPackage = videos.stream()
                    .filter(pkg -> {
                        return queue.stream().allMatch(job -> {
                            FileContainer source = job.getVideoPackage().getVideo().getSourceFile();
                            boolean found = source.equals(pkg.getVideo().getSourceFile());
                            if (found) {
                                logger.info("Skipping because {} is already encoding it: {}",
                                        job.getNodeID(), source.getFullPath());
                            }
                            return !found;
                        });
                    })
                    .filter(pkg -> {
                        return finished.stream().allMatch(job -> {
                            FileContainer source = job.getVideoPackage().getVideo().getSourceFile();
                            boolean found = source.equals(pkg.getVideo().getSourceFile());
                            if (found) {
                                logger.debug("Skipping because it is already converted: {}", source.getFullPath());
                            }
                            return !found;
                        });
                    })
                    .filter(pkg -> {
                        return constraints.stream().allMatch(constraint -> {
                            boolean accepted = constraint.accept(pkg.getVideo());
                            if (!accepted) {
                                logger.info("Skipping because it has not been accepted by constraint {}: {}",
                                        constraint, pkg.getVideo().getSourceFile().getFullPath());
                            }
                            return accepted;
                        });
                    })
                    .findFirst().get();
            videoPackage.setOutputFile(getOutputFile(videoPackage.getVideo()));

            Job job = jobProvider.get();
            job.setNodeID(nodeSettings.getNodeID());
            job.setVideoPackage(videoPackage);
            queue.add(job);
            queueSettings.setJobs(queue);
            fireStateTrigger(new QueueResultTrigger(job));
        } catch (NoSuchElementException ex) {
            logger.info("No sources found to queue");
            fireStateTrigger(new MessageTrigger("No sources found to queue"));
        }

    }

    private List<Job> cleanUpQueue(List<Job> queues) {
        return queues.stream().filter(queue -> {
            return !queue.getNodeID().equals(nodeSettings.getNodeID());
        }).collect(Collectors.toList());
    }

    private FileContainer getOutputFile(Video video) {
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
