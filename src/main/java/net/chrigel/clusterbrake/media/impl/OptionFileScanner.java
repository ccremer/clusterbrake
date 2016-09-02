package net.chrigel.clusterbrake.media.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import org.apache.commons.io.FileUtils;

/**
 *
 */
class OptionFileScanner
        extends AbstractFileScanner<VideoOptionPackage> {

    @Override
    public List<VideoOptionPackage> scan() throws IOException {
        List<VideoOptionPackage> resultList = new LinkedList<>();
        FileUtils.iterateFiles(
                getSearchDir(),
                getExtensions().toArray(new String[0]),
                isRecursive())
                .forEachRemaining(file -> {
                    resultList.add(new HandbrakeOptionPackage(file));
                });
        return resultList;
    }

    @Override
    public List<VideoOptionPackage> scanForSameFilenamesButDifferentExtensions() throws IOException {
        List<VideoOptionPackage> resultList = new LinkedList<>();

        scanForSameFilesWithDifferentExtensions(getSearchDir(), getExtensions()).forEach(file -> {
            resultList.add(new HandbrakeOptionPackage(file));
        });
        return resultList;
    }

}
