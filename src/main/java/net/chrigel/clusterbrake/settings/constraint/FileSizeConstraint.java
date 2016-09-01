package net.chrigel.clusterbrake.settings.constraint;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.chrigel.clusterbrake.media.Video;

/**
 *
 */
public class FileSizeConstraint
        implements Constraint {

    @Inject
    FileSizeConstraint(
            @Named("node.constraint.file.minSize") long minSize,
            @Named("node.constraint.file.maxSize") long maxSize
    ) {
    }

    @Override
    public boolean accept(Video video) {
        return true;
    }

}
