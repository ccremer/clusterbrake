package net.chrigel.clusterbrake.transcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface Transcoder {

    /**
     * Sets the source file.
     *
     * @param source the original source file with extension.
     * @return this.
     * @throws FileNotFoundException if source does not exist.
     */
    Transcoder from(File source) throws FileNotFoundException;

    /**
     * Sets the output file.
     *
     * @param output the output file with extension.
     * @return this.
     */
    Transcoder to(File output);

    /**
     * Sets the encoder options.
     *
     * @param arguments the options or arguments used by the encoder. Each entry may be a pair of 2 strings or a single
     * string.
     * @return this.
     * @throws IllegalArgumentException if the list contains illegal options for the encoder (like a help flag).
     */
    Transcoder withOptions(List<String> arguments) throws IllegalArgumentException;

    /**
     * Launches the encoding process and waits for its termination.
     *
     * @return the exit code of the encoder.
     * @throws InterruptedException if the thread waiting for the process has been interrupted.
     * @throws IOException if the process could not start.
     * @throws NullPointerException if source or output are not specified.
     */
    int transcode() throws InterruptedException, IOException;

    /**
     * Terminates the encoding process. It waits for a maximum of 1 Minute or returns immediately after termination.
     */
    void abort();
}
