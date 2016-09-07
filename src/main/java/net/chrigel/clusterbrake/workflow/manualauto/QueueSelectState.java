package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
            JobSettings jobSettings,
            NodeSettings nodeSettings,
            JobSettings finishedJobs,
            Provider<Job> queueProvider
    ) {
        super(context);
        this.queueSettings = jobSettings;
        this.finishedSettings = finishedJobs;
        this.queueSettings.setSettingsFile(new File(DirTypes.CONFIG.getBase(), "queue.json"));
        this.finishedSettings.setSettingsFile(new File(DirTypes.CONFIG.getBase(), "finished.json"));
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

            Stream<VideoPackage> stream1 = videos.stream()
                    .filter(pkg -> {
                        return queue.stream().noneMatch(job -> {
                            return !job.getVideoPackage().getVideo().getSourceFile()
                                    .equals(pkg.getVideo().getSourceFile());
                        });
                    });
            Stream<VideoPackage> stream2 = stream1.filter(pkg -> {
                return finished.stream().noneMatch(job -> {
                    return job.getVideoPackage().getVideo().getSourceFile()
                            .equals(pkg.getVideo().getSourceFile());
                });
            });
            Stream<VideoPackage> stream3 = stream2.filter(pkg -> {
                return constraints.stream().allMatch(constraint -> {
                    return constraint.accept(pkg.getVideo());
                });
            });

            VideoPackage videoPackage = stream3.findFirst().get();
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
