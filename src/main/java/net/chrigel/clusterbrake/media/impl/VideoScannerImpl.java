package net.chrigel.clusterbrake.media.impl;

import com.google.inject.Inject;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.media.VideoScanner;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;

/**
 *
 */
public class VideoScannerImpl
        implements VideoScanner {

    private File searchDir;
    private boolean isRecursive;
    private List<String> allowedExtensions;

    @Inject
    VideoScannerImpl() {
        this.allowedExtensions = new LinkedList<>();
        this.allowedExtensions.add("mp4");
        this.allowedExtensions.add("mkv");
    }

    @Override
    public VideoScanner searchIn(File dir) {
        this.searchDir = dir;
        return this;
    }

    @Override
    public VideoScanner recursive(boolean recursive) {
        this.isRecursive = recursive;
        return this;
    }

    @Override
    public VideoScanner withFileExtensionFilter(List<String> allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
        return this;
    }

    @Override
    public List<Video> scan() throws IOException {

        List<Video> resultList = new LinkedList<>();
        FileFilter filter = new SuffixFileFilter(allowedExtensions, IOCase.INSENSITIVE);
        Arrays.asList(searchDir.listFiles(filter)).forEach(result -> {
            resultList.add(new VideoImpl(result));
        });

        return resultList;
    }

}
