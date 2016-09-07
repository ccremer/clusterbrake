package net.chrigel.clusterbrake.workflow.manualauto.settings.impl;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;
import net.chrigel.clusterbrake.media.DirType;
import net.chrigel.clusterbrake.workflow.manualauto.DirTypes;

/**
 *
 */
public class DirTypeConverter implements Converter<DirType> {

    @Override
    public DirType deserialize(ObjectReader reader, Context ctx) throws Exception {
        return DirTypes.valueOf(reader.valueAsString());
    }

    @Override
    public void serialize(DirType type, ObjectWriter writer, Context ctx) throws Exception {
        writer.writeString(type.toString());
    }

}
