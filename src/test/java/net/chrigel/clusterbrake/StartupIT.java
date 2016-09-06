package net.chrigel.clusterbrake;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 */
public class StartupIT {

    private static final File TEST_BASE_DIR = new File("test");

    private final File inputDir = new File("target/input");
    private final File outputDir = new File("target/output");
    private final File testSource = new File(TestUtility.getTestResourcesDir(), "testsample.mp4");
    private final File templateDir = new File("target/templates");
    private final File configDir = new File("target/config");
    private final File tempDir = new File("target/tmp");

    @Before
    public void setup() {
        TEST_BASE_DIR.mkdir();
    }

    @After
    public void after() throws IOException {
        FileUtils.deleteDirectory(TEST_BASE_DIR);
    }

    @Test
    public void testManaualTranscode() throws Exception {

        inputDir.mkdir();
        outputDir.mkdir();
        configDir.mkdir();
        tempDir.mkdir();
        FileUtils.copyFile(testSource, new File(inputDir, "manual/testsample.mp4"));
        FileUtils.copyFile(
                new File(TestUtility.getTestResourcesDir(), "manual.conf"),
                new File(templateDir, "manual.conf"));

        Startup.main(new String[]{"target/clusterbrake.properties"});

        Thread.sleep(1000000000);
    }

}
