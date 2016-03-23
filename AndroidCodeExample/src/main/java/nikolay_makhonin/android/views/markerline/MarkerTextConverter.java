package nikolay_makhonin.android.views.markerline;

import android.graphics.Paint;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.contracts.IParamsChangedStringConverter;
import nikolay_makhonin.utils.events.EventHandler;
import nikolay_makhonin.utils.events.IEventHandler;

public abstract class MarkerTextConverter implements IParamsChangedStringConverter {
    protected final IDrawState _drawState;

    public MarkerTextConverter(IDrawState drawState) {
        _drawState = drawState;
    }

    @Override
    public String ConvertToString(Object value) {
        return valueToStringPrivate((Double) value);
    }

    public abstract int calcWidth(Paint paint, double delta);

    protected abstract String valueToStringPrivate(double value);

    protected IEventHandler _paramsChanged = new EventHandler();

    @Override
    public IEventHandler ParamsChanged() {
        return _paramsChanged;
    }
}
