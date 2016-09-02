package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.IOException;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.statemachine.trigger.TranscodingFinishedTrigger;
import net.chrigel.clusterbrake.transcode.Transcoder;

/**
 *
 */
public class TranscodingState
        extends AbstractState {

    private final Provider<Transcoder> transcoderProvider;
    private Transcoder currentTranscoder;
    private VideoPackage videoPackage;

    @Inject
    public TranscodingState(
            StateContext context,
            Provider<Transcoder> transcoderProvider
    ) {
        super(context);
        this.transcoderProvider = transcoderProvider;
    }

    public void setVideoPackage(VideoPackage videoPackage) {
        this.videoPackage = videoPackage;
    }
    
    @Override
    protected void enterState() {
        try {
            int returnValue = transcoderProvider.get()
                    .from(videoPackage.getVideo().getSourceFile())
                    .to(videoPackage.getOutputFile())
                    .withOptions(videoPackage.getSettings().getOptions())
                    .transcode();

            if (returnValue == 0) {
                logger.info("Transcoding finished.");
                fireStateTrigger(new TranscodingFinishedTrigger());
            } else {
                logger.error("Transcoder exited with {}", returnValue);
            }

        } catch (InterruptedException | IOException ex) {
            logger.error("Could not transcode file: {}", ex);
        }
// read config files from dir.stage.queue and
// parse options file from templates and transform to command
// invoke handbrake cli, wait till finish

    }

    @Override
    protected void exitState() {
        if (isActive()) {
            currentTranscoder.abort();
        }

    }

}
