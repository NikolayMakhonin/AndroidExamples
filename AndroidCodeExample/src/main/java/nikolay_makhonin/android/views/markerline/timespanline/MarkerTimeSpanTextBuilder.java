package nikolay_makhonin.android.views.markerline.timespanline;

import nikolay_makhonin.android.views.markerline.MarkerBuilderMultLevelBase;
import nikolay_makhonin.android.views.markerline.MarkerTextConverter;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.datetime.TimeSpanPart;

public class MarkerTimeSpanTextBuilder extends MarkerBuilderMultLevelBase<MarkerTimeSpanText> {
    private final MarkerTextConverter _stringConverter;
    private final boolean             _drawBetweenMarkers;
    private final TimeSpanPart        _deltaPart;

    public MarkerTimeSpanTextBuilder(IDrawState drawState, String[] patterns, TimeSpanPart[] patternParts,
        TimeSpanPart deltaPart, double[] coefs, int delta0, int maxDeltaField, boolean drawBetweenMarkers)
    {
        this(drawState, 0, patterns, patternParts, deltaPart, coefs, delta0, maxDeltaField, drawBetweenMarkers);
    }

    public MarkerTimeSpanTextBuilder(IDrawState drawState, int drawOrder, String[] patterns, TimeSpanPart[] patternParts, TimeSpanPart deltaPart, double[] coefs, int delta0, int maxDeltaField, boolean drawBetweenMarkers) {
        this(drawState, drawOrder, new MarkerTextTimeSpanConverter(drawState, patterns, patternParts), deltaPart, coefs, delta0, maxDeltaField, drawBetweenMarkers);
    }

    public MarkerTimeSpanTextBuilder(IDrawState drawState, MarkerTextConverter stringConverter, TimeSpanPart deltaPart, double[] coefs, int delta0, int maxDeltaField, boolean drawBetweenMarkers) {
        this(drawState, 0, stringConverter, deltaPart, coefs, delta0, maxDeltaField, drawBetweenMarkers);
    }
    
    public MarkerTimeSpanTextBuilder(IDrawState drawState, int drawOrder, MarkerTextConverter stringConverter, TimeSpanPart deltaPart, double[] coefs, int delta0, int maxDeltaField, boolean drawBetweenMarkers) {
        super(drawState, drawOrder, MarkerTimeSpanText.getDeltaSeconds(deltaPart) * delta0, coefs, MarkerTimeSpanText.getDeltaSeconds(deltaPart), MarkerTimeSpanText.getDeltaSeconds(deltaPart) * maxDeltaField);
        _stringConverter = stringConverter;
        _drawBetweenMarkers = drawBetweenMarkers;
        _deltaPart = deltaPart;
    }

    @Override
    protected MarkerTimeSpanText createMarker(double delta) {
        return new MarkerTimeSpanText(_drawState, _drawOrder, _deltaPart, (int)Math.round(delta / _minDelta), _stringConverter, _drawBetweenMarkers);
    }
    
    public int getMaxDrawHeight() {
        return getMarker0().getMaxDrawHeight();
    }
}
