package net.chrigel.clusterbrake.settings;

import java.util.List;

/**
 *
 */
public interface JobSettings {

    List<Job> getJobs();
    
    void setJobs(List<Job> jobs);

}
