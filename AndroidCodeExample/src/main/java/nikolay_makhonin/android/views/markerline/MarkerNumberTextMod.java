package nikolay_makhonin.android.views.markerline;

import android.graphics.Paint;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.DoubleStringConverter;
import nikolay_makhonin.utils.DrawStringUtils;
import nikolay_makhonin.utils.MathUtils;

public class MarkerNumberTextMod extends MarkerNumberText {
    private final double _minDelta;
    private final double _maxDelta;
    private final String _numderSuffix;
    private final boolean _modMode;
    
    public MarkerNumberTextMod(IDrawState drawState, int drawOrder, double delta, double minDelta, double maxDelta, DoubleStringConverter stringConverter,
        String numderSuffix, boolean drawBetweenMarkers) {
        super(drawState, drawOrder, delta, stringConverter, drawBetweenMarkers);
        _minDelta = minDelta;
        _maxDelta = maxDelta;
        _numderSuffix = numderSuffix;
        _modMode = _maxDelta > 0 && _maxDelta != Double.MAX_VALUE;
    }
        
    @Override
    protected int calcWidth(int level) {
        double[] valueRange = _drawState.drawData().getValueRange();
        double max, min;
        if (!_modMode || MathUtils.getIntegerValue(valueRange[0] / _maxDelta) == MathUtils.getIntegerValue(valueRange[1] / _maxDelta)) {
            min = (_minDelta > 0) ? valueRange[0] / _minDelta : valueRange[0];
            max = (_minDelta > 0) ? valueRange[1] / _minDelta : valueRange[1];
        } else {
            max = _maxDelta - _minDelta;
            min = _minDelta;
        }
        Paint paint = getPaint(getLevel());
        return DrawStringUtils.getTextMaxWidth(_stringConverter, _numderSuffix, min, max, getMaxNumbersCount(), paint,
            _drawState.drawSize().getDrawSize()[0]);
    }
    
    @Override
    protected String valueToString(double value) {
        if (_modMode) value = value - MathUtils.getIntegerValue(value / _maxDelta) * _maxDelta; 
        if (_minDelta > 0) value /= _minDelta;
        return super.valueToString(value) + _numderSuffix;
    }
}
