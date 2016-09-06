package net.chrigel.clusterbrake.workflow.manualauto.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.io.File;
import net.chrigel.clusterbrake.workflow.manualauto.settings.WorkflowQueueSettings;

/**
 *
 */
class WorkflowQueueSettingsImpl
        implements WorkflowQueueSettings {

    private final File tempDir;

    @Inject
    WorkflowQueueSettingsImpl(
            @Named("workflow.queue.dir.temp") String tempDir
    ) {
        this.tempDir = new File(tempDir);
    }

    @Override
    public File getTemporaryDir() {
        return tempDir;
    }

}
