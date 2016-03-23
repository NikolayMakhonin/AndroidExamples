package nikolay_makhonin.android.views.markerline;

import android.content.Context;
import android.util.AttributeSet;

import nikolay_makhonin.android.views.markerline.contracts.IDrawStyleState;

public class DrawStyleState implements IDrawStyleState {
    private final Context _context;
    private final AttributeSet _attrs;
    private final int _defStyleAttr;
    private final boolean _isInEditMode;
    private final Object _locker = new Object();
    
    public DrawStyleState(Context context, AttributeSet attrs, int defStyleAttr, boolean isInEditMode) {
        _context = context;
        _attrs = attrs;
        _defStyleAttr = defStyleAttr;
        _isInEditMode = isInEditMode;
    }

    @Override
    public boolean isInEditMode() {
        return _isInEditMode;
    }

    @Override
    public Context getContext() {
        return _context;
    }

    @Override
    public AttributeSet getAttributes() {
        return _attrs;
    }

    @Override
    public int getDefStyleAttr() {
        return _defStyleAttr;
    }    
    
    @Override
    public Object Locker() {
        return _locker;
    }
}
