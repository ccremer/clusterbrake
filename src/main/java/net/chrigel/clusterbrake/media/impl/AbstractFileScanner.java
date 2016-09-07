package net.chrigel.clusterbrake.media.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.DirType;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.FileScanner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @param <T>
 */
public abstract class AbstractFileScanner<T>
        implements FileScanner<T> {

    private DirType searchDir;
    private boolean isRecursive;
    private List<String> allowedExtensions;

    @Override
    public FileScanner<T> search(DirType dir) {
        this.searchDir = dir;
        return this;
    }

    protected final DirType getSearchDir() {
        return searchDir;
    }

    @Override
    public FileScanner<T> withRecursion(boolean recursive) {
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

    protected Iterator<File> scanForFiles(File searchDir, List<String> allowedExtensions, boolean isRecursive) {
        return FileUtils.iterateFiles(
                searchDir,
                allowedExtensions.toArray(new String[0]),
                isRecursive);
    }

    protected List<FileContainer> scanForSameFilesWithDifferentExtensions(
            File fileWithExtension,
            List<String> extensions)
            throws IOException {
        List<FileContainer> list = new LinkedList<>();
        String pathNameWithoutExtension = FilenameUtils.removeExtension(fileWithExtension.getName());
        String fileNameWithoutExtension = FilenameUtils.getName(pathNameWithoutExtension);
        try {
            Iterator<String> itr = extensions.iterator();
            while (itr.hasNext()) {
                String extension = itr.next();
                String fileName = String.format("%1$s.%2$s", fileNameWithoutExtension, extension);
                File child = new File(fileWithExtension.getParentFile(), fileName);
                if (child.exists() && child.isFile()) {
                    list.add(new FileContainer(searchDir, child));
                }
            }
        } catch (IllegalArgumentException ex) {
            throw new IOException(ex);
        }
        return list;
    }

}
