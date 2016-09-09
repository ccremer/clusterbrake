package net.chrigel.clusterbrake.workflow.manualauto;

import java.io.File;
import net.chrigel.clusterbrake.media.DirType;

/**
 *
 */
public enum DirTypes
        implements DirType {

    INPUT,
    INPUT_AUTO,
    INPUT_MANUAL,
    OUTPUT,
    OUTPUT_AUTO,
    OUTPUT_MANUAL,
    CONFIG,
    TEMPLATE,
    TMP,
    SCRIPT;

    private File dir;

    @Override
    public File getBase() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        if (dir == null) {
            return "";
        } else {
            return dir.toString();
        }
    }

}
