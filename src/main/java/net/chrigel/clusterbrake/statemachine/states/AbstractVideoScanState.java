package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Provider;
import java.io.IOException;
import java.util.List;
import net.chrigel.clusterbrake.media.DirType;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.statemachine.StateContext;

/**
 *
 */
public abstract class AbstractVideoScanState
        extends AbstractState {

    private final Provider<FileScanner<Video>> videoScannerProvider;

    public AbstractVideoScanState(
            StateContext context,
            Provider<FileScanner<Video>> videoScannerProvider
    ) {
        super(context);
        this.videoScannerProvider = videoScannerProvider;
    }

    protected List<Video> scanForVideoFiles(
            DirType baseDir, List<String> allowedExtensions, boolean recursive) throws IOException {
        logger.info("Scanning for media files: {}", baseDir);
        return videoScannerProvider.get()
                .search(baseDir)
                .withRecursion(recursive)
                .withFileExtensionFilter(allowedExtensions)
                .scan();
    }
    
}
