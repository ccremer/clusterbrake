package net.chrigel.clusterbrake.transcode.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import net.chrigel.clusterbrake.transcode.TranscoderSettings;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class HandbrakeCliTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Mock
    private TranscoderSettings settings;

    private HandbrakeCli subject;

    @Before
    public void setUp() throws FileNotFoundException {
        MockitoAnnotations.initMocks(this);
        when(settings.getCLIPath()).thenReturn("C:\\Program Files\\Handbrake\\HandBrakeCLI.exe");
        when(settings.isIORedirected()).thenReturn(false);
        subject = new HandbrakeCli(settings);
    }

    @Test
    public void testFrom_ShouldThrowException_IfFileDoesNotExist() throws Exception {
        expectedException.expect(FileNotFoundException.class);

        subject.from(new File(".inexistent"));
    }

    @Test
    public void testTo_ShouldThrowException_IfDirectoryDoesNotExist() throws Exception {
        expectedException.expect(IllegalArgumentException.class);

        subject.to(new File(new File("inexistentDir"), "inexistentFile"));
    }

    @Test
    public void testWithOptions_ShouldThrowException_IfValidationFails() {
        expectedException.expect(IllegalArgumentException.class);
        List<String> options = new LinkedList<>();

        options.add("--help");

        subject.withOptions(options);
    }

}
