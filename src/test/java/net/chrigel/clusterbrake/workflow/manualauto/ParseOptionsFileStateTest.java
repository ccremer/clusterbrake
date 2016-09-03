package net.chrigel.clusterbrake.workflow.manualauto;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class ParseOptionsFileStateTest {

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
    public void testEnterState_ShouldCorrectlyParseTemplates() {
        
        
        
    }

}
