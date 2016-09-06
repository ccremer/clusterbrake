package net.chrigel.clusterbrake.settings;

import java.io.File;
import java.util.List;

/**
 *
 */
public interface JobSettings {

    List<Job> getJobs();
    
    void setJobs(List<Job> jobs);

    File getSettingsFile();
    
    void setSettingsFile(File file);
}
