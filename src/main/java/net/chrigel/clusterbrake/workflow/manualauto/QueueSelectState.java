package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.settings.DirectorySettings;
import net.chrigel.clusterbrake.settings.NodeSettings;
import net.chrigel.clusterbrake.settings.constraint.Constraint;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;
import net.chrigel.clusterbrake.statemachine.trigger.MessageTrigger;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.statemachine.trigger.QueueResultTrigger;
import net.chrigel.clusterbrake.settings.JobSettings;

/**
 *
 */
public class QueueSelectState
        extends AbstractState {

    private List<VideoPackage> videos;
    private List<Constraint> constraints;
    private final DirectorySettings dirSettings;
    private final JobSettings queueSettings;
    private final NodeSettings nodeSettings;
    private final Provider<Job> queueProvider;

    @Inject
    QueueSelectState(
            StateContext context,
            DirectorySettings dirSettings,
            JobSettings queueSettings,
            NodeSettings nodeSettings,
            Provider<Job> queueProvider
    ) {
        super(context);
        this.dirSettings = dirSettings;
        this.queueSettings = queueSettings;
        this.queueSettings.setSettingsFile(new File(dirSettings.getConfigBaseDir(), "queue.json"));
        this.nodeSettings = nodeSettings;
        this.queueProvider = queueProvider;
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
            VideoPackage videoPackage = videos.stream()
                    .filter(pkg -> {
                        return queue.stream().anyMatch(job -> {
                            return !job.getVideoPackage().getVideo().getSourceFile()
                                    .equals(pkg.getVideo().getSourceFile());
                        });
                    })
                    .filter(pkg -> {
                        return constraints.stream().allMatch(constraint -> {
                            return constraint.accept(pkg.getVideo());
                        });
                    })
                    .findFirst().get();
            videoPackage.setOutputFile(new File(
                    dirSettings.getOutputBaseDir(),
                    videoPackage.getVideo().getSourceFile().getPath()));

            Job job = queueProvider.get();
            job.setNodeID(nodeSettings.getNodeID());
            job.setVideoPackage(videoPackage);
            queue.add(job);
            queueSettings.setJobs(queue);
            fireStateTrigger(new QueueResultTrigger(job));
        } catch (NoSuchElementException ex) {
            logger.info("No sources found to queue", ex);
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
