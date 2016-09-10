package net.chrigel.clusterbrake.process.impl;

import net.chrigel.clusterbrake.process.CommandLineInterpreter;

/**
 *
 */
class Bash
        implements CommandLineInterpreter {

    @Override
    public String getPath() {
        return "/bin/bash";
    }

}
