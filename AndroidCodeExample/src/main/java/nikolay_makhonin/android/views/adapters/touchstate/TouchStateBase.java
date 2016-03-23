package nikolay_makhonin.android.views.adapters.touchstate;

import java.util.EnumSet;

import nikolay_makhonin.utils.lists.IList;
import nikolay_makhonin.utils.lists.SortedList;

public class TouchStateBase {
    public static final int               MAX_POINTER_COUNT = 10;
    protected           IList<PointState> _downStates       = new SortedList<PointState>(true, true);
    protected           IList<PointState> _upStates         = new SortedList<PointState>(true, false);
    protected int     _pointerCount;
    protected boolean _clickDownMode;
    public static final EnumSet<TouchState> UpStates   = EnumSet
        .of(TouchState.None, TouchState.Up, TouchState.Click, TouchState.DoubleClick);
    public static final EnumSet<TouchState> DownStates = EnumSet
        .of(TouchState.Down, TouchState.Move, TouchState.ClickDown, TouchState.ClickDownMove);

    public TouchStateBase() {
    }

    public boolean isClickDownMode() {
        return _clickDownMode;
    }

    /** = MotionEvent.getPointerCount() */
    public int getPointCount() {
        return _downStates.size();
    }

    /** index = 0 .. MAX_POINTER_COUNT - 1 */
    public PointState getPointState(int index) {
        return _downStates.get(index);
    }    
}