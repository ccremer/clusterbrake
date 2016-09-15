package net.chrigel.clusterbrake.transcode;

import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.VideoOptionPackage;

/**
 *
 */
public class SimpleOptionPackage
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
