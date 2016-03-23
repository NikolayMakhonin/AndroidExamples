package nikolay_makhonin.android.views.markerline;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;

public class MarkerLineFixDelta extends MarkerLine {
    private double _delta;
    
    public MarkerLineFixDelta(IDrawState drawState, int drawOrder, double delta, int middleSpaceHeight) {
        super(drawState, drawOrder, middleSpaceHeight);
        _delta = delta;
    }

    @Override
    protected double calcDelta() {
        return _delta;
    }
}
