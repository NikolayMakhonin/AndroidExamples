package nikolay_makhonin.android.views.markerline;

import android.graphics.Paint;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.DoubleStringConverter;
import nikolay_makhonin.utils.DrawStringUtils;

public class MarkerNumberText extends MarkerTextFixDelta {
    protected final DoubleStringConverter _stringConverter;

    public MarkerNumberText(IDrawState drawState, int drawOrder, double delta, DoubleStringConverter stringConverter,
        boolean drawBetweenMarkers)
    {
        super(drawState, drawOrder, delta, stringConverter, drawBetweenMarkers);
        _stringConverter = stringConverter;
    }
    
    @Override
    protected void onValueRangeChanged() {
        super.onValueRangeChanged();
        recalcMinWidths();
    }
    
    private final int MAX_NUMBER_COUNT = 20;
    protected int getMaxNumbersCount() {
        double[] valueRange = _drawState.drawData().getValueRange();
        double delta = getDelta();
        if (delta == 0) return MAX_NUMBER_COUNT;
        int maxNumbersCount = (int)Math.floor((valueRange[1] - valueRange[0]) / delta);
        if (maxNumbersCount > MAX_NUMBER_COUNT) return MAX_NUMBER_COUNT;
        if (maxNumbersCount <= 0) return 1;
        return maxNumbersCount;
    }
    
    @Override
    protected int calcWidth(int level) {
        double[] valueRange = _drawState.drawData().getValueRange();
        Paint paint = getPaint(getLevel());
        return DrawStringUtils
            .getTextMaxWidth(_stringConverter, "", valueRange[0], valueRange[1], getMaxNumbersCount(), paint,
                _drawState.drawSize().getDrawSize()[0]);
    }
}
