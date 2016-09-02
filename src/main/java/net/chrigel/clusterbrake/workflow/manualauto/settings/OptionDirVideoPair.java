package net.chrigel.clusterbrake.workflow.manualauto.settings;

import java.io.File;
import java.util.List;
import net.chrigel.clusterbrake.media.Video;

/**
 *
 */
public class OptionDirVideoPair {

    private final File optionDir;
    private final List<Video> list;

    public OptionDirVideoPair(File optionDir, List<Video> list) {
        this.optionDir = optionDir;
        this.list = list;
    }

    public File getOptionDir() {
        return optionDir;
    }

    public List<Video> getVideoList() {
        return list;
    }

}
