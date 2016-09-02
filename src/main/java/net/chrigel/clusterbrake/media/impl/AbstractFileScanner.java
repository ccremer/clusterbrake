package net.chrigel.clusterbrake.media.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @param <T>
 */
public abstract class AbstractFileScanner<T>
        implements FileScanner<T> {

    private File searchDir;
    private boolean isRecursive;
    private List<String> allowedExtensions;

    @Override
    public FileScanner<T> search(File dir) {
        this.searchDir = dir;
        return this;
    }

    protected final File getSearchDir() {
        return searchDir;
    }

    @Override
    public FileScanner<T> recursive(boolean recursive) {
        this.isRecursive = recursive;
        return this;
    }

    protected final boolean isRecursive() {
        return isRecursive;
    }

    @Override
    public FileScanner<T> withFileExtensionFilter(List<String> allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
        return this;
    }

    protected final List<String> getExtensions() {
        return allowedExtensions;
    }

    protected List<File> scanForSameFilesWithDifferentExtensions(
            File fileWithExtension,
            List<String> extensions)
            throws IOException {
        List<File> list = new LinkedList<>();
        String pathNameWithoutExtension = FilenameUtils.removeExtension(fileWithExtension.getName());
        String fileNameWithoutExtension = FilenameUtils.getName(pathNameWithoutExtension);
        try {
            Iterator<String> itr = extensions.iterator();
            while (itr.hasNext()) {
                String extension = itr.next();
                File child = new File(String.format("%1$s.%2$s", fileNameWithoutExtension, extension));
                if (FileUtils.directoryContains(
                        fileWithExtension.getParentFile(),
                        child)) {
                    list.add(new File(fileWithExtension.getParentFile(), child.getName()));
                }
            }
        } catch (IllegalArgumentException ex) {
            throw new IOException(ex);
        }
        return list;
    }

}
