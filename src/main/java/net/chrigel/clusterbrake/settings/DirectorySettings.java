package net.chrigel.clusterbrake.settings;

import java.io.File;

/**
 *
 */
public interface DirectorySettings {

    File getManualInputBaseDir();

    File getAutomaticInputBaseDir();

    File getCustomPresetDir();

    File getPreinstalledPresetDir();

    File getStageManualDir();

    File getStageAutomaticDir();

    File getStageQueueDir();

    File getStageCurrentDir();

    File getStageFinishedDir();

    File getOutputBaseDir();
}
