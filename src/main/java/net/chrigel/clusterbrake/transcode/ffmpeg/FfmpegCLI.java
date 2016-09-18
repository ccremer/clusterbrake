package net.chrigel.clusterbrake.transcode.ffmpeg;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import net.chrigel.clusterbrake.process.ExternalProcess;
import net.chrigel.clusterbrake.transcode.Transcoder;
import net.chrigel.clusterbrake.transcode.TranscoderSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class FfmpegCLI
        implements Transcoder {

    private File source;
    private File output;
    private List<String> arguments;
    private final Logger logger;
    private TranscoderSettings settings;
    private final Provider<ExternalProcess> processProvider;
    private ExternalProcess process;

    @Inject
    FfmpegCLI(
            TranscoderSettings settings,
            Provider<ExternalProcess> processProvider
    ) throws FileNotFoundException {
        if (!new File(settings.getCLIPath()).exists()) {
            throw new FileNotFoundException(settings.getCLIPath());
        }
        this.settings = settings;
        this.logger = LogManager.getLogger(getClass());
        this.processProvider = processProvider;
    }

    @Override
    public Transcoder from(File source) throws FileNotFoundException {
        if (source.exists()) {
            this.source = source;
            return this;
        } else {
            throw new FileNotFoundException(String.format("Source does not exist: %1$s", source.getAbsolutePath()));
        }
    }

    @Override
    public Transcoder to(File output) {
        validateOutput(output);
        this.output = output;
        return this;
    }

    @Override
    public Transcoder withOptions(List<String> options) throws IllegalArgumentException {
        validateArguments(options);
        this.arguments = options;
        return this;
    }

    @Override
    public int transcode() throws InterruptedException, IOException {
        Objects.requireNonNull(source, "Input is not specified");
        Objects.requireNonNull(output, "Output is not specified");
        Objects.requireNonNull(arguments, "Arguments not specified");
        this.process = processProvider.get();
        return process
                .withIORedirected(settings.isIORedirected())
                .withPath(settings.getCLIPath())
                .withArguments(addOptionsToArguments(arguments))
                .start();
    }

    @Override
    public void abort() {
        if (process != null) {
            try {
                process.destroy(1, TimeUnit.MINUTES);
            } catch (InterruptedException ex) {
                logger.warn(ex);
            }
        }
    }

    private void validateArguments(List<String> arguments) {
        logger.debug("Validating arguments...");
        arguments.forEach(arg -> {
            if (arg.startsWith("--help") || arg.equals("-h ") || arg.equals("-?") || arg.equals("-help")) {
                throw new IllegalArgumentException("Help option is specified in arguments");
            }
            if (arg.startsWith("-version")) {
                throw new IllegalArgumentException("Update option is specified in arguments");
            }
        });
    }

    private void validateOutput(File file) {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            throw new IllegalArgumentException("Parent folder of output does not exist.");
        }
    }

    private List<String> addOptionsToArguments(List<String> options) {
        List<String> list = new LinkedList<>();
        options.forEach(arg -> {
            if (arg.contains(" ")) {
                if (arg.contains("${INPUT}")) {
                    arg = arg.replace("${INPUT}", source.getAbsolutePath());
                }
                list.addAll(Arrays.asList(arg.split(" ", 2)));
            } else {
                list.add(arg);
            }
        });

        list.add(output.getAbsolutePath());
        return list;
    }
}
