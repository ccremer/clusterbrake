package net.chrigel.clusterbrake.media;

import java.io.File;

/**
 *
 */
public interface VideoPackage {

    Video getVideo();

    File getConfigFile();

    File getOutputFile();

    VideoOptionPackage getSettings();
}
