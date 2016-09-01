package net.chrigel.clusterbrake.settings;

import net.chrigel.clusterbrake.media.Video;

/**
 *
 */
public interface Constraint {
    
    boolean accept(Video video);
    
}
