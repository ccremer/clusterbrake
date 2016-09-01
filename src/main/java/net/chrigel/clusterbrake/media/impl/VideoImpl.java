package net.chrigel.clusterbrake.media.impl;

import com.google.inject.Inject;
import java.io.File;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.settings.InputSettings;

/**
 *
 */
public class VideoImpl
        implements Video {

    @Inject
    VideoImpl(InputSettings inputSettings) {
        
        
    }

    
    
    @Override
    public File getSourceFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
