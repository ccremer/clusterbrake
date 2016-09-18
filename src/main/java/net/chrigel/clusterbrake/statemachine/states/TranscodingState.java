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
import net.chrigel.clusterbrake.statemachine.trigger.ExceptionTrigger;

/**
 *
 */
public class TranscodingState
        extends AbstractState {

    private final Provider<Transcoder> transcoderProvider;
    private Transcoder currentTranscoder;
    private Job job;

    @Inject
    TranscodingState(
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
            job.getVideoPackage().getOutputFile().getFullPath().getParentFile().mkdirs();
            currentTranscoder = transcoderProvider.get();
            logger.info("Beginning transcode...");
            int returnValue = currentTranscoder
                    .from(job.getVideoPackage().getSourceFile().getFullPath())
                    .to(job.getVideoPackage().getOutputFile().getFullPath())
                    .withOptions(job.getVideoPackage().getSettings().getOptions())
                    .transcode();

            if (returnValue == 0) {
                logger.info("Transcoding finished.");
                job.setFinishTime(LocalDateTime.now(Clock.systemDefaultZone()));
                fireStateTrigger(new TranscodingFinishedTrigger(job));
            } else {
                fireStateTrigger(new ExceptionTrigger("Transcoder exited with code " + returnValue));
            }

        } catch (InterruptedException | IOException ex) {
            fireStateTrigger(new ExceptionTrigger("Could not transcode file.", ex));
        }
    }

    @Override
    protected void exitState() {
        if (isActive()) {
            currentTranscoder.abort();
        }

    }

}
