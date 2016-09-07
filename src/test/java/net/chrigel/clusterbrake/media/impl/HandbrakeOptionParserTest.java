package net.chrigel.clusterbrake.media.impl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.TestUtility;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 *
 */
public class HandbrakeOptionParserTest {

    private final HandbrakeOptionParser subject = new HandbrakeOptionParser();

    private static final File TEST_BASE_DIR = TestUtility.getTestDir();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeClass
    public static void classSetup() {
        TEST_BASE_DIR.mkdirs();
    }

    @AfterClass
    public static void tearDown() throws IOException {
        FileUtils.deleteDirectory(TEST_BASE_DIR);
    }

    @Test
    public void testParseFile_ShouldParseFileCorrectly() throws Exception {

        File optionFile = new File(TEST_BASE_DIR, "options.conf");

        List<String> lines = new LinkedList<>();
        lines.add("--encoder x265 ");
        lines.add("# comment");
        lines.add(" --markers");
        lines.add("--gain 4.5");

        FileUtils.writeLines(optionFile, lines);

        List<String> options = subject.parseFile(optionFile);

        assertThat(options, allOf(
                hasItems("--encoder x265", "--markers", "--gain 4.5"),
                not(hasItem("# comment"))));
    }

}
