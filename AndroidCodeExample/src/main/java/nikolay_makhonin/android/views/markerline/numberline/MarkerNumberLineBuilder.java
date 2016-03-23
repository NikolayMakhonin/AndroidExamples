package nikolay_makhonin.android.views.markerline.numberline;

import nikolay_makhonin.android.views.markerline.MarkerBuilderMultLevelBase;
import nikolay_makhonin.android.views.markerline.MarkerLineFixDelta;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;

public class MarkerNumberLineBuilder extends MarkerBuilderMultLevelBase<MarkerLineFixDelta> {
    private final int _middleSpaceHeight;
    
    public MarkerNumberLineBuilder(IDrawState drawState, int middleSpaceHeight) {
        this(drawState, 0, middleSpaceHeight);
    }
    
    public MarkerNumberLineBuilder(IDrawState drawState, int drawOrder, int middleSpaceHeight) {
        super(drawState, drawOrder, 1.0, new double[] { 5.0, 2.0 }, -1, -1);
        _middleSpaceHeight = middleSpaceHeight;
    }

    @Override
    protected MarkerLineFixDelta createMarker(double delta) {
        return new MarkerLineFixDelta(_drawState, _drawOrder, delta, _middleSpaceHeight);
    }    
}
