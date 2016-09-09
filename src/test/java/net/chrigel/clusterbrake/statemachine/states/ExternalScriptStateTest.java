package net.chrigel.clusterbrake.statemachine.states;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import net.chrigel.clusterbrake.TestUtility;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.trigger.ScriptExecutedTrigger;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertThat;

public class ExternalScriptStateTest {

    private ExternalScriptState subject;

    @Mock
    private StateContext context;

    private final File baseDir = TestUtility.getTestDir();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        subject = createSubject();
    }

    @After
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(baseDir);
    }

    private ExternalScriptState createSubject() {
        return new ExternalScriptState(context);
    }

    @Test
    public void testEnterState_ShouldInvokeScript() throws IOException {

        File script = new File(baseDir, "script.cmd");
        FileUtils.writeLines(script, Arrays.asList("echo test"));

        AtomicBoolean isCalled = new AtomicBoolean();
        subject.bindNextStateToTrigger(null, ScriptExecutedTrigger.class, trigger -> {
            isCalled.set(true);
            assertThat(trigger.getPayload(), equalTo(0));
            return null;
        });

        subject.setScriptFile(script);
        subject.enter();
        assertThat(isCalled.get(), equalTo(true));
    }

    @Test
    public void testEnterState_ShouldIgnoreScript_IfFileNotExists() throws IOException {

        AtomicBoolean isCalled = new AtomicBoolean();
        subject.bindNextStateToTrigger(null, ScriptExecutedTrigger.class, trigger -> {
            isCalled.set(true);
            assertThat(trigger.getPayload(), equalTo(-1));
            return null;
        });

        subject.enter();
        assertThat(isCalled.get(), equalTo(true));
    }

}
