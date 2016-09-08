package net.chrigel.clusterbrake.transcode.handbrake;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import org.apache.commons.io.FileUtils;

/**
 *
 */
class HandbrakeOptionParser
        implements OptionsFileParser {

    @Override
    public List<String> parseFile(File file)
            throws ParseException, IOException {

        List<String> lines = FileUtils.readLines(file, "UTF-8");
        return lines.parallelStream().map(line -> {
            return line.trim();
        }).filter(line -> {
            return !line.startsWith("#") && !"".equals(line);
        }).collect(Collectors.toList());
    }

}
