package net.chrigel.clusterbrake.settings;

import java.time.LocalDateTime;
import net.chrigel.clusterbrake.media.VideoPackage;

/**
 *
 */
public interface Job {

    VideoPackage getVideoPackage();

    void setVideoPackage(VideoPackage pkg);

    String getNodeID();

    void setNodeID(String id);

    LocalDateTime getStartTime();

    void setStartTime(LocalDateTime time);

    LocalDateTime getFinishTime();

    void setFinishTime(LocalDateTime time);
}
