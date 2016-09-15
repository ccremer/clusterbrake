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
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class ScanAutoInputDirStateIT {

    @Mock
    private StateContext context;
    @Mock
    private InputSettings inputSettings;
    @Mock
    private Provider<FileScanner<VideoPackage>> scannerProvider;
    @Mock
    private Provider<VideoPackage> packageProvider;
    @Mock
    private VideoPackage videoPackage;

    private ScanAutoInputDirState subject;

    @Before
    public void setup() {
        TestUtility.initDirs();
        DirTypes.INPUT_AUTO.getBase().mkdirs();
        MockitoAnnotations.initMocks(this);
        when(packageProvider.get()).thenReturn(videoPackage);
        when(scannerProvider.get()).thenReturn(new VideoFileScanner(packageProvider));
        when(inputSettings.getVideoExtensions()).thenReturn(Arrays.asList("mp4", "mkv"));
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(TestUtility.getTestDir());
    }

    private ScanAutoInputDirState createSubject() {
        return new ScanAutoInputDirState(context, inputSettings, scannerProvider);
    }

    @Test
    public void testEnterState_ShouldFindFiles() throws IOException {
        subject = createSubject();

        File level1File = new File(DirTypes.INPUT_AUTO.getBase(), "video1.mp4");
        File level2File = new File(DirTypes.INPUT_AUTO.getBase(), "movie2/video2.mkv");
        File level3File = new File(DirTypes.INPUT_AUTO.getBase(), "movie2/subdir/video3.mp4");

        AtomicBoolean called = new AtomicBoolean();
        subject.bindNextStateToTrigger(null, GenericCollectionTrigger.class, trigger -> {

            assertThat(trigger.getPayload().size(), equalTo(3));

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
    public void testEnterState_ShouldShouldTriggerWithEmptyList() throws IOException {
        subject = createSubject();

        File level1File = new File(DirTypes.INPUT_AUTO.getBase(), "video1.nfo");
        File level2File = new File(DirTypes.INPUT_AUTO.getBase(), "template1/video2.srt");
        File level3File = new File(DirTypes.INPUT_AUTO.getBase(), "template2/subdir/video3.other");

        AtomicBoolean called = new AtomicBoolean();
        subject.bindNextStateToTrigger(null, GenericCollectionTrigger.class, trigger -> {

            assertThat(trigger.getPayload().size(), equalTo(0));

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
