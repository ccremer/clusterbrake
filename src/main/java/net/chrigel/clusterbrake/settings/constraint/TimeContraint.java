package net.chrigel.clusterbrake.settings.constraint;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.chrigel.clusterbrake.media.Video;

/**
 * Not realized yet.
 */
public class TimeContraint
        implements Constraint {

    @Inject
    TimeContraint(
            @Named("node.constraint.time.begin") String begin,
            @Named("node.constraint.time.stop") String stop,
            @Named("node.constraint.time.forceStop") boolean forceStop) {
    }

    @Override
    public boolean accept(Video video) {
        return true;
    }

}
