package net.chrigel.clusterbrake.workflow.manualauto.settings;

import java.io.File;
import java.util.List;

/**
 *
 */
public interface InputSettings {

    File getManualInputDirectory();
    
    File getAutoInputDirectory();

    List<String> getVideoExtensions();

}
