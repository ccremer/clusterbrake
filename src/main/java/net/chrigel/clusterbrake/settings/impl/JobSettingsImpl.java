package net.chrigel.clusterbrake.settings.impl;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.reflect.VisibilityFilter;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.DirType;
import net.chrigel.clusterbrake.media.impl.VideoPackageImpl;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.settings.JobSettings;
import net.chrigel.clusterbrake.workflow.manualauto.DirTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
class JobSettingsImpl
        implements JobSettings {

    private File settingsFile;
    private final Logger logger;
    private final GensonSerializer<Queue> serializer;

    JobSettingsImpl() {
        this.logger = LogManager.getLogger(getClass());
        this.serializer = new GensonSerializer<>(Queue.class, new GensonBuilder()
                .acceptSingleValueAsList(true)
                .useIndentation(true)
                .useFields(true, VisibilityFilter.PRIVATE)
                .useMethods(false)
                .useClassMetadata(true)
                .useRuntimeType(true)
                .addAlias("Queue", Queue.class)
                .addAlias("Job", JobImpl.class)
                .addAlias("VideoPackage", VideoPackageImpl.class)
                .withConverter(new DirTypeDeserializer(), DirType.class)
        );
    }

    @Override
    public List<Job> getJobs() {
        try {
            logger.info("Reading jobs from {}", settingsFile.getAbsolutePath());
            Queue queue = serializer.deserialize(settingsFile);
            return queue.getJobs();
        } catch (IOException ex) {
            logger.error(ex);
            return new LinkedList<>();
        }
    }

    @Override
    public File getSettingsFile() {
        return settingsFile;
    }

    @Override
    public void setJobs(List<Job> queue) {
        try {
            logger.info("Saving jobs in {}", settingsFile.getAbsolutePath());
            serializer.serialize(settingsFile, new Queue(queue));
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    @Override
    public void setSettingsFile(File file) {
        this.settingsFile = file;
    }

    class DirTypeDeserializer implements Converter<DirType> {

        @Override
        public DirType deserialize(ObjectReader reader, Context ctx) throws Exception {
            DirType type = DirTypes.valueOf(reader.valueAsString());
            return type;
        }

        @Override
        public void serialize(DirType type, ObjectWriter writer, Context ctx) throws Exception {
     //       writer.beginObject();
            writer.writeString("type", type.toString());
       //     writer.endObject();
        }

    }
}
