package net.chrigel.clusterbrake.media;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 *
 */
public interface OptionsFileParser {
    
    List<String> parseFile(File file) throws ParseException, IOException;
    
}
