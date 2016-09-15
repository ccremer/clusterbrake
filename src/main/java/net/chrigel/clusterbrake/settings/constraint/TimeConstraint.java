package net.chrigel.clusterbrake.settings.constraint;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.time.Clock;
import java.time.LocalTime;
import net.chrigel.clusterbrake.media.VideoPackage;

/**
 *
 */
public class TimeConstraint
        implements Constraint {

    private LocalTime currentTime;
    private boolean skipConstraint;
    private final int beginHour;
    private final int beginMinute;
    private final int stopHour;
    private final int stopMinute;

    /**
     * Creates a new time constraint.
     *
     * @param begin a string in the format "hh:mm". Set to "-1" if not enabled.
     * @param stop a string in the format "hh:mm". Set to "-1" if not enabled.
     */
    @Inject
    public TimeConstraint(
            @Named("node.constraint.time.begin") String begin,
            @Named("node.constraint.time.stop") String stop
    ) {
        if ("-1".equals(begin) || "-1".equals(stop)) {
            skipConstraint = true;
            this.beginHour = -1;
            this.beginMinute = -1;
            this.stopHour = -1;
            this.stopMinute = -1;
        } else {
            this.beginHour = Integer.valueOf(begin.split(":")[0]);
            this.beginMinute = Integer.valueOf(begin.split(":")[1]);
            this.stopHour = Integer.valueOf(stop.split(":")[0]);
            this.stopMinute = Integer.valueOf(stop.split(":")[1]);
        }
    }

    /**
     * Sets the current timestamp. This is used to fake or simulate a constant timestamp.
     *
     * @param timestamp
     */
    void setCurrentTime(LocalTime timestamp) {
        this.currentTime = timestamp;
    }

    private LocalTime getTimestamp() {
        if (currentTime == null) {
            return LocalTime.now(Clock.systemDefaultZone());
        } else {
            return currentTime;
        }
    }

    @Override
    public boolean accept(VideoPackage video) {
        if (skipConstraint) {
            return true;
        } else {
            LocalTime begin = LocalTime.of(beginHour, beginMinute);
            LocalTime stop = LocalTime.of(stopHour, stopMinute);
            LocalTime now = getTimestamp();
            if (begin.compareTo(stop) == 0) {
                return true;
            }
            if (begin.isBefore(stop)) {
                return begin.isBefore(now) && now.isBefore(stop);
            } else {
                return (now.isAfter(begin) && now.isAfter(stop)) || (now.isBefore(stop) && now.isBefore(begin));
            }
        }

    }

}
