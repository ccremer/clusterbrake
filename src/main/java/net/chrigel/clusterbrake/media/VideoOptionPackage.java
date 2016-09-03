package net.chrigel.clusterbrake.media;

import java.io.File;
import java.util.List;

/**
 *
 */
public interface VideoOptionPackage {

    List<String> getOptions();

    void setOptions(List<String> options);

    File getOptionFile();
    
    void setOptionFile(File file);

}
