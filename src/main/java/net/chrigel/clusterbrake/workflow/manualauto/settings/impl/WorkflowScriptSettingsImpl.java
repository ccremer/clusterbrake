package net.chrigel.clusterbrake.workflow.manualauto.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.chrigel.clusterbrake.workflow.manualauto.settings.WorkflowScriptSettings;

/**
 *
 */
public class WorkflowScriptSettingsImpl
        implements WorkflowScriptSettings {

    private final String postQueueScriptName;
    private final String postCleanupScriptName;

    @Inject
    WorkflowScriptSettingsImpl(
            @Named("workflow.script.afterQueue") String postQueueScriptName,
            @Named("workflow.script.afterCleanup") String postCleanupScriptName
    ) {
        String extension;
        if (System.getProperty("os.name").startsWith("Windows")) {
            extension = ".cmd";
        } else {
            extension = ".sh";
        }
        this.postQueueScriptName = postQueueScriptName + extension;
        this.postCleanupScriptName = postCleanupScriptName + extension;

    }

    @Override
    public String getPostQueueScriptName() {
        return postQueueScriptName;
    }

    @Override
    public String getPostCleanupScriptName() {
        return postCleanupScriptName;
    }

}
