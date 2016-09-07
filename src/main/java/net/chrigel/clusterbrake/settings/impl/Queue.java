
package net.chrigel.clusterbrake.settings.impl;

import java.util.List;
import net.chrigel.clusterbrake.settings.Job;

/**
 *
 */
public class Queue {

    private List<Job> jobs;

    public Queue() {
    }

    Queue(List<Job> jobs) {
        this.jobs = jobs;
    }

    List<Job> getJobs() {
        return jobs;
    }

}
