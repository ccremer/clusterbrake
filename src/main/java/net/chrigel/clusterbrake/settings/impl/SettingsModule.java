package net.chrigel.clusterbrake.settings.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.chrigel.clusterbrake.settings.NodeSettings;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.settings.JobSettings;

/**
 *
 */
public class SettingsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NodeSettings.class).to(NodeSettingsImpl.class).in(Singleton.class);
        bind(Job.class).to(JobImpl.class);
        bind(JobSettings.class).to(JobSettingsImpl.class);
    }

}
