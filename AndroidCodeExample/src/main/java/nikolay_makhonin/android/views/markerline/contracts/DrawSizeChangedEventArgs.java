package nikolay_makhonin.android.views.markerline.contracts;

import nikolay_makhonin.utils.events.EventArgs;

public class DrawSizeChangedEventArgs extends EventArgs {
    public final int[] oldDrawSize;
    public DrawSizeChangedEventArgs(int[] oldDrawSize) {
        this.oldDrawSize = oldDrawSize;
    }
}