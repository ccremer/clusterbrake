package net.chrigel.clusterbrake.media.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class OptionFileScanner
        extends AbstractFileScanner<VideoOptionPackage> {

    private final Logger logger;

    public OptionFileScanner() {
        this.logger = LogManager.getLogger(getClass());
    }

    @Override
    public List<VideoOptionPackage> scan() throws IOException {
        List<VideoOptionPackage> resultList = new LinkedList<>();
        scanForFiles(getSearchDir().getBase(), getExtensions(), isRecursive())
                .forEachRemaining(file -> {
                    logger.debug("Found {}", file);
                    resultList.add(new HandbrakeOptionPackage(new FileContainer(getSearchDir(), file)));
                });
        return resultList;
    }

}
