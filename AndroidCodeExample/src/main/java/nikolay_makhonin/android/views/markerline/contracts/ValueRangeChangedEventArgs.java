package nikolay_makhonin.android.views.markerline.contracts;

import nikolay_makhonin.utils.events.EventArgs;

public class ValueRangeChangedEventArgs extends EventArgs {
    public final double[] oldValueRange;
    public ValueRangeChangedEventArgs(double[] oldValueRange) {
        this.oldValueRange = oldValueRange;
    }
}