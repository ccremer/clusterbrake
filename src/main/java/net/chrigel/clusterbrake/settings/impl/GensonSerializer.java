package net.chrigel.clusterbrake.settings.impl;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @param <T>
 */
public class GensonSerializer<T> {

    private Genson genson;
    private Class<T> clazz;

    public GensonSerializer(Class<T> clazz) {
        this(clazz, null);
    }

    public GensonSerializer(Class<T> clazz, GensonBuilder gensonBuilder) {
        if (gensonBuilder == null) {
            this.genson = new GensonBuilder().useIndentation(true).create();
        } else {
            this.genson = gensonBuilder.create();
        }
        this.clazz = clazz;
    }

    public T deserialize(File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            return genson.deserialize(stream, clazz);
        }
    }

    public void serialize(File file, T instance) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            genson.serialize(instance, stream);
        }
    }

}
