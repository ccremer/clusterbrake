package net.chrigel.clusterbrake.settings.impl;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.TestUtility;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.media.impl.VideoPackageImpl;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.workflow.manualauto.DirTypes;
import net.chrigel.clusterbrake.workflow.manualauto.settings.impl.DirTypeConverter;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;

public class JobSettingsImplTest {

    private JobSettingsImpl subject;
    private final File testBaseDir = DirTypes.CONFIG.getBase();

    @BeforeClass
    public static void classSetup() {
        TestUtility.initDirs();
        DirTypes.CONFIG.getBase().mkdirs();
    }

    @AfterClass
    public static void tearDown() throws IOException {
        FileUtils.deleteDirectory(TestUtility.getTestDir());
    }

    @Test
    public void testSerializeDeserialize() throws IOException {
        File queueFile = new File(testBaseDir, "queue.json");
        FileContainer outputContainer = new FileContainer(DirTypes.OUTPUT_MANUAL, "movie.mp4");
        subject = new JobSettingsImpl(new DirTypeConverter());
        subject.setSettingsFile(queueFile);

        LocalDateTime timestamp = LocalDateTime.now(Clock.systemDefaultZone());
        List<Job> queues = new LinkedList<>();
        Job job = new JobImpl();
        VideoPackage pkg = new VideoPackageImpl();
        pkg.setOutputFile(outputContainer);
        job.setVideoPackage(pkg);
        job.setNodeID("1");
        job.setStartTime(timestamp);
        queues.add(job);
        subject.setJobs(queues);

        List<Job> deserializedQueue = subject.getJobs();
        Job result = deserializedQueue.get(0);
        FileContainer cont = result.getVideoPackage().getOutputFile();
        assertThat(result.getNodeID(), equalTo("1"));
        assertThat(cont, equalTo(outputContainer));
        assertThat(result.getStartTime(), equalTo(timestamp));
    }

}
