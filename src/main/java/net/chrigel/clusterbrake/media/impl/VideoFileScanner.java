package net.chrigel.clusterbrake.media.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.VideoPackage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class VideoFileScanner
        extends AbstractFileScanner<VideoPackage> {

    private final Logger logger;
    private final Provider<VideoPackage> videoPackageProvider;

    @Inject
    public VideoFileScanner(
            Provider<VideoPackage> videoPackageProvider
    ) {
        this.logger = LogManager.getLogger(getClass());
        this.videoPackageProvider = videoPackageProvider;
    }

    @Override
    public List<VideoPackage> scan() throws IOException {
        List<VideoPackage> resultList = new LinkedList<>();
        scanForFiles(getSearchDir(), getExtensions(), isRecursive())
                .forEachRemaining(file -> {
                    logger.debug("Found {}", file);
                    Path full = file.toPath();
                    Path base = getDirType().getBase().toPath();
                    Path relative = base.relativize(full);
                    VideoPackage pkg = videoPackageProvider.get();
                    pkg.setSourceFile(new FileContainer(getDirType(), relative.toFile()));
                    resultList.add(pkg);
                });

        return resultList;
    }

}
