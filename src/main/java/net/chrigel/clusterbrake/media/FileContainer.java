package net.chrigel.clusterbrake.media;

import java.io.File;
import java.util.Objects;

/**
 *
 */
public class FileContainer {

    private DirType type;
    private File path;

    FileContainer() {
    }

    public FileContainer(DirType type, String fileName) {
        this(type, new File(fileName));
    }

    public FileContainer(DirType type, File file) {
        this.type = type;
        this.path = file;
    }

    public DirType getType() {
        return type;
    }

    public File getFile() {
        return path;
    }

    public void setDirType(DirType type) {
        this.type = type;
    }

    public File getFullPath() {
        return new File(type.getBase(), path.getPath());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.type);
        hash = 89 * hash + Objects.hashCode(this.path);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileContainer other = (FileContainer) obj;
        if (!Objects.equals(this.type.getBase(), other.type.getBase())) {
            return false;
        }
        return Objects.equals(this.path, other.path);
    }
}
