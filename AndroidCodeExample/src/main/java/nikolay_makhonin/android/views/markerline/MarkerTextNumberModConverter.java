package nikolay_makhonin.android.views.markerline;

import android.graphics.Paint;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.DoubleStringConverter;
import nikolay_makhonin.utils.DrawStringUtils;
import nikolay_makhonin.utils.MathUtils;

public class MarkerTextNumberModConverter extends MarkerTextNumberConverter {
    private final double _minDelta;
    private final double _maxDelta;
    private final boolean _modMode;

    public MarkerTextNumberModConverter(IDrawState drawState, DoubleStringConverter doubleStringConverter, double minDelta, double maxDelta) {
        super(drawState, doubleStringConverter);
        _minDelta = minDelta;
        _maxDelta = maxDelta;
        _modMode = _maxDelta > 0 && _maxDelta != Double.MAX_VALUE;
    }

    @Override
    public int calcWidth(Paint paint, double delta) {
        double[] valueRange = _drawState.drawData().getValueRange();
        double max, min;
        if (!_modMode || MathUtils.getIntegerValue(valueRange[0] / _maxDelta) == MathUtils.getIntegerValue(valueRange[1] / _maxDelta)) {
            min = (_minDelta > 0) ? valueRange[0] / _minDelta : valueRange[0];
            max = (_minDelta > 0) ? valueRange[1] / _minDelta : valueRange[1];
        } else {
            max = _maxDelta - _minDelta;
            min = _minDelta;
        }
        return DrawStringUtils.getTextMaxWidth(_doubleStringConverter, "", min, max, getMaxNumbersCount(delta), paint,
            _drawState.drawSize().getDrawSize()[0]);
    }

    @Override
    protected String valueToStringPrivate(double value) {
        if (_modMode) value = value - MathUtils.getIntegerValue(value / _maxDelta) * _maxDelta; 
        if (_minDelta > 0) value /= _minDelta;
        return super.valueToStringPrivate(value);
    }    
}
