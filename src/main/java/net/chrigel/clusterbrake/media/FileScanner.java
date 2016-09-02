package net.chrigel.clusterbrake.media;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @param <T>
 */
public interface FileScanner<T> {

    FileScanner<T> search(File dirOrFile);

    FileScanner<T> withRecursion(boolean recursive);

    FileScanner<T> withFileExtensionFilter(List<String> allowedExtensions);

    /**
     * Scans for the files with the allowed extensions in the specified directory. Use
     * {@link #scanForSameFilenamesButDifferentExtensions()} if the directory is a file.
     *
     * @return a list of files
     * @throws IOException if the scan could not be executed.
     */
    List<T> scan() throws IOException;

    /**
     * Lists all files which have the same name as the specified file in {@link #search(java.io.File)} but the
     * extensions provided in {@link #withFileExtensionFilter(java.util.List)}. The recursive option does not apply in
     * this method.
     *
     * @return a list of files with different extensions.
     * @throws IOException if the scan could not be executed.
     */
    List<T> scanForSameFilenamesButDifferentExtensions() throws IOException;
}
