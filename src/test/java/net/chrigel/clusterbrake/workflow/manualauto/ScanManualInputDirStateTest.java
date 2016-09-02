package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.media.impl.VideoFileScanner;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.ListResultTrigger;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 */
public class ScanManualInputDirStateTest {

    @Mock
    private StateContext context;

    @Mock
    private InputSettings inputSettings;

    @Mock
    private Provider<FileScanner<Video>> scannerProvider;

    private ScanManualInputDirState subject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(scannerProvider.get()).thenReturn(new VideoFileScanner());
        when(inputSettings.getManualInputDirectory()).thenReturn(TEST_BASE_DIR);
        when(inputSettings.getVideoExtensions()).thenReturn(Arrays.asList("mp4", "mkv"));
    }

    private static final File TEST_BASE_DIR = new File("test");

    @BeforeClass
    public static void classSetup() {
        TEST_BASE_DIR.mkdir();
    }

    @AfterClass
    public static void tearDown() throws IOException {
        FileUtils.deleteDirectory(TEST_BASE_DIR);
    }

    private ScanManualInputDirState createSubject() {
        return new ScanManualInputDirState(context, inputSettings, scannerProvider);
    }

    @Test
    public void testEnterState() throws IOException {
        subject = createSubject();

        File level1File = new File(TEST_BASE_DIR, "video2.mp4");
        File level2File = new File(TEST_BASE_DIR, "template1/video2.mkv");
        File level3File = new File(TEST_BASE_DIR, "template2/subdir/video3.mp4");

        AtomicBoolean called = new AtomicBoolean();
        subject.bindNextStateToTrigger(null, ListResultTrigger.class, (ListResultTrigger param) -> {

            assertThat(param.getList().size(), equalTo(3));

            called.set(true);
            return null;
        });

        FileUtils.touch(level1File);
        FileUtils.touch(level2File);
        FileUtils.touch(level3File);

        subject.enter();
        assertThat(called.get(), equalTo(true));
    }

}
