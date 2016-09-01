package net.chrigel.clusterbrake.settings;

import java.io.File;
import java.util.List;

/**
 *
 */
public interface InputSettings {

    File getBaseDirectory();

    List<String> getVideoExtensions();

}
