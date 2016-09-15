package net.chrigel.clusterbrake.settings.constraint;

import net.chrigel.clusterbrake.media.VideoPackage;

/**
 *
 */
public interface Constraint {
    
    boolean accept(VideoPackage video);
    
}
