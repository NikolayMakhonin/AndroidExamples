package nikolay_makhonin.android.views.markerline;

import nikolay_makhonin.android.views.markerline.contracts.DrawSizeChangedEventArgs;
import nikolay_makhonin.android.views.markerline.contracts.IDrawDataState;
import nikolay_makhonin.android.views.markerline.contracts.IDrawSizeState;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IDrawStyleState;
import nikolay_makhonin.android.views.markerline.contracts.ValueRangeChangedEventArgs;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.EventHandler;
import nikolay_makhonin.utils.events.IEventHandler;
import nikolay_makhonin.utils.events.IEventListener;

public class DrawState implements IDrawState {
    private Double _deltaPixel;
    private final Object _locker = new Object();
    private final IDrawStyleState _drawStyleState;
    private final IDrawSizeState  _drawSizeState;
    private final IDrawDataState  _drawDataState;
    private static final double EPSILON_MULT = 1.0 + 1e-5;

    public DrawState(IDrawStyleState drawStyleState, IDrawSizeState drawSizeState, IDrawDataState drawDataState) {
        _drawStyleState = drawStyleState;
        _drawSizeState = drawSizeState;
        _drawDataState = drawDataState;
        _drawSizeState.drawSizeChanged().add(_drawSizeChanged_listener);
        _drawDataState.valueRangeChanged().add(_valueRangeChanged_listener);
    }

    private IEventListener<DrawSizeChangedEventArgs> _drawSizeChanged_listener
        = new IEventListener<DrawSizeChangedEventArgs>() {
        @Override
        public boolean onEvent(Object o, DrawSizeChangedEventArgs e) {
            if (_deltaPixel != null) {
                int[] drawSize = _drawSizeState.getDrawSize();
                if (drawSize != null && (e.oldDrawSize == null || e.oldDrawSize[0] != drawSize[0])) {
                    _deltaPixel = null;
                    _deltaPixelChanged.invoke(this, EventArgs.Empty);
                }
            }
            return true;
        }
    };

    private IEventListener<ValueRangeChangedEventArgs> _valueRangeChanged_listener
        = new IEventListener<ValueRangeChangedEventArgs>() {
        @Override
        public boolean onEvent(Object o, ValueRangeChangedEventArgs e) {
            if (_deltaPixel != null) {
                double[] valueRange = _drawDataState.getValueRange();
                if (valueRange != null) {
                    if (e.oldValueRange == null) {
                        _deltaPixel = null;
                        _deltaPixelChanged.invoke(this, EventArgs.Empty);
                    } else {
                        double oldWidth = Math.abs(valueRange[1] - valueRange[0]);
                        double newWidth = Math.abs(e.oldValueRange[1] - e.oldValueRange[0]);
                        if (newWidth * EPSILON_MULT > oldWidth || newWidth * EPSILON_MULT < oldWidth) {
                            _deltaPixel = null;
                            _deltaPixelChanged.invoke(this, EventArgs.Empty);
                        }
                    }
                }
            }
            return true;
        }
    };

    @Override
    public Object Locker() {
        return _locker;
    }

    @Override
    public IDrawSizeState drawSize() {
        return _drawSizeState;
    }

    @Override
    public IDrawStyleState drawStyle() {
        return _drawStyleState;
    }

    @Override
    public IDrawDataState drawData() {
        return _drawDataState;
    }

    @Override
    public double getDeltaPixel() {
        if (_deltaPixel != null)
            return _deltaPixel;
        synchronized (_locker) {
            if (_deltaPixel != null)
                return _deltaPixel;
            double[] valueRange = _drawDataState.getValueRange();
            int[] drawSize = _drawSizeState.getDrawSize();
            _deltaPixel = Math.abs(valueRange[1] - valueRange[0]) / drawSize[0];
            return _deltaPixel;
        }
    }

    protected final IEventHandler _deltaPixelChanged = new EventHandler();

    @Override
    public IEventHandler deltaPixelChanged() {
        return _deltaPixelChanged;
    }
}
