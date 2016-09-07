package net.chrigel.clusterbrake.media.impl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.TestUtility;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class AbstractFileScannerTest {

    private final AbstractFileScannerImpl<File> subject = new AbstractFileScannerImpl<>();

    private static final File TEST_BASE_DIR = TestUtility.getTestDir();

    @BeforeClass
    public static void classSetup() {
        TEST_BASE_DIR.mkdirs();
    }

    @AfterClass
    public static void tearDown() throws IOException {
        FileUtils.deleteDirectory(TEST_BASE_DIR);
    }

    @Test
    public void testScanForFiles() throws Exception {

        List<String> extensions = new LinkedList<>();
        extensions.add("txt");
        extensions.add("doc");

        String fileName = getClass().getSimpleName();

        File searchFile = TEST_BASE_DIR;

        File textFile = new File(TEST_BASE_DIR, fileName + ".txt");
        File docFile = new File(TEST_BASE_DIR, fileName + ".doc");
        File ignoredFile = new File(TEST_BASE_DIR, fileName + ".ignore");

        FileUtils.touch(searchFile);
        FileUtils.touch(docFile);
        FileUtils.touch(textFile);
        FileUtils.touch(ignoredFile);

        List<File> files = new LinkedList<>();
        subject.scanForFiles(searchFile, extensions, false).forEachRemaining(file -> {
            files.add(file);
        });

        assertThat(files, allOf(hasItems(docFile, textFile), not(hasItems(ignoredFile))));
    }

    public class AbstractFileScannerImpl<T> extends AbstractFileScanner<T> {

        @Override
        public List scan() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

}
