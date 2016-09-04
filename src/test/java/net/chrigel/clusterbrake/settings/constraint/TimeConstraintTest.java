package net.chrigel.clusterbrake.settings.constraint;

import java.time.LocalTime;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimeConstraintTest {

    private TimeConstraint subject;

    @Test
    public void testAccept_ShouldReturnTrue_IfTimeIsBetweenBeginAndStop() {
        subject = new TimeConstraint("03:00", "12:00");

        subject.setCurrentTime(LocalTime.of(5, 0));

        assertThat(subject.accept(null), equalTo(true));
    }

    @Test
    public void testAccept_ShouldReturnFalse_IfTimeIsBeforeBeginAndStop() {
        subject = new TimeConstraint("03:00", "12:00");

        subject.setCurrentTime(LocalTime.of(1, 0));

        assertThat(subject.accept(null), equalTo(false));
    }

    @Test
    public void testAccept_ShouldReturnFalse_IfTimeIsAfterBeginAndStop() {
        subject = new TimeConstraint("03:00", "12:00");

        subject.setCurrentTime(LocalTime.of(12, 30));

        assertThat(subject.accept(null), equalTo(false));
    }

    @Test
    public void testAccept_ShouldReturnTrue_IfTimeIsAfterBeginAndAfterStop() {
        subject = new TimeConstraint("17:00", "03:00");

        subject.setCurrentTime(LocalTime.of(21, 0));

        assertThat(subject.accept(null), equalTo(true));
    }

    @Test
    public void testAccept_ShouldReturnTrue_IfTimeIsAfterBeginAndBeforeStop() {
        subject = new TimeConstraint("17:00", "03:00");

        subject.setCurrentTime(LocalTime.of(1, 0));

        assertThat(subject.accept(null), equalTo(true));
    }

    @Test
    public void testAccept_ShouldReturnFalse_IfTimeIsBeforeBeginAndAfterStop() {
        subject = new TimeConstraint("17:00", "03:00");

        subject.setCurrentTime(LocalTime.of(5, 0));

        assertThat(subject.accept(null), equalTo(false));
    }

    @Test
    public void testAccept_ShouldReturnTrue_IfTimeIsNotSpecified() {
        subject = new TimeConstraint("-1", "03:00");

        subject.setCurrentTime(LocalTime.of(1, 0));

        assertThat(subject.accept(null), equalTo(true));
    }

    @Test
    public void testAccept_ShouldReturnTrue_IfTimeAreSame() {
        subject = new TimeConstraint("00:00", "00:00");

        subject.setCurrentTime(LocalTime.of(1, 0));

        assertThat(subject.accept(null), equalTo(true));
    }

}
