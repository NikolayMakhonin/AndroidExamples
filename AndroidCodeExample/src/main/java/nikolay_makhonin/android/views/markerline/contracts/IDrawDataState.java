package nikolay_makhonin.android.views.markerline.contracts;

import nikolay_makhonin.utils.contracts.ILocker;
import nikolay_makhonin.utils.events.IEventHandler;

public interface IDrawDataState extends ILocker {

    /** = {minValue, maxValue} */
    double[] getValueRange();
    void setValueRange(double[] valueRange);
    void setValueRange(double minValue, double maxValue);
    IEventHandler<ValueRangeChangedEventArgs> valueRangeChanged();
    
    /** = {startValue, endValue} */
    double[] getSelectedRange();
    void setSelectedRange(double[] selectedRange);
    void setSelectedRange(double startValue, double endValue);
    IEventHandler selectedRangeChanged();
    
}
