package net.chrigel.clusterbrake.media.impl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class OptionFileScannerTest {

    private final OptionFileScanner subject = new OptionFileScanner();

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
    public void testScanForSameFilenamesButDifferentExtensions() throws Exception {

        List<String> extensions = new LinkedList<>();
        extensions.add("conf");

        String fileName = getClass().getSimpleName();

        File searchFile = new File(TEST_BASE_DIR, fileName + ".class");

        File textFile = new File(TEST_BASE_DIR, fileName + ".conf");
        File docFile = new File(TEST_BASE_DIR, fileName + ".doc");
        File ignoredFile = new File(TEST_BASE_DIR, fileName + ".ignore");

        FileUtils.touch(searchFile);
        FileUtils.touch(docFile);
        FileUtils.touch(textFile);
        FileUtils.touch(ignoredFile);

        List<VideoOptionPackage> files = subject.search(searchFile)
                .withFileExtensionFilter(extensions)
                .scanForSameFilenamesButDifferentExtensions();

        assertThat(files.size(), equalTo(1));
    }

}
