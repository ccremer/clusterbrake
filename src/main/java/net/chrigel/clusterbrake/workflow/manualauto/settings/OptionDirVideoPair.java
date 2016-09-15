package net.chrigel.clusterbrake.workflow.manualauto.settings;

import java.io.File;
import java.util.List;
import net.chrigel.clusterbrake.media.VideoPackage;

/**
 *
 */
public class OptionDirVideoPair {

    private final File optionDir;
    private final List<VideoPackage> list;

    public OptionDirVideoPair(File optionDir, List<VideoPackage> list) {
        this.optionDir = optionDir;
        this.list = list;
    }

    public File getOptionDir() {
        return optionDir;
    }

    public List<VideoPackage> getVideoList() {
        return list;
    }

}
