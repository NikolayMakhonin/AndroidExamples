package nikolay_makhonin.android.views.markerline;

import android.graphics.Paint;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.DoubleStringConverter;
import nikolay_makhonin.utils.DrawStringUtils;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.IEventListener;

public class MarkerTextNumberConverter extends MarkerTextConverter {
    protected DoubleStringConverter _doubleStringConverter;
    private MarkerTextNumberConverter _this = this;

    public MarkerTextNumberConverter(IDrawState drawState, DoubleStringConverter doubleStringConverter) {
        super(drawState);
        _doubleStringConverter = doubleStringConverter;
        if (_doubleStringConverter != null) _doubleStringConverter.ParamsChanged().add(onStringConverterChanged_listener);
    }

    IEventListener<EventArgs> onStringConverterChanged_listener = new IEventListener<EventArgs>() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            _paramsChanged.invoke(_this, EventArgs.Empty);
            return true;
        }
    };

    private final int MAX_NUMBER_COUNT = 20;

    protected int getMaxNumbersCount(double delta) {
        double[] valueRange = _drawState.drawData().getValueRange();
        if (delta == 0)
            return MAX_NUMBER_COUNT;
        int maxNumbersCount = (int) Math.floor((valueRange[1] - valueRange[0]) / delta);
        if (maxNumbersCount > MAX_NUMBER_COUNT)
            return MAX_NUMBER_COUNT;
        return maxNumbersCount;
    }

    @Override
    public int calcWidth(Paint paint, double delta) {
        double[] valueRange = _drawState.drawData().getValueRange();
        return DrawStringUtils
            .getTextMaxWidth(_doubleStringConverter, "", valueRange[0], valueRange[1], getMaxNumbersCount(delta), paint,
                _drawState.drawSize().getDrawSize()[0]);
    }

    @Override
    protected String valueToStringPrivate(double value) {
        return _doubleStringConverter.ConvertToString(value);
    }
    
    
}
