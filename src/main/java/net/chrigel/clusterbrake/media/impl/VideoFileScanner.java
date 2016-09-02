package net.chrigel.clusterbrake.media.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.Video;
import org.apache.commons.io.FileUtils;

/**
 *
 */
class VideoFileScanner
        extends AbstractFileScanner<Video> {

    @Override
    public List<Video> scan() throws IOException {
        List<Video> resultList = new LinkedList<>();
        FileUtils.iterateFiles(
                getSearchDir(),
                getExtensions().toArray(new String[0]),
                isRecursive())
                .forEachRemaining(file -> {
                    resultList.add(new VideoImpl(file));
                });
        return resultList;
    }

    @Override
    public List<Video> scanForSameFilenamesButDifferentExtensions() throws IOException {
        List<Video> resultList = new LinkedList<>();

        scanForSameFilesWithDifferentExtensions(getSearchDir(), getExtensions()).forEach(file -> {
            resultList.add(new VideoImpl(file));
        });
        return resultList;
    }

}
