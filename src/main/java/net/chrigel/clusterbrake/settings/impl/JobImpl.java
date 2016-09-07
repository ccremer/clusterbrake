package net.chrigel.clusterbrake.settings.impl;

import java.time.LocalDateTime;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.settings.Job;

/**
 *
 */
public class JobImpl
        implements Job {

    private VideoPackage pkg;
    private String nodeId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    @Override
    public VideoPackage getVideoPackage() {
        return pkg;
    }

    @Override
    public String getNodeID() {
        return nodeId;
    }

    @Override
    public void setVideoPackage(VideoPackage pkg) {
        this.pkg = pkg;
    }

    @Override
    public void setNodeID(String id) {
        this.nodeId = id;
    }

    @Override
    public LocalDateTime getStartTime() {
        return beginTime;
    }

    @Override
    public void setStartTime(LocalDateTime time) {
        this.beginTime = time;
    }

    @Override
    public LocalDateTime getFinishTime() {
        return endTime;
    }

    @Override
    public void setFinishTime(LocalDateTime time) {
        this.endTime = time;
    }

}
