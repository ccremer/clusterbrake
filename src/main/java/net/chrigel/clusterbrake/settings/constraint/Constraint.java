package net.chrigel.clusterbrake.settings.constraint;

import net.chrigel.clusterbrake.media.Video;

/**
 *
 */
public interface Constraint {
    
    boolean accept(Video video);
    
}
