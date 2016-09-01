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

    private final Provider<VideoPackage> videoPackageProvider;
    private final Provider<Transcoder> transcoderProvider;
    private Transcoder currentTranscoder;

    @Inject
    TranscodingState(
            StateContext context,
            Provider<VideoPackage> videoContextProvider,
            Provider<Transcoder> transcoderProvider
    ) {
        super(context);
        this.videoPackageProvider = videoContextProvider;
        this.transcoderProvider = transcoderProvider;
    }

    @Override
    protected void enterState() {
        try {
            VideoPackage container = videoPackageProvider.get();

            int returnValue = transcoderProvider.get()
                    .from(container.getVideo().getSourceFile())
                    .to(container.getOutputFile())
                    .withOptions(container.getSettings().getOptions())
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
