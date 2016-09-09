package net.chrigel.clusterbrake.transcode;

/**
 *
 */
public interface TranscoderSettings {

    String getCLIPath();

    boolean isIORedirected();
    
    String getOptionsFileExtension();

}
