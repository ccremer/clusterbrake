package net.chrigel.clusterbrake.settings.impl;

import com.owlike.genson.GensonBuilder;
import com.owlike.genson.reflect.VisibilityFilter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.impl.VideoPackageImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.settings.JobSettings;

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
        );
    }

    @Override
    public List<Job> getJobs() {
        try {
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
            Queue holder = new Queue(queue);
            serializer.serialize(settingsFile, holder);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    @Override
    public void setSettingsFile(File file) {
        this.settingsFile = file;
    }

}
