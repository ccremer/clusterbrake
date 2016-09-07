package net.chrigel.clusterbrake.settings.constraint;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.io.File;
import net.chrigel.clusterbrake.media.Video;

/**
 *
 */
public class FileSizeConstraint
        implements Constraint {

    private final long minSize;
    private final long maxSize;

    public static long MEBI_BYTES = 1048576;
    public static long KIBI_BYTES = 1024;
    public static long BYTES = 1;
    
    @Inject
    public FileSizeConstraint(
            @Named("node.constraint.file.minSize") long minSize,
            @Named("node.constraint.file.maxSize") long maxSize
    ) {
        this(minSize, maxSize, MEBI_BYTES);
    }

    public FileSizeConstraint(
            long minSize,
            long maxSize,
            long factor) {
        this.minSize = minSize * factor;
        this.maxSize = maxSize * factor;
    }

    @Override
    public boolean accept(Video video) {
        if (minSize <= 0 && maxSize <= 0) {
            return true;
        }

        File file = video.getSourceFile().getFullPath();
        long size = file.length();
        if (minSize > 0 && maxSize <= 0) {
            return size >= minSize;
        } else if (minSize <= 0 && maxSize > 0) {
            return size <= maxSize;
        } else {
            return size >= minSize && size <= maxSize;
        }
    }

}
