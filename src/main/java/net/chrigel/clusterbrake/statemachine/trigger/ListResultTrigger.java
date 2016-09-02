package net.chrigel.clusterbrake.statemachine.trigger;

import java.util.List;
import net.chrigel.clusterbrake.statemachine.Trigger;

/**
 *
 * @param <T>
 */
public class ListResultTrigger<T>
        implements Trigger {

    private final List<T> videos;

    public ListResultTrigger(List<T> list) {
        this.videos = list;
    }

    public List<T> getList() {
        return videos;
    }
}
