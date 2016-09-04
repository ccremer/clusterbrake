package net.chrigel.clusterbrake.workflow.manualauto;

import com.google.inject.Inject;
import java.util.Arrays;
import java.util.List;
import net.chrigel.clusterbrake.media.VideoPackage;
import net.chrigel.clusterbrake.settings.constraint.Constraint;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.states.AbstractState;

/**
 *
 */
public class QueueSelectState
        extends AbstractState {

    private List<VideoPackage> videos;
    private List<Constraint> constraints;

    @Inject
    QueueSelectState(StateContext context) {
        super(context);
    }
    
    public void setVideoPackageList(List<VideoPackage> list) {
        this.videos = list;
    }
    
    public void setConstraints(Constraint... constraints) {
        this.constraints = Arrays.asList(constraints);
    }

    @Override
    protected void enterState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void exitState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
