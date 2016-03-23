package nikolay_makhonin.android.views.adapters.touchstate;

public enum TouchState {
    None,
    
    /** Down */
    Down,
    /** Down after Click */
    ClickDown,
    
    /** Move */
    Move,
    /** Move after ClickDown */
    ClickDownMove,
    
    /** Up = Click */
    Click,
    /** Up = Double Click */
    DoubleClick,
    /** Up */
    Up
}