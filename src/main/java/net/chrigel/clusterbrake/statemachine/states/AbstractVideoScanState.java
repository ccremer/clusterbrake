package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import net.chrigel.clusterbrake.media.DirType;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.statemachine.StateContext;

/**
 *
 */
public abstract class AbstractVideoScanState
        extends AbstractState {

    private final Provider<FileScanner<VideoPackage>> videoScannerProvider;

    public AbstractVideoScanState(
            StateContext context,
            Provider<FileScanner<VideoPackage>> videoScannerProvider
    ) {
        super(context);
        this.videoScannerProvider = videoScannerProvider;
    }

    protected List<VideoPackage> scanForVideoFiles(
            File searchDir, DirType baseDir, List<String> allowedExtensions, boolean recursive) throws IOException {
        logger.info("Scanning for media files: {}", baseDir);
        return videoScannerProvider.get()
                .search(searchDir)
                .withBaseDirType(baseDir)
                .withRecursion(recursive)
                .withFileExtensionFilter(allowedExtensions)
                .scan();
    }

    protected List<VideoPackage> filterFinishedJobs(List<Job> finishedJobs, List<VideoPackage> videos) {
        return videos.stream().filter(pkg -> {
            return finishedJobs.stream().allMatch(job -> {
                FileContainer source = job.getVideoPackage().getSourceFile();
                boolean found = source.equals(pkg.getSourceFile());
                if (found) {
                    logger.info("Skipping because it is already converted: {}", source.getFullPath());
                }
                return !found;
            });
        }).collect(Collectors.toList());
    }

}
