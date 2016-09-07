package net.chrigel.clusterbrake.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.chrigel.clusterbrake.settings.NodeSettings;

/**
 *
 */
public class NodeSettingsImpl
        implements NodeSettings {

    private final String nodeID;

    @Inject
    NodeSettingsImpl(
            @Named("node.id") String nodeID
    ) {
        this.nodeID = nodeID;
    }

    @Override
    public String getNodeID() {
        return nodeID;
    }
}
