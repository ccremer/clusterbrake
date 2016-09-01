package net.chrigel.clusterbrake.media;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface VideoScanner {

    VideoScanner searchIn(File dir);
    
    VideoScanner recursive(boolean recursive);
    
    VideoScanner withFileExtensionFilter(List<String> allowedExtensions);
    
    List<Video> scan() throws IOException;
    
}
