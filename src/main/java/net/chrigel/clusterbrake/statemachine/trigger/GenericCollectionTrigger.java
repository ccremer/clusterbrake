package net.chrigel.clusterbrake.statemachine.trigger;

import java.util.Collection;
import java.util.List;
import net.chrigel.clusterbrake.util.UnsafeCastUtil;

/**
 *
 */
public class GenericCollectionTrigger
        extends GenericPayloadTrigger<Collection> {

    public GenericCollectionTrigger(Collection payload) {
        super(payload);
    }

    public <E> Collection<E> getCollection(Class<E> elementClass) {
        return UnsafeCastUtil.cast(getPayload());
    }

    public <E> List<E> getList(Class<E> elementClass) {
        return UnsafeCastUtil.cast(getPayload());
    }
}
