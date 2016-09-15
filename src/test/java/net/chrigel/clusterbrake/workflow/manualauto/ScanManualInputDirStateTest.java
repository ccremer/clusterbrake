package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import net.chrigel.clusterbrake.TestUtility;
import net.chrigel.clusterbrake.media.FileScanner;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.media.impl.VideoFileScanner;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.GenericCollectionTrigger;
import net.chrigel.clusterbrake.workflow.manualauto.settings.InputSettings;
import net.chrigel.clusterbrake.workflow.manualauto.triggers.NoResultTrigger;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
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
    private Provider<FileScanner<VideoPackage>> scannerProvider;
    @Mock
    private Provider<VideoPackage> packageProvider;
    @Mock
    private VideoPackage videoPackage1;
    @Mock
    private VideoPackage videoPackage2;

    private ScanManualInputDirState subject;

    @Before
    public void setup() {
        TestUtility.initDirs();
        DirTypes.INPUT_MANUAL.getBase().mkdirs();
        MockitoAnnotations.initMocks(this);
        when(scannerProvider.get()).thenReturn(new VideoFileScanner(packageProvider));
        when(inputSettings.getVideoExtensions()).thenReturn(Arrays.asList("mp4", "mkv"));
        when(packageProvider.get()).thenReturn(videoPackage1, videoPackage2);

        subject = createSubject();
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(TestUtility.getTestDir());
    }

    private ScanManualInputDirState createSubject() {
        return new ScanManualInputDirState(context, inputSettings, scannerProvider);
    }

    @Test
    public void testEnterState_ShouldFindAllFiles() throws IOException {
        File level1File = new File(DirTypes.INPUT_MANUAL.getBase(), "video1.mp4");
        File level2File = new File(DirTypes.INPUT_MANUAL.getBase(), "template1/video2.mkv");
        File level3File = new File(DirTypes.INPUT_MANUAL.getBase(), "template2/subdir/video3.mp4");

        AtomicBoolean called = new AtomicBoolean();
        subject.bindNextStateToTrigger(null, GenericCollectionTrigger.class, trigger -> {

            assertThat(trigger.getPayload().size(), equalTo(2));

            called.set(true);
            return null;
        });

        FileUtils.touch(level1File);
        FileUtils.touch(level2File);
        FileUtils.touch(level3File);

        subject.enter();
        assertThat(called.get(), equalTo(true));
    }

    @Test
    public void testEnterState_ShouldTriggerNoResult() throws IOException {
        File level1File = new File(DirTypes.INPUT_MANUAL.getBase(), "ignored.mp4");
        File level2File = new File(DirTypes.INPUT_MANUAL.getBase(), "template1/video2.nfo");
        File level3File = new File(DirTypes.INPUT_MANUAL.getBase(), "template2/subdir/video3.srt");

        AtomicBoolean called = new AtomicBoolean();
        subject.bindNextStateToTrigger(null, NoResultTrigger.class, trigger -> {
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
