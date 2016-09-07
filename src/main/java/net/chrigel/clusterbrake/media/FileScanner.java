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
    
    FileScanner<T> withBaseDirType(DirType dirType);

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

}
