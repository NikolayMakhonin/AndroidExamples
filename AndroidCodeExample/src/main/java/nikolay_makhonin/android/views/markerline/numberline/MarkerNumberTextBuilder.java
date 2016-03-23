package nikolay_makhonin.android.views.markerline.numberline;

import nikolay_makhonin.android.views.markerline.MarkerBuilderMultLevelBase;
import nikolay_makhonin.android.views.markerline.MarkerNumberText;
import nikolay_makhonin.android.views.markerline.MarkerTextFixDelta;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.DoubleStringConverter;

public class MarkerNumberTextBuilder extends MarkerBuilderMultLevelBase<MarkerTextFixDelta> {
    private final DoubleStringConverter _stringConverter;

    public MarkerNumberTextBuilder(IDrawState drawState, DoubleStringConverter stringConverter) {
        this(drawState, 0, stringConverter);
    }

    public MarkerNumberTextBuilder(IDrawState drawState, int drawOrder, DoubleStringConverter stringConverter) {
        super(drawState, drawOrder, 1.0, new double[] { 5.0, 2.0 }, -1, -1);
        _stringConverter = stringConverter;
    }

    @Override
    protected MarkerTextFixDelta createMarker(double delta) {
        return new MarkerNumberText(_drawState, _drawOrder, delta, _stringConverter, false);
    }    
    
    public int getMaxDrawHeight() {
        return getMarker0().getMaxDrawHeight();
    }
}
