package net.chrigel.clusterbrake.transcode.handbrake;

import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.VideoOptionPackage;

/**
 *
 */
class HandbrakeOptionPackage
        implements VideoOptionPackage {

    private List<String> options = new LinkedList<>();

    @Override
    public List<String> getOptions() {
        return options;
    }

    @Override
    public void setOptions(List<String> options) {
        this.options = options;
    }

}
