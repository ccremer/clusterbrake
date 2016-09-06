package net.chrigel.clusterbrake.workflow.manualauto.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.chrigel.clusterbrake.workflow.manualauto.settings.CleanupSettings;

/**
 *
 */
class CleanupSettingsImpl
        implements CleanupSettings {

    private final boolean sourceDeletionEnabled;

    private final boolean asyncMoveEnabled;

    @Inject
    CleanupSettingsImpl(
            @Named("workflow.cleanup.deleteSource") boolean sourceDeletionEnabled,
            @Named("workflow.cleanup.moveAsync") boolean asyncMoveEnabled
    ) {
        this.sourceDeletionEnabled = sourceDeletionEnabled;
        this.asyncMoveEnabled = asyncMoveEnabled;
    }

    @Override
    public boolean isSourceDeletionEnabled() {
        return sourceDeletionEnabled;
    }

    @Override
    public boolean isAsyncMoveEnabled() {
        return asyncMoveEnabled;
    }

}
