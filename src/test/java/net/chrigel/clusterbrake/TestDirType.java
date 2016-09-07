package net.chrigel.clusterbrake;

import java.io.File;
import net.chrigel.clusterbrake.media.DirType;

/**
 *
 */
public class TestDirType
        implements DirType {

    @Override
    public File getBase() {
        return new File("test");
    }

}
