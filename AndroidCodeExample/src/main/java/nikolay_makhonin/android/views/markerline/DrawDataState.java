package nikolay_makhonin.android.views.markerline;

import nikolay_makhonin.android.views.markerline.contracts.IDrawDataState;
import nikolay_makhonin.android.views.markerline.contracts.ValueRangeChangedEventArgs;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.EventHandler;
import nikolay_makhonin.utils.events.IEventHandler;

public class DrawDataState implements IDrawDataState {
    private double[] _valueRange;
    private double[] _selectedRange;
    private final Object _locker = new Object();
    
    @Override
    public Object Locker() {
        return _locker;
    }
    
    @Override
    public double[] getValueRange() {
        return _valueRange;
    }
    
    @Override
    public void setValueRange(double[] valueRange) {
        if (_valueRange != null && _valueRange[0] == valueRange[0] && _valueRange[1] == valueRange[1]) return;
        synchronized (_locker) {            
            if (_valueRange != null && _valueRange[0] == valueRange[0] && _valueRange[1] == valueRange[1]) return;
            double[] oldValueRange = _valueRange == null ? null : _valueRange.clone(); 
            _valueRange = valueRange;
            onValueRangeChanged(oldValueRange);
        }
    }

    @Override
    public void setValueRange(double minValue, double maxValue) {
        if (_valueRange != null && _valueRange[0] == minValue && _valueRange[1] == maxValue) return;
        synchronized (_locker) {   
            double[] oldValueRange; 
            if (_valueRange != null) {
                if (_valueRange[0] == minValue && _valueRange[1] == maxValue) return;
                oldValueRange = _valueRange.clone();
                _valueRange[0] = minValue;
                _valueRange[1] = maxValue;
            } else {
                oldValueRange = null;
                _valueRange = new double[] { minValue, maxValue };
            }
            onValueRangeChanged(oldValueRange);
        }
    }
    
    private void onValueRangeChanged(double[] oldValueRange) {
        _valueRangeChanged.invoke(this, new ValueRangeChangedEventArgs(oldValueRange));
//        oldValueRange = Math.abs(_valueRange[1] - _valueRange[0]);
//        double newValueRange = Math.abs(_valueRange[1] - _valueRange[0]);
//        if (newValueRange * EPSILON_MULT > oldValueRange || newValueRange * EPSILON_MULT < oldValueRange) {
//            _deltaPixel = null;
//        }
//        _valueRangeChanged.invoke(this, EventArgs.Empty);
//        if (_deltaPixel == null) _deltaPixelChanged.invoke(this, EventArgs.Empty);        
    }

    protected final IEventHandler<ValueRangeChangedEventArgs> _valueRangeChanged
        = new EventHandler<ValueRangeChangedEventArgs>();

    @Override
    public IEventHandler<ValueRangeChangedEventArgs> valueRangeChanged() {
        return _valueRangeChanged;
    }


    @Override
    public double[] getSelectedRange() {
        return _selectedRange;
    }

    @Override
    public void setSelectedRange(double[] selectedRange) {
        if (_selectedRange != null && _selectedRange[0] == selectedRange[0] && _selectedRange[1] == selectedRange[1])
            return;
        synchronized (_locker) {
            if (_selectedRange != null && _selectedRange[0] == selectedRange[0]
                && _selectedRange[1] == selectedRange[1])
                return;
            _selectedRange = selectedRange;
            onSelectedRangeChanged();
        }
    }

    @Override
    public void setSelectedRange(double startValue, double endValue) {
        if (_selectedRange != null && _selectedRange[0] == startValue && _selectedRange[1] == endValue)
            return;
        synchronized (_locker) {
            if (_selectedRange != null) {
                if (_selectedRange[0] == startValue && _selectedRange[1] == endValue)
                    return;
                _selectedRange[0] = startValue;
                _selectedRange[1] = endValue;
            } else {
                _selectedRange = new double[] { startValue, endValue };
            }
            onSelectedRangeChanged();
        }
    }

    private void onSelectedRangeChanged() {
        _selectedRangeChanged.invoke(this, EventArgs.Empty);
    }

    protected final IEventHandler _selectedRangeChanged = new EventHandler();

    @Override
    public IEventHandler selectedRangeChanged() {
        return _selectedRangeChanged;
    }
}
