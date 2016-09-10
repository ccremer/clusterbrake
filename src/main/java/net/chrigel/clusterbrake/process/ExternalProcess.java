package net.chrigel.clusterbrake.process;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public interface ExternalProcess {
    
    ExternalProcess withIORedirected(boolean redirectIO);
    
    ExternalProcess withArguments(List<String> arguments);
    
    ExternalProcess withPath(String path);
    
    int start() throws InterruptedException, IOException;
    
    void destroy(long timeout, TimeUnit unit) throws InterruptedException;
    
}
