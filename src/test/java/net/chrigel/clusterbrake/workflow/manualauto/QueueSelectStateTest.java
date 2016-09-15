package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Provider;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import net.chrigel.clusterbrake.TestUtility;
import net.chrigel.clusterbrake.media.FileContainer;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.settings.Job;
import net.chrigel.clusterbrake.settings.JobSettings;
import net.chrigel.clusterbrake.settings.NodeSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.QueueResultTrigger;
import net.chrigel.clusterbrake.workflow.manualauto.settings.OutputSettings;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class QueueSelectStateTest {

    @Mock
    private StateContext context;
    private JobSettings jobSettings;
    @Mock
    private NodeSettings nodeSettings;
    private JobSettings finishedJobs;
    @Mock
    private Provider<Job> queueProvider;
    @Mock
    private OutputSettings outputSettings;
    @Mock
    private Job job1;
    @Mock
    private Job job2;
    @Mock
    private Job job3;
    @Mock
    private VideoPackage videoPackage1;
    @Mock
    private VideoPackage videoPackage2;
    @Mock
    private VideoPackage videoPackage3;

    private QueueSelectState subject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestUtility.initDirs();
        jobSettings = new JobSettingsStub();
        finishedJobs = new JobSettingsStub();
        subject = createSubject();
        when(nodeSettings.getNodeID()).thenReturn("localhost");
        when(videoPackage1.getSourceFile()).thenReturn(
                new FileContainer(DirTypes.INPUT_MANUAL, new File("video1.mkv")));
        when(videoPackage2.getSourceFile()).thenReturn(
                new FileContainer(DirTypes.INPUT_MANUAL, new File("video2.mkv")));
        when(videoPackage3.getSourceFile()).thenReturn(
                new FileContainer(DirTypes.INPUT_AUTO, new File("video2.mkv")));

        when(job1.getVideoPackage()).thenReturn(videoPackage1);
        when(job2.getVideoPackage()).thenReturn(videoPackage2);
        when(job3.getVideoPackage()).thenReturn(videoPackage3);
    }

    private QueueSelectState createSubject() {
        return new QueueSelectState(context, jobSettings, nodeSettings, finishedJobs, queueProvider, outputSettings);
    }

    @Test
    public void testClearQueue_ShouldRemoveEntriesForSameNode() {
        jobSettings.getJobs().add(job1);
        jobSettings.getJobs().add(job2);
        when(job1.getNodeID()).thenReturn("localhost");
        subject.clearQueue();

        assertThat(jobSettings.getJobs(), allOf(hasItem(job2), not(hasItem(job1))));
    }

    @Test
    public void testEnterState_ShouldSkipEntries_IfTheyAreInQueue() {

        AtomicBoolean isCalled = new AtomicBoolean();

        /**
         * Job 1 in use by another host, expect job2
         */
        jobSettings.getJobs().add(job1);
        jobSettings.getJobs().add(job1);

        List<VideoPackage> videoList = new LinkedList<>();
        videoList.add(videoPackage1);
        videoList.add(videoPackage2);
        videoList.add(videoPackage3);

        when(job2.getNodeID()).thenReturn("localhost");
        when(queueProvider.get()).thenReturn(job2);

        subject.bindNextStateToTrigger(null, QueueResultTrigger.class, trigger -> {
            isCalled.set(true);
            Job job = trigger.getPayload();
            verify(job).setVideoPackage(videoPackage2);
            assertThat(job, equalTo(job2));
            assertThat(jobSettings.getJobs(), allOf(hasItem(job1), hasItem(job2)));
            return null;
        });

        subject.setVideoPackageList(videoList);
        subject.enter();

        assertThat(isCalled.get(), equalTo(true));
    }

    @Test
    public void testEnterState_ShouldSkipEntries_IfTheyAreAlreadyConverted() {

        AtomicBoolean isCalled = new AtomicBoolean();

        finishedJobs.getJobs().add(job1);

        List<VideoPackage> videoList = new LinkedList<>();
        videoList.add(videoPackage1);
        videoList.add(videoPackage2);
        videoList.add(videoPackage3);

        when(queueProvider.get()).thenReturn(job2);

        subject.bindNextStateToTrigger(null, QueueResultTrigger.class, trigger -> {
            isCalled.set(true);
            Job job = trigger.getPayload();
            verify(job).setVideoPackage(videoPackage2);
            verify(videoPackage1, atLeastOnce()).getSourceFile();
            verify(job1, atLeastOnce()).getVideoPackage();
            assertThat(job, equalTo(job2));
            assertThat(jobSettings.getJobs(), allOf(hasItem(job2)));
            return null;
        });

        subject.setVideoPackageList(videoList);
        subject.enter();

        assertThat(isCalled.get(), equalTo(true));
    }

    @Test
    public void testEnterState_ShouldPriorizeManualInput() {

        AtomicBoolean isCalled = new AtomicBoolean();

        List<VideoPackage> videoList = new LinkedList<>();
        videoList.add(videoPackage1);
        videoList.add(videoPackage2);
        videoList.add(videoPackage3);

        when(queueProvider.get()).thenReturn(job1);

        subject.bindNextStateToTrigger(null, QueueResultTrigger.class, trigger -> {
            isCalled.set(true);
            Job job = trigger.getPayload();
            verify(job).setVideoPackage(videoPackage1);
            verify(videoPackage1, atLeastOnce()).getSourceFile();
            assertThat(job, equalTo(job1));
            assertThat(jobSettings.getJobs(), allOf(hasItem(job1)));
            return null;
        });

        subject.setVideoPackageList(videoList);
        subject.enter();

        assertThat(isCalled.get(), equalTo(true));
    }

    @Test
    public void testEnterState_ShouldSelectAutoInput_IfManualInputIsFinished() {

        AtomicBoolean isCalled = new AtomicBoolean();

        finishedJobs.getJobs().add(job1);

        List<VideoPackage> videoList = new LinkedList<>();
        videoList.add(videoPackage1);
 //       videoList.add(videoPackage2);
        videoList.add(videoPackage3);

        when(queueProvider.get()).thenReturn(job3);

        subject.bindNextStateToTrigger(null, QueueResultTrigger.class, trigger -> {
            isCalled.set(true);
            Job job = trigger.getPayload();
            verify(job).setVideoPackage(videoPackage3);
            verify(videoPackage1, atLeastOnce()).getSourceFile();
            assertThat(job, equalTo(job3));
            assertThat(jobSettings.getJobs(), allOf(hasItem(job3)));
            return null;
        });

        subject.setVideoPackageList(videoList);
        subject.enter();

        assertThat(isCalled.get(), equalTo(true));
    }

    class JobSettingsStub implements JobSettings {

        private List<Job> jobs = new LinkedList<>();

        @Override
        public List<Job> getJobs() {
            return jobs;
        }

        @Override
        public void setJobs(List<Job> jobs) {
            this.jobs = jobs;
        }

    }

}
