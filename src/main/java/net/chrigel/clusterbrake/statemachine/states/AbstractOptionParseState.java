package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Provider;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.statemachine.StateContext;

/**
 *
 */
public abstract class AbstractOptionParseState
        extends AbstractState {

    private final Provider<VideoOptionPackage> optionPackageProvider;
    private final Provider<OptionsFileParser> optionParserProvider;

    public AbstractOptionParseState(
            StateContext context,
            Provider<VideoOptionPackage> optionPackageProvider,
            Provider<OptionsFileParser> optionParserProvider
    ) {
        super(context);
        this.optionPackageProvider = optionPackageProvider;
        this.optionParserProvider = optionParserProvider;
    }

    protected List<VideoPackage> applyOptionsTemplate(FileContainer template, List<VideoPackage> videoList)
            throws IOException, ParseException {

        logger.info("Parsing {}", template.getFullPath());
        List<String> options = optionParserProvider
                .get()
                .parseFile(template.getFullPath());
        videoList.parallelStream().forEach(video -> {
            VideoOptionPackage optionPkg = optionPackageProvider.get();
            optionPkg.setOptions(options);
            video.setSettings(optionPkg);
        });
        return videoList;
    }

}
