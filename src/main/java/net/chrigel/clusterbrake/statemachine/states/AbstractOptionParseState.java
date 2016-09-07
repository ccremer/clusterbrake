package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Provider;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.Video;
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
    private final Provider<VideoPackage> videoPackageProvider;

    public AbstractOptionParseState(
            StateContext context,
            Provider<VideoOptionPackage> optionPackageProvider,
            Provider<OptionsFileParser> optionParserProvider,
            Provider<VideoPackage> videoPackageProvider
    ) {
        super(context);
        this.optionPackageProvider = optionPackageProvider;
        this.optionParserProvider = optionParserProvider;
        this.videoPackageProvider = videoPackageProvider;
    }

    protected List<VideoPackage> applyOptionsTemplate(FileContainer template, List<Video> videoList)
            throws IOException, ParseException {

        List<VideoPackage> pkgList = new LinkedList<>();
        logger.info("Parsing {}", template.getFullPath());
        List<String> options = optionParserProvider
                .get()
                .parseFile(template.getFullPath());
        videoList.parallelStream().forEach(video -> {
            VideoPackage videoPkg = videoPackageProvider.get();
            VideoOptionPackage optionPkg = optionPackageProvider.get();
            optionPkg.setOptionFile(template);
            optionPkg.setOptions(options);
            videoPkg.setSettings(optionPkg);
            videoPkg.setVideo(video);
            pkgList.add(videoPkg);
        });
        return pkgList;
    }

}
