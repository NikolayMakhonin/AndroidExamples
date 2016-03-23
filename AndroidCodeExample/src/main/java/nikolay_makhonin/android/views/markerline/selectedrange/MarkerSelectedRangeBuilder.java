package nikolay_makhonin.android.views.markerline.selectedrange;

import nikolay_makhonin.android.views.markerline.MarkerBuilderBase;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IMarker;

public class MarkerSelectedRangeBuilder extends MarkerBuilderBase {
    private final int _pointDiameter;
    
    public MarkerSelectedRangeBuilder(IDrawState drawState, int pointDiameter) {
        this(drawState, 0, pointDiameter);
    }
    
    public MarkerSelectedRangeBuilder(IDrawState drawState, int drawOrder, int pointDiameter) {
        super(drawState, drawOrder, 1);
        _pointDiameter = pointDiameter;
    }

    @Override
    protected IMarker createMarker(int level) {
        return new MarkerSelectedRange(_drawState, -1, _pointDiameter);
    }

    @Override
    protected int calcMinLevel() {
        return 0;
    }    
}
