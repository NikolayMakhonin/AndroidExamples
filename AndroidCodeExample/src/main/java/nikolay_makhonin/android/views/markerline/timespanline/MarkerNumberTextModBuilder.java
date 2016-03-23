package nikolay_makhonin.android.views.markerline.timespanline;

import nikolay_makhonin.android.views.markerline.MarkerBuilderMultLevelBase;
import nikolay_makhonin.android.views.markerline.MarkerNumberTextMod;
import nikolay_makhonin.android.views.markerline.MarkerTextFixDelta;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.DoubleStringConverter;

public class MarkerNumberTextModBuilder extends MarkerBuilderMultLevelBase<MarkerTextFixDelta> {
    private final DoubleStringConverter _stringConverter;
    private final String                _suffix;

    public MarkerNumberTextModBuilder(IDrawState drawState, DoubleStringConverter stringConverter, double[] coefs,
        double minDelta, double maxDelta, String suffix)
    {
        this(drawState, 0, stringConverter, coefs, minDelta, maxDelta, suffix);
    }

    public MarkerNumberTextModBuilder(IDrawState drawState, int drawOrder, DoubleStringConverter stringConverter, double[] coefs, double minDelta, double maxDelta, String suffix) {
        super(drawState, drawOrder, minDelta > 0 ? minDelta : (maxDelta > 0 ? maxDelta : 1), coefs, minDelta, maxDelta);
        _stringConverter = stringConverter;
        _suffix = suffix;
    }

    @Override
    protected MarkerTextFixDelta createMarker(double delta) {
        return new MarkerNumberTextMod(_drawState, _drawOrder, delta, _minDelta, _maxDelta, _stringConverter, _suffix, false);
    }    
    
    public int getMaxDrawHeight() {
        return getMarker0().getMaxDrawHeight();
    }    
}
