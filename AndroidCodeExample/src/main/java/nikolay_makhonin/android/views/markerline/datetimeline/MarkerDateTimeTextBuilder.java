package nikolay_makhonin.android.views.markerline.datetimeline;

import nikolay_makhonin.android.views.markerline.MarkerBuilderMultLevelBase;
import nikolay_makhonin.android.views.markerline.MarkerTextConverter;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;

public class MarkerDateTimeTextBuilder extends MarkerBuilderMultLevelBase<MarkerDateTimeText> {
    private final MarkerTextConverter _stringConverter;
    private final boolean             _drawBetweenMarkers;
    private final int                 _calendarField;

    public MarkerDateTimeTextBuilder(IDrawState drawState, String dateTimeFormat, int calendarField, double[] coefs,
        int delta0, int maxDeltaField, boolean drawBetweenMarkers)
    {
        this(drawState, 0, dateTimeFormat, calendarField, coefs, delta0, maxDeltaField, drawBetweenMarkers);
    }

    public MarkerDateTimeTextBuilder(IDrawState drawState, int drawOrder, String dateTimeFormat, int calendarField, double[] coefs, int delta0, int maxDeltaField, boolean drawBetweenMarkers) {
        this(drawState, drawOrder, new MarkerTextDateTimeConverter(drawState, dateTimeFormat), calendarField, coefs, delta0, maxDeltaField, drawBetweenMarkers);
    }

    public MarkerDateTimeTextBuilder(IDrawState drawState, MarkerTextConverter stringConverter, int calendarField, double[] coefs, int delta0, int maxDeltaField, boolean drawBetweenMarkers) {
        this(drawState, 0, stringConverter, calendarField, coefs, delta0, maxDeltaField, drawBetweenMarkers);
    }
    
    public MarkerDateTimeTextBuilder(IDrawState drawState, int drawOrder, MarkerTextConverter stringConverter, int calendarField, double[] coefs, int delta0, int maxDeltaField, boolean drawBetweenMarkers) {
        super(drawState, drawOrder, MarkerDateTimeText.getMinDeltaSeconds(calendarField) * delta0, coefs, MarkerDateTimeText.getMinDeltaSeconds(calendarField), MarkerDateTimeText.getMaxDeltaSeconds(calendarField) * maxDeltaField);
        _stringConverter = stringConverter;
        _drawBetweenMarkers = drawBetweenMarkers;
        _calendarField = calendarField;
    }

    @Override
    protected MarkerDateTimeText createMarker(double delta) {
        return new MarkerDateTimeText(_drawState, _drawOrder, _calendarField, (int)Math.round(delta / _minDelta), _stringConverter, _drawBetweenMarkers);
    }
    
    public int getMaxDrawHeight() {
        return getMarker0().getMaxDrawHeight();
    }
}
