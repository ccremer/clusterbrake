package net.chrigel.clusterbrake.transcode.handbrake;

import com.google.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import net.chrigel.clusterbrake.transcode.Transcoder;
import net.chrigel.clusterbrake.transcode.TranscoderSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class HandbrakeCli
        implements Transcoder {

    private File source;
    private File output;
    private List<String> arguments;
    private final Logger logger;
    private TranscoderSettings settings;
    private Process process;

    @Inject
    HandbrakeCli(TranscoderSettings settings) throws FileNotFoundException {
        if (!new File(settings.getCLIPath()).exists()) {
            throw new FileNotFoundException(settings.getCLIPath());
        }
        this.settings = settings;
        this.logger = LogManager.getLogger(getClass());
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
        this.arguments = addOptionsToArguments(arguments);
        ProcessBuilder processBuilder = new ProcessBuilder(arguments);
        if (settings.isIORedirected()) {
            processBuilder.inheritIO();
        } else {
            // This is necessary. Otherwise waitFor() will be deadlocked even if transcoding finished hours ago.
            processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(new File("NUL:")));
            processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(new File("NUL:")));
        }
        logger.info("Invoking Handbrake: {}", parseArguments(arguments));
        this.process = processBuilder.start();
        return this.process.waitFor();
    }

    @Override
    public void abort() {
        if (process != null) {
            try {
                process.destroyForcibly();
                process.waitFor(1, TimeUnit.MINUTES);
            } catch (InterruptedException ex) {
                logger.warn("Could not destroy child process: {}", ex);
            }
        }
    }

    private void validateArguments(List<String> arguments) {
        logger.debug("Validating arguments...");
        arguments.forEach(arg -> {
            if (arg.startsWith("--input") || arg.startsWith("-i")) {
                throw new IllegalArgumentException("Input is specified in arguments");
            }
            if (arg.startsWith("--output") || arg.startsWith("-o")) {
                throw new IllegalArgumentException("Output is specified in arguments");
            }
            if (arg.startsWith("--help") || arg.startsWith("-h")) {
                throw new IllegalArgumentException("Help option is specified in arguments");
            }
            if (arg.startsWith("--update") || arg.startsWith("-u")) {
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
        list.add(settings.getCLIPath());
        if (options != null) {
            options.forEach(arg -> {
                if (arg.contains(" ")) {
                    list.addAll(Arrays.asList(arg.split(" ", 2)));
                } else {
                    list.add(arg);
                }
            });
        }
        list.add("--input");
        list.add(String.format("\"%1$s\"", source.getAbsolutePath()));
        list.add("--output");
        list.add(String.format("\"%1$s\"", output.getAbsolutePath()));
        return list;
    }

    private String parseArguments(List<String> arguments) {
        StringBuilder builder = new StringBuilder();
        arguments.forEach(arg -> {
            builder.append(arg);
            builder.append(' ');
        });
        return builder.toString();
    }

}
