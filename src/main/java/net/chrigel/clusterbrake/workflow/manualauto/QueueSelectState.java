package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import com.google.inject.Provider;
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
import net.chrigel.clusterbrake.workflow.manualauto.settings.QueueSettings;

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

    @Inject
    QueueSelectState(
            StateContext context,
            @QueueSettings JobSettings jobSettings,
            NodeSettings nodeSettings,
            @FinishedJobSettings JobSettings finishedJobs,
            Provider<Job> queueProvider
    ) {
        super(context);
        this.queueSettings = jobSettings;
        this.finishedSettings = finishedJobs;
        this.nodeSettings = nodeSettings;
        this.jobProvider = queueProvider;
    }

    public void setVideoPackageList(List<VideoPackage> list) {
        this.videos = list;
    }

    public void setConstraints(Constraint... constraints) {
        this.constraints = Arrays.asList(constraints);
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
            videoPackage.setOutputFile(new FileContainer(
                    DirTypes.TEMP,
                    videoPackage.getVideo().getSourceFile().getFile()));

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

    @Override
    protected void exitState() {
    }

}
