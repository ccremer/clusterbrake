package net.chrigel.clusterbrake.media.impl;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import net.chrigel.clusterbrake.media.DirType;
import net.chrigel.clusterbrake.media.FileScanner;
import org.apache.commons.io.FileUtils;

/**
 *
 * @param <T>
 */
public abstract class AbstractFileScanner<T>
        implements FileScanner<T> {

    private File searchDir;
    private boolean isRecursive;
    private List<String> allowedExtensions;
    private DirType dirType;

    @Override
    public FileScanner<T> search(File dir) {
        this.searchDir = dir;
        return this;
    }

    protected final File getSearchDir() {
        return searchDir;
    }

    @Override
    public FileScanner<T> withBaseDirType(DirType dirType) {
        this.dirType = dirType;
        return this;
    }

    protected final DirType getDirType() {
        return dirType;
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

}
