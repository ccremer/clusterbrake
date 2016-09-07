package net.chrigel.clusterbrake.transcode.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.TestUtility;
import net.chrigel.clusterbrake.transcode.TranscoderSettings;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class HandbrakeCliIT {

    @Mock
    private TranscoderSettings settings;

    private HandbrakeCli subject;

    @Before
    public void setUp() throws FileNotFoundException {
        MockitoAnnotations.initMocks(this);
        when(settings.getCLIPath()).thenReturn("C:\\Program Files\\Handbrake\\HandBrakeCLI.exe");
        when(settings.isIORedirected()).thenReturn(true);
        subject = new HandbrakeCli(settings);
    }

    @Test
    public void testTranscode() throws Exception {

        List<String> options = new LinkedList<>();

        File source = new File(TestUtility.getTestResourcesDir(), "testsample.mp4");
        File output = new File(TestUtility.getTestResourcesDir(), "testoutput.mp4");
        output.delete();
        options.add("--encoder mpeg4");

        subject.from(source).to(output).withOptions(options).transcode();
        output.delete();
    }

}
