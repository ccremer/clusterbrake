package net.chrigel.clusterbrake.media.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.Video;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class VideoFileScanner
        extends AbstractFileScanner<Video> {

    private final Logger logger;

    public VideoFileScanner() {
        this.logger = LogManager.getLogger(getClass());
    }

    @Override
    public List<Video> scan() throws IOException {
        List<Video> resultList = new LinkedList<>();
        scanForFiles(getSearchDir().getBase(), getExtensions(), isRecursive())
                .forEachRemaining(file -> {
                    logger.debug("Found {}", file);
                    resultList.add(new VideoImpl(new FileContainer(getSearchDir(), file)));
                });
        return resultList;
    }

}
