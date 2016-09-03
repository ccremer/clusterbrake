package net.chrigel.clusterbrake.settings.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.io.File;
import net.chrigel.clusterbrake.settings.TemplateSettings;

/**
 *
 */
class TemplateSettingsImpl
        implements TemplateSettings {

    private final File templateDir;

    @Inject
    TemplateSettingsImpl(
            @Named("templates.dir") String templateDir
    ) {
        this.templateDir = new File(templateDir);
    }

    @Override
    public File getTemplateDir() {
        return templateDir;
    }

}
