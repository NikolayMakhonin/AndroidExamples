package nikolay_makhonin.android.views.markerline.timespanline;

import nikolay_makhonin.android.views.markerline.MarkerBuilderMultLevelBase;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.datetime.TimeSpanPart;

public class MarkerTimeSpanLineBuilder extends MarkerBuilderMultLevelBase<MarkerTimeSpanLine> {
    private final TimeSpanPart _deltaPart;
    private final int          _middleSpaceHeight;

    public MarkerTimeSpanLineBuilder(IDrawState drawState, TimeSpanPart deltaPart, double[] coefs, int delta0,
        int maxDeltaField, int middleSpaceHeight)
    {
        this(drawState, 0, deltaPart, coefs, delta0, maxDeltaField, middleSpaceHeight);
    }

    public MarkerTimeSpanLineBuilder(IDrawState drawState, int drawOrder, TimeSpanPart deltaPart, double[] coefs,
        int delta0, int maxDeltaField, int middleSpaceHeight) {
        super(drawState, drawOrder, MarkerTimeSpanText.getDeltaSeconds(deltaPart) * delta0, coefs, MarkerTimeSpanText
            .getDeltaSeconds(deltaPart), MarkerTimeSpanText.getDeltaSeconds(deltaPart) * maxDeltaField);
        _middleSpaceHeight = middleSpaceHeight;
        _deltaPart = deltaPart;
    }
    
    @Override
    protected MarkerTimeSpanLine createMarker(double delta) {
        return new MarkerTimeSpanLine(_drawState, _drawOrder, _deltaPart, (int) Math.round(delta / _minDelta), _middleSpaceHeight);
    }
}
