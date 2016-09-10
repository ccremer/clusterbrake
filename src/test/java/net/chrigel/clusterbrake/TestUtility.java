package net.chrigel.clusterbrake;

import java.io.File;
import net.chrigel.clusterbrake.media.DirType;
import net.chrigel.clusterbrake.workflow.manualauto.DirTypes;

/**
 *
 */
public class TestUtility {

    public static File getTestResourcesDir() {
        return new File("src/test/resources");
    }

    public static void initDirs() {
        File inputFolder = new File("test/input");
        File outputFolder = new File("test/output");

        DirTypes.CONFIG.setDir(new File("test/config"));
        DirTypes.INPUT.setDir(inputFolder);
        DirTypes.INPUT_AUTO.setDir(new File(inputFolder, "auto"));
        DirTypes.INPUT_MANUAL.setDir(new File(inputFolder, "manual"));
        DirTypes.OUTPUT.setDir(outputFolder);
        DirTypes.OUTPUT_AUTO.setDir(new File(outputFolder, "auto"));
        DirTypes.OUTPUT_MANUAL.setDir(new File(outputFolder, "manual"));
        DirTypes.TMP.setDir(new File("test/tmp"));
        DirTypes.TEMPLATE.setDir(new File("target/config/templates"));
        DirTypes.SCRIPT.setDir(new File("target/config/scripts"));
    }

    public static DirType getTestDirType() {
        return new TestDirType();
    }

    public static File getTestDir() {
        return new File("test");
    }

}
