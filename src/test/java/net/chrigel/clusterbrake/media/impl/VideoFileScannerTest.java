package net.chrigel.clusterbrake.media.impl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.media.Video;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 */
public class VideoFileScannerTest {

    private final VideoFileScanner subject = new VideoFileScanner();

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
        extensions.add("mp4");
        extensions.add("mkv");

        String fileName = getClass().getSimpleName();

        File searchFile = TEST_BASE_DIR;

        File mp4File = new File(TEST_BASE_DIR, fileName + ".mp4");
        File mkvFile = new File(TEST_BASE_DIR, fileName + ".mkv");
        File ignoredFile = new File(TEST_BASE_DIR, fileName + ".ignore");

        FileUtils.touch(mkvFile);
        FileUtils.touch(mp4File);
        FileUtils.touch(ignoredFile);

        List<Video> files = subject.search(searchFile)
                .withFileExtensionFilter(extensions)
                .scan();

        assertThat(files.size(), equalTo(2));
    }

}
