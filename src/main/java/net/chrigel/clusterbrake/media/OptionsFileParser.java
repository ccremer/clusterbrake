package net.chrigel.clusterbrake.media;

import java.io.IOException;
import java.text.ParseException;

/**
 *
 */
public interface OptionsFileParser {
    
    VideoOptionPackage parseFile(VideoOptionPackage optionPackage) throws ParseException, IOException;
    
}
