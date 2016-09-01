package net.chrigel.clusterbrake.media;

import java.io.File;
import java.util.List;

/**
 *
 */
public interface Video {
    
    File getSourceFile();
    
    
    List<File> getSupplementalFiles();
    
}
