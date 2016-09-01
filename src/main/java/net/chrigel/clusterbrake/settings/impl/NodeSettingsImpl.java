package net.chrigel.clusterbrake.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.chrigel.clusterbrake.settings.NodeSettings;

/**
 *
 */
public class NodeSettingsImpl
        implements NodeSettings {

    private String nodeID;
    private final long maxFileSizeContraint;
    private final long minFileSizeContraint;

    @Inject
    NodeSettingsImpl(
            @Named("node.id") String nodeID,
            @Named("node.constraint.maxFileSize") long maxFileSizeContraint,
            @Named("node.constraint.minFileSize") long minFileSizeContraint
    ) {
        this.nodeID = nodeID;
        this.maxFileSizeContraint = maxFileSizeContraint;
        this.minFileSizeContraint = minFileSizeContraint;
    }

    @Override
    public String getNodeID() {
        return nodeID;
    }

    @Override
    public void setNodeID(String id) {
        this.nodeID = id;
    }

    @Override
    public long getMaxFileSize() {
        return maxFileSizeContraint;
    }

    @Override
    public long getMinFileSize() {
        return minFileSizeContraint;
    }

}
