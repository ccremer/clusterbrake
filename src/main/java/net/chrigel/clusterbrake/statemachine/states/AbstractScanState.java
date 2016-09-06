package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.statemachine.StateContext;

/**
 *
 */
public abstract class AbstractScanState
        extends AbstractState {

    private final Provider<FileScanner<Video>> videoScannerProvider;

    public AbstractScanState(
            StateContext context,
            Provider<FileScanner<Video>> videoScannerProvider
    ) {
        super(context);
        this.videoScannerProvider = videoScannerProvider;
    }

    protected List<Video> scanForVideoFiles(
            File baseDir, List<String> allowedExtensions, boolean recursive) throws IOException {
        logger.info("Scanning for media files: {}", baseDir);
        return videoScannerProvider.get()
                .search(baseDir)
                .withRecursion(recursive)
                .withFileExtensionFilter(allowedExtensions)
                .scan();
    }

}
