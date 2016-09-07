package net.chrigel.clusterbrake.media;

import java.util.List;

/**
 *
 */
public interface VideoOptionPackage {

    List<String> getOptions();

    void setOptions(List<String> options);

    FileContainer getOptionFile();
    
    void setOptionFile(FileContainer file);

}
