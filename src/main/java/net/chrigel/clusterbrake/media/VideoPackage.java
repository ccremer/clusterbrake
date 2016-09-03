package net.chrigel.clusterbrake.media;

import java.io.File;

/**
 *
 */
public interface VideoPackage {

    Video getVideo();

    void setVideo(Video video);
    
    File getOutputFile();

    void setOutputFile(File file);

    VideoOptionPackage getSettings();

    void setSettings(VideoOptionPackage settings);
}
