package net.chrigel.clusterbrake.process.impl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.chrigel.clusterbrake.process.ExternalProcess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class ExternalProcessImpl
        implements ExternalProcess {

    private boolean redirectIO;
    private List<String> arguments;
    private String path;
    private final Logger logger;
    private Process process;

    public ExternalProcessImpl() {
        this.logger = LogManager.getLogger(getClass());
    }

    @Override
    public ExternalProcess withIORedirected(boolean redirectIO) {
        this.redirectIO = redirectIO;
        return this;
    }

    @Override
    public ExternalProcess withArguments(List<String> arguments) {
        this.arguments = arguments;
        return this;
    }

    @Override
    public ExternalProcess withPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public int start() throws InterruptedException, IOException {
        this.arguments = addOptionsToArguments(arguments);
        ProcessBuilder processBuilder = new ProcessBuilder(arguments);
        if (redirectIO) {
            processBuilder.inheritIO();
        } else {
            // This is necessary. Otherwise waitFor() will be deadlocked even if transcoding finished hours ago.
            processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(new File("NUL:")));
            processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(new File("NUL:")));
        }
        processBuilder.directory(new File("").getAbsoluteFile());
        logger.info("Invoking Program: {}", parseArguments(arguments));
        this.process = processBuilder.start();
        return this.process.waitFor();
    }

    @Override
    public void destroy(long timeout, TimeUnit unit) throws InterruptedException {
        if (process != null) {
            process.destroyForcibly();
            process.waitFor(timeout, unit);
        }
    }

    private List<String> addOptionsToArguments(List<String> options) {
        List<String> list = new LinkedList<>();
        if (path != null && !"".equals(path)) {
            list.add(path);
        }
        if (options != null) {
            list.addAll(options);
        }
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
