package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import net.chrigel.clusterbrake.TestUtility;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.GenericCollectionTrigger;
import net.chrigel.clusterbrake.transcode.TranscoderSettings;
import net.chrigel.clusterbrake.workflow.manualauto.settings.WorkflowTemplateSettings;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class AutoParseOptionsFileStateTest {

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
    private TranscoderSettings transcoderSettings;
    @Mock
    private WorkflowTemplateSettings templateSettings;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        TestUtility.initDirs();
        TEST_BASE_DIR.mkdirs();
        subject = createSubject();
        DirTypes.TEMPLATE.setDir(TestUtility.getTestDir());
        DirTypes.TEMPLATE.getBase().mkdirs();
        
        when(video.getSourceFile()).thenReturn(new FileContainer(DirTypes.INPUT_MANUAL, "test.mkv"));
        when(optionPackageProvider.get()).thenReturn(optionPackage);
        when(optionParserProvider.get()).thenReturn(parser);
        when(videoPackageProvider.get()).thenReturn(videoPackage);
        when(transcoderSettings.getOptionsFileExtension()).thenReturn("handbrake");
        when(templateSettings.getDefaultAutoTemplate()).thenReturn("auto");
    }

    private static final File TEST_BASE_DIR = TestUtility.getTestDir();

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(TestUtility.getTestDir());
    }

    private AutoParseOptionsFileState createSubject() {
        return new AutoParseOptionsFileState(context, templateSettings, optionPackageProvider,
                optionParserProvider, videoPackageProvider, transcoderSettings);
    }

    @Test
    public void testEnterState() throws Exception {
        AtomicBoolean isCalled = new AtomicBoolean();
        subject.bindNextStateToTrigger(null, GenericCollectionTrigger.class, trigger -> {
            isCalled.set(true);
            return null;
        });
        FileContainer optionContainer = new FileContainer(TestUtility.getTestDirType(), "auto.handbrake");

        List<Video> videoList = new LinkedList<>();
        videoList.add(video);
        optionContainer.getFullPath().createNewFile();
        subject.setVideoList(videoList);
        subject.enter();

        verify(optionParserProvider).get();
        verify(optionPackageProvider).get();
        verify(optionPackage).setOptions(any());
        verify(videoPackage).setSettings(optionPackage);
        verify(videoPackage).setVideo(video);
        verify(parser).parseFile(optionContainer.getFullPath());
        assertThat(isCalled.get(), equalTo(true));
    }
}
