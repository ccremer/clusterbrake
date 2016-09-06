package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.TranscodingFinishedTrigger;
import net.chrigel.clusterbrake.transcode.Transcoder;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.statemachine.trigger.ErrorTrigger;

/**
 *
 */
public class TranscodingState
        extends AbstractState {

    private final Provider<Transcoder> transcoderProvider;
    private Transcoder currentTranscoder;
    private Job job;

    @Inject
    public TranscodingState(
            StateContext context,
            Provider<Transcoder> transcoderProvider
    ) {
        super(context);
        this.transcoderProvider = transcoderProvider;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    protected void enterState() {
        try {
            job.setStartTime(LocalDateTime.now(Clock.systemDefaultZone()));
            int returnValue = transcoderProvider.get()
                    .from(job.getVideoPackage().getVideo().getSourceFile())
                    .to(job.getVideoPackage().getOutputFile())
                    .withOptions(job.getVideoPackage().getSettings().getOptions())
                    .transcode();

            if (returnValue == 0) {
                logger.info("Transcoding finished.");
                job.setFinishTime(LocalDateTime.now(Clock.systemDefaultZone()));
                fireStateTrigger(new TranscodingFinishedTrigger(job));
            } else {
                logger.error("Transcoder exited with code {}", returnValue);
                fireStateTrigger(new ErrorTrigger("Transcoder exited with code " + returnValue));
            }

        } catch (InterruptedException | IOException ex) {
            logger.error("Could not transcode file: {}", ex);
            fireStateTrigger(new ErrorTrigger(ex));
        }
    }

    @Override
    protected void exitState() {
        if (isActive()) {
            currentTranscoder.abort();
        }

    }

}
