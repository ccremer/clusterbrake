package net.chrigel.clusterbrake.media;

/**
 *
 */
public interface VideoPackage {

    Video getVideo();

    void setVideo(Video video);
    
    FileContainer getOutputFile();

    void setOutputFile(FileContainer file);

    VideoOptionPackage getSettings();

    void setSettings(VideoOptionPackage settings);
}
