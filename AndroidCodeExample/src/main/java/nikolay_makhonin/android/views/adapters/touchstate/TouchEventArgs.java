package nikolay_makhonin.android.views.adapters.touchstate;

import nikolay_makhonin.utils.events.EventArgs;

public class TouchEventArgs extends EventArgs {
    /** if (State == Move | ClickDownMove) PointIndexes.length >= 1 else PointIndexes.length == 1 */
    public final int[] PointIndexes;
    public final TouchStateBase State;
    
    public TouchEventArgs(TouchStateBase state, int index) {
        this(state, new int[] { index });
    }
    
    public TouchEventArgs(TouchStateBase state, int[] indexes) {
        this.State = state;
        this.PointIndexes = indexes;
    }
}