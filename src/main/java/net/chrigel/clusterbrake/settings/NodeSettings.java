package net.chrigel.clusterbrake.settings;

/**
 *
 */
public interface NodeSettings {

    String getNodeID();

    void setNodeID(String id);

    long getMaxFileSize();

    long getMinFileSize();
}
