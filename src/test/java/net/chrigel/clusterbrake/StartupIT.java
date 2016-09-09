package net.chrigel.clusterbrake;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import net.chrigel.clusterbrake.workflow.manualauto.DirTypes;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;

/**
 *
 */
public class StartupIT {

    private static final File TEST_BASE_DIR = TestUtility.getTestDir();

    private final File testSource = new File(TestUtility.getTestResourcesDir(), "testsample.mp4");

    @Before
    public void setup() {
        TestUtility.initDirs();
        TEST_BASE_DIR.mkdir();
    }

    @After
    public void after() throws IOException {
        FileUtils.deleteDirectory(TEST_BASE_DIR);
        new File("target/config/finished.json").delete();
        new File("target/config/queue.json").delete();
    }

    //  @Test
    public void testManaualTranscode() throws Exception {

        DirTypes.INPUT_AUTO.getBase().mkdirs();
        DirTypes.INPUT_MANUAL.getBase().mkdirs();
        DirTypes.TEMPLATE.getBase().mkdirs();
        DirTypes.SCRIPT.getBase().mkdirs();
        FileUtils.copyFile(testSource, new File(DirTypes.INPUT_MANUAL.getBase(), "test/testsample.mp4"));
        FileUtils.copyFile(
                new File(TestUtility.getTestResourcesDir(), "test.handbrake"),
                new File(DirTypes.TEMPLATE.getBase(), "test.handbrake"));
        FileUtils.writeLines(new File(DirTypes.SCRIPT.getBase(), "postQueue.cmd"), Arrays.asList("echo manual"));

        Startup.main(new String[]{"target/clusterbrake.properties"});

        Thread.sleep(1000000000);
    }

    @Test
    public void testAutoTranscode() throws Exception {

        DirTypes.INPUT_AUTO.getBase().mkdirs();
        DirTypes.INPUT_MANUAL.getBase().mkdirs();
        DirTypes.TEMPLATE.getBase().mkdirs();
        DirTypes.SCRIPT.getBase().mkdirs();
        FileUtils.copyFile(testSource, new File(DirTypes.INPUT_AUTO.getBase(), "folder/testsample.mp4"));
        FileUtils.copyFile(
                new File(TestUtility.getTestResourcesDir(), "test.handbrake"),
                new File(DirTypes.TEMPLATE.getBase(), "auto.handbrake"));
        FileUtils.writeLines(new File(DirTypes.SCRIPT.getBase(), "postQueue.cmd"), Arrays.asList("echo auto"));
        FileUtils.writeLines(new File(DirTypes.SCRIPT.getBase(), "postCleanup.cmd"), Arrays.asList("echo postCleanup"));

        Startup.main(new String[]{"target/clusterbrake.properties"});

        Thread.sleep(1000000000);
    }
}
