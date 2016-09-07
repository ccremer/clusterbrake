package net.chrigel.clusterbrake.settings.impl;

import net.chrigel.clusterbrake.persistence.GensonSerializer;
import com.google.inject.Inject;
import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.reflect.VisibilityFilter;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.DirType;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.impl.VideoPackageImpl;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.settings.JobSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class JobSettingsImpl
        implements JobSettings {

    private File settingsFile;
    private final Logger logger;
    private final GensonSerializer<Queue> serializer;

    @Inject
    JobSettingsImpl(
            Converter<DirType> dirTypeConverter
    ) {
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
                .addAlias("File", FileContainer.class)
                .withConverter(dirTypeConverter, DirType.class)
                .withConverter(new DateConverter(), LocalDateTime.class)
        );
    }

    @Override
    public List<Job> getJobs() {
        try {
            logger.info("Reading jobs from {}", settingsFile.getPath());
            Queue queue = serializer.deserialize(settingsFile);
            return queue.getJobs();
        } catch (IOException ex) {
            logger.error(ex);
            return new LinkedList<>();
        }
    }

    @Override
    public void setJobs(List<Job> queue) {
        try {
            logger.info("Saving jobs in {}", settingsFile.getPath());
            serializer.serialize(settingsFile, new Queue(queue));
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    public void setSettingsFile(File file) {
        this.settingsFile = file;
    }

    class DateConverter implements Converter<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime timestamp, ObjectWriter writer, Context context) throws Exception {
            writer.writeString(timestamp.toInstant(ZoneOffset.UTC).toString());
        }

        @Override
        public LocalDateTime deserialize(ObjectReader reader, Context context) throws Exception {
            return LocalDateTime.ofInstant(Instant.parse(reader.valueAsString()), ZoneId.of("Z"));
        }

    }
}
