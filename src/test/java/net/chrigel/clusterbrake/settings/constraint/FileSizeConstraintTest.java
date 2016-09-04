package net.chrigel.clusterbrake.settings.constraint;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import net.chrigel.clusterbrake.media.Video;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class FileSizeConstraintTest {

    private FileSizeConstraint subject;
    
    @Mock
    private Video video;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
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

    @Test
    public void testAccept_ShouldReturnTrue_IfFileIsGreaterThanMinSize() throws IOException {
        subject = new FileSizeConstraint(2, 0, FileSizeConstraint.BYTES);
        File testFile = new File(TEST_BASE_DIR, "testFile");
        FileUtils.writeLines(testFile, Arrays.asList("line", "line", "line", "line"));
        when(video.getSourceFile()).thenReturn(testFile);
        
        assertThat(subject.accept(video), equalTo(true));
    }
    
    @Test
    public void testAccept_ShouldReturnTrue_IfFileIsSmallerThanMaxSize() throws IOException {
        subject = new FileSizeConstraint(0, 100, FileSizeConstraint.BYTES);
        File testFile = new File(TEST_BASE_DIR, "testFile");
        FileUtils.writeLines(testFile, Arrays.asList("line"));
        when(video.getSourceFile()).thenReturn(testFile);
        
        assertThat(subject.accept(video), equalTo(true));
    }
    
    @Test
    public void testAccept_ShouldReturnTrue_IfFileIsBetweenMaxAndMinSize() throws IOException {
        subject = new FileSizeConstraint(2, 100, FileSizeConstraint.BYTES);
        File testFile = new File(TEST_BASE_DIR, "testFile");
        FileUtils.writeLines(testFile, Arrays.asList("line"));
        when(video.getSourceFile()).thenReturn(testFile);
        
        assertThat(subject.accept(video), equalTo(true));
    }
    
    @Test
    public void testAccept_ShouldReturnFalse_IfFileIsSmallerThanMinSize() throws IOException {
        subject = new FileSizeConstraint(50, 0, FileSizeConstraint.BYTES);
        File testFile = new File(TEST_BASE_DIR, "testFile");
        FileUtils.writeLines(testFile, Arrays.asList("line"));
        when(video.getSourceFile()).thenReturn(testFile);
        
        assertThat(subject.accept(video), equalTo(false));
    }
    
    @Test
    public void testAccept_ShouldReturnFalse_IfFileIsGreaterThanMaxSize() throws IOException {
        subject = new FileSizeConstraint(10, 20, FileSizeConstraint.BYTES);
        File testFile = new File(TEST_BASE_DIR, "testFile");
        FileUtils.writeLines(testFile, Arrays.asList("line", "line", "line", "line"));
        when(video.getSourceFile()).thenReturn(testFile);
        
        assertThat(subject.accept(video), equalTo(false));
    }

}
