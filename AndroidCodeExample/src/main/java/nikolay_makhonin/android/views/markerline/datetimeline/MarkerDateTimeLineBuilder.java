package nikolay_makhonin.android.views.markerline.datetimeline;

import nikolay_makhonin.android.views.markerline.MarkerBuilderMultLevelBase;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;

public class MarkerDateTimeLineBuilder extends MarkerBuilderMultLevelBase<MarkerDateTimeLine> {
    private final int _calendarField;
    private final int _middleSpaceHeight;
    
    public MarkerDateTimeLineBuilder(IDrawState drawState, int calendarField, double[] coefs, int delta0, int maxDeltaField, int middleSpaceHeight) {
        this(drawState, 0, calendarField, coefs, delta0, maxDeltaField, middleSpaceHeight);
    }
    
    public MarkerDateTimeLineBuilder(IDrawState drawState, int drawOrder, int calendarField, double[] coefs, int delta0, int maxDeltaField, int middleSpaceHeight) {
        super(drawState, drawOrder, MarkerDateTimeText.getMinDeltaSeconds(calendarField) * delta0, coefs, MarkerDateTimeText.getMinDeltaSeconds(calendarField), MarkerDateTimeText.getMaxDeltaSeconds(calendarField) * maxDeltaField);
        _middleSpaceHeight = middleSpaceHeight;
        _calendarField = calendarField;
    }

    @Override
    protected MarkerDateTimeLine createMarker(double delta) {
        return new MarkerDateTimeLine(_drawState, _drawOrder, _calendarField, (int)Math.round(delta / _minDelta), _middleSpaceHeight);
    }
}
