package net.chrigel.clusterbrake.settings.impl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.media.impl.VideoPackageImpl;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import net.chrigel.clusterbrake.settings.Job;
import static org.junit.Assert.assertThat;

public class QueueSettingsImplTest {

    private JobSettingsImpl subject;
    private static final File TEST_BASE_DIR = new File("test");

    @BeforeClass
    public static void classSetup() {
        TEST_BASE_DIR.mkdir();
    }

    @AfterClass
    public static void tearDown() throws IOException {
        FileUtils.deleteDirectory(TEST_BASE_DIR);
    }

    @Test
    public void testGetQueueSettingsFile() {
        File queueFile = new File(TEST_BASE_DIR, "queue.json");
        subject = new JobSettingsImpl();
        subject.setSettingsFile(queueFile);
        
        assertThat(subject.getSettingsFile(), equalTo(queueFile));
    }

    @Test
    public void testSerializeDeserialize() throws IOException {
        File queueFile = new File(TEST_BASE_DIR, "queue.json");
        File outputFile = new File(TEST_BASE_DIR, "movie.mp4");
        subject = new JobSettingsImpl();
        subject.setSettingsFile(queueFile);

        List<Job> queues = new LinkedList<>();
        Job queue = new JobImpl();
        VideoPackage pkg = new VideoPackageImpl();
        pkg.setOutputFile(outputFile);
        queue.setVideoPackage(pkg);
        queue.setNodeID("1");
        queues.add(queue);
        subject.setJobs(queues);

        List<Job> newQueue = subject.getJobs();
        assertThat(newQueue.get(0).getNodeID(), equalTo("1"));
        assertThat(newQueue.get(0).getVideoPackage().getOutputFile(), equalTo(outputFile));
    }

}
