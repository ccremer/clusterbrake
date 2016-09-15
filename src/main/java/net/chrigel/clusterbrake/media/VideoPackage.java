package net.chrigel.clusterbrake.media;

/**
 *
 */
public interface VideoPackage {

    FileContainer getSourceFile();
    
    void setSourceFile(FileContainer source);

    FileContainer getOutputFile();

    void setOutputFile(FileContainer file);

    VideoOptionPackage getSettings();

    void setSettings(VideoOptionPackage settings);
}
