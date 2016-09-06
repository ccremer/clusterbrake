package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import net.chrigel.clusterbrake.media.OptionsFileParser;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.settings.TemplateSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.GenericCollectionTrigger;
import net.chrigel.clusterbrake.workflow.manualauto.settings.OptionDirVideoPair;
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

public class ParseOptionsFileStateTest {

    @Mock
    private ManualParseOptionsFileState subject;

    @Mock
    private StateContext context;
    @Mock
    private TemplateSettings templateSettings;
    @Mock
    private WorkflowTemplateSettings workflowTemplateSettings;
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
    private File templateDir;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        TEST_BASE_DIR.mkdir();
        templateDir = new File(TEST_BASE_DIR, "templates");
        templateDir.mkdir();
        when(templateSettings.getTemplateDir()).thenReturn(templateDir);
        when(workflowTemplateSettings.getDefaultManualTemplate())
                .thenReturn(new File(templateDir, "manual.conf"));

        when(optionPackageProvider.get()).thenReturn(optionPackage);
        when(optionParserProvider.get()).thenReturn(parser);
        when(videoPackageProvider.get()).thenReturn(videoPackage);
    }

    private static final File TEST_BASE_DIR = new File("test");

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(TEST_BASE_DIR);
    }

    private ManualParseOptionsFileState createSubject() {
        return new ManualParseOptionsFileState(context, templateSettings, workflowTemplateSettings,
                optionPackageProvider, optionParserProvider, videoPackageProvider);
    }

    @Test
    public void testEnterState_ShouldCorrectlyParseTemplateFile_IfItExists() throws Exception {
        subject = createSubject();
        AtomicBoolean isCalled = new AtomicBoolean();
        subject.bindNextStateToTrigger(null, GenericCollectionTrigger.class, trigger -> {
            isCalled.set(true);
            return null;
        });
        workflowTemplateSettings.getDefaultManualTemplate().createNewFile();
        File optionDir = new File(TEST_BASE_DIR, "option1d");
        File optionFile = new File(templateDir, "option1d.conf");

        optionDir.mkdir();
        optionFile.createNewFile();

        List<OptionDirVideoPair> pairList = new LinkedList<>();
        List<Video> videoList = new LinkedList<>(Arrays.asList(video));
        pairList.add(new OptionDirVideoPair(optionDir, videoList));

        subject.setOptionDirList(pairList);
        subject.enter();

        verify(optionParserProvider).get();
        verify(optionPackageProvider).get();
        verify(optionPackage).setOptionFile(optionFile);
        verify(optionPackage).setOptions(any());
        verify(videoPackage).setSettings(optionPackage);
        verify(videoPackage).setVideo(video);
        verify(parser).parseFile(optionFile);
        assertThat(isCalled.get(), equalTo(true));
    }

    @Test
    public void testEnterState_ShouldCorrectlyParseDefaultFile() throws Exception {
        subject = createSubject();
        AtomicBoolean isCalled = new AtomicBoolean();
        subject.bindNextStateToTrigger(null, GenericCollectionTrigger.class, trigger -> {
            isCalled.set(true);
            return null;
        });
        workflowTemplateSettings.getDefaultManualTemplate().createNewFile();
        File optionDir = new File(TEST_BASE_DIR, "option1d");

        optionDir.mkdir();

        List<OptionDirVideoPair> pairList = new LinkedList<>();
        List<Video> videoList = new LinkedList<>(Arrays.asList(video));
        pairList.add(new OptionDirVideoPair(optionDir, videoList));

        subject.setOptionDirList(pairList);
        subject.enter();

        verify(optionParserProvider).get();
        verify(optionPackageProvider).get();
        verify(optionPackage).setOptionFile(workflowTemplateSettings.getDefaultManualTemplate());
        verify(optionPackage).setOptions(any());
        verify(videoPackage).setSettings(optionPackage);
        verify(videoPackage).setVideo(video);
        verify(parser).parseFile(workflowTemplateSettings.getDefaultManualTemplate());
        assertThat(isCalled.get(), equalTo(true));
    }
}
