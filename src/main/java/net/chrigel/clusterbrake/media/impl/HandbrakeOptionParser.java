package net.chrigel.clusterbrake.media.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import org.apache.commons.io.FileUtils;

/**
 *
 */
class HandbrakeOptionParser
        implements OptionsFileParser {

    @Override
    public VideoOptionPackage parseFile(VideoOptionPackage optionPackage)
            throws ParseException, IOException {

        List<String> lines = FileUtils.readLines(optionPackage.getOptionFile(), "UTF-8");
        List<String> options = lines.parallelStream().map(line -> {
            return line.trim();
        }).filter(line -> {
            return !line.startsWith("#");
        }).collect(Collectors.toList());

        optionPackage.setOptions(options);
        
        return optionPackage;
    }

}
