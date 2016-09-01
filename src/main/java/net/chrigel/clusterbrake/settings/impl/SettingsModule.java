package net.chrigel.clusterbrake.settings.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.chrigel.clusterbrake.settings.DirectorySettings;
import net.chrigel.clusterbrake.settings.NodeSettings;

/**
 *
 */
public class SettingsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NodeSettings.class).to(NodeSettingsImpl.class).in(Singleton.class);
        bind(DirectorySettings.class).to(DirectorySettingsImpl.class);
    }

}
