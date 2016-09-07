package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Provider;
import java.io.File;
import java.io.IOException;
import net.chrigel.clusterbrake.TestUtility;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.workflow.manualauto.settings.WorkflowTemplateSettings;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class AutoParseOptionsFileStateIT {

    private AutoParseOptionsFileState subject;

    @Mock
    private StateContext context;
    @Mock
    private Provider<VideoOptionPackage> optionPackageProvider;
    @Mock
    private Provider<OptionsFileParser> optionParserProvider;
    @Mock
    private Provider<VideoPackage> videoPackageProvider;
    @Mock
    private VideoOptionPackage optionPackage;
    @Mock
    private OptionsFileParser parser;
    @Mock
    private VideoPackage videoPackage;
    @Mock
    private Video video;
    @Mock
    private WorkflowTemplateSettings settings;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        TestUtility.initDirs();
        TEST_BASE_DIR.mkdirs();
        DirTypes.TEMPLATE.getBase().mkdirs();

        when(video.getSourceFile()).thenReturn(new FileContainer(DirTypes.INPUT_AUTO, "test.mkv"));
        when(optionPackageProvider.get()).thenReturn(optionPackage);
        when(optionParserProvider.get()).thenReturn(parser);
        when(videoPackageProvider.get()).thenReturn(videoPackage);
    }

    private static final File TEST_BASE_DIR = TestUtility.getTestDir();

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(TestUtility.getTestDir());
    }

    private AutoParseOptionsFileState createSubject() {
        return new AutoParseOptionsFileState(context, settings, optionPackageProvider,
                optionParserProvider, videoPackageProvider);
    }

    @Test
    public void testSetVideoList() {
    }

    @Test
    public void testEnterState() {
    }

    @Test
    public void testExitState() {
    }

}
