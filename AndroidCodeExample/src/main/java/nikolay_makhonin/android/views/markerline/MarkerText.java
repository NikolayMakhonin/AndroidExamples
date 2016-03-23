package nikolay_makhonin.android.views.markerline;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.contracts.IParamsChangedStringConverter;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.IEventListener;

public abstract class MarkerText extends MarkerTextBase {
    protected final IParamsChangedStringConverter _stringConverter;

    public MarkerText(IDrawState drawState, int drawOrder, IParamsChangedStringConverter stringConverter,
        boolean drawBetweenMarkers)
    {
        super(drawState, drawOrder, drawBetweenMarkers);
        _stringConverter = stringConverter;
        if (_stringConverter != null)
            _stringConverter.ParamsChanged().add(onStringConverterChanged_listener);
    }

    IEventListener<EventArgs> onStringConverterChanged_listener = new IEventListener<EventArgs>() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            onStringConverterChanged(o, e);
            return true;
        }
    };

    protected void onStringConverterChanged(Object o, EventArgs e) {
        recalcMinWidths();
    }
}
