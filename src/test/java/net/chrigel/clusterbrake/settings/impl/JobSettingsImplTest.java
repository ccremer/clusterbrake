package net.chrigel.clusterbrake.settings.impl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.TestUtility;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.media.impl.VideoPackageImpl;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.workflow.manualauto.DirTypes;
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
    public void testGetQueueSettingsFile() {
        File queueFile = new File(testBaseDir, "queue.json");
        subject = new JobSettingsImpl();
        subject.setSettingsFile(queueFile);

        assertThat(subject.getSettingsFile(), equalTo(queueFile));
    }

    @Test
    public void testSerializeDeserialize() throws IOException {
        File queueFile = new File(testBaseDir, "queue.json");
        FileContainer outputContainer = new FileContainer(DirTypes.OUTPUT_MANUAL, "movie.mp4");
        subject = new JobSettingsImpl();
        subject.setSettingsFile(queueFile);

        List<Job> queues = new LinkedList<>();
        Job queue = new JobImpl();
        VideoPackage pkg = new VideoPackageImpl();
        pkg.setOutputFile(outputContainer);
        queue.setVideoPackage(pkg);
        queue.setNodeID("1");
        queues.add(queue);
        subject.setJobs(queues);

        List<Job> newQueue = subject.getJobs();
        FileContainer cont = newQueue.get(0).getVideoPackage().getOutputFile();
        assertThat(newQueue.get(0).getNodeID(), equalTo("1"));
        assertThat(cont, equalTo(outputContainer));
    }

}
