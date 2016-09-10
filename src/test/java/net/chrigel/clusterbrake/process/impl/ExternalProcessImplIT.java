package net.chrigel.clusterbrake.process.impl;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

public class ExternalProcessImplIT {
    
    private ExternalProcessImpl subject;
    
    @Before
    public void setup() {
        subject = new ExternalProcessImpl();
    }
    
    @Test
    public void testStart_ShouldInvokeBash() throws Exception {
        
        subject.withPath("/bin/bash");
        subject.withArguments(Arrays.asList("a", "b"));
        subject.start();
    }

    
}
