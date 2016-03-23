package nikolay_makhonin.android.views.markerline;

import nikolay_makhonin.android.views.markerline.contracts.DrawSizeChangedEventArgs;
import nikolay_makhonin.android.views.markerline.contracts.IDrawSizeState;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.EventHandler;
import nikolay_makhonin.utils.events.IEventHandler;

public class DrawSizeState implements IDrawSizeState {
    private int[] _drawSize;
    private final Object _locker = new Object();
    private int _maxCountLevels;
    
    @Override
    public Object Locker() {
        return _locker;
    }
    
    @Override
    public int[] getDrawSize() {
        return _drawSize;
    }
    
    @Override
    public void setDrawSize(int width, int height) {
        if (_drawSize != null && _drawSize[0] == width && _drawSize[1] == height) return;
        synchronized (_locker) {   
            int[] oldDrawSize;
            if (_drawSize != null) {
                if (_drawSize[0] == width && _drawSize[1] == height) return;
                oldDrawSize = _drawSize.clone();
                _drawSize[0] = width;                
                _drawSize[1] = height;                
            } else {
                oldDrawSize = null;
                _drawSize = new int[] { width, height };
            }
            _drawSizeChanged.invoke(this, new DrawSizeChangedEventArgs(oldDrawSize));
//            if (_drawSize[0] != oldWidth) _deltaPixel = null;
//            _drawSizeChanged.invoke(this, EventArgs.Empty);
//            if (_deltaPixel == null) _deltaPixelChanged.invoke(this, EventArgs.Empty);
        }
    }

    @Override
    public void setDrawSize(int[] drawSize) {
        if (_drawSize != null && _drawSize[0] == drawSize[0] && _drawSize[1] == drawSize[1]) return;
        synchronized (_locker) {            
            if (_drawSize != null && _drawSize[0] == drawSize[0] && _drawSize[1] == drawSize[1]) return;
            int[] oldDrawSize = _drawSize == null ? null : _drawSize.clone();
            _drawSize = drawSize;
            _drawSizeChanged.invoke(this, new DrawSizeChangedEventArgs(oldDrawSize));
//            if (_drawSize[0] != oldWidth) _deltaPixel = null;
//            _drawSizeChanged.invoke(this, EventArgs.Empty);
//            if (_deltaPixel == null) _deltaPixelChanged.invoke(this, EventArgs.Empty);
        }
    }

    protected final IEventHandler<DrawSizeChangedEventArgs> _drawSizeChanged
        = new EventHandler<DrawSizeChangedEventArgs>();

    @Override
    public IEventHandler<DrawSizeChangedEventArgs> drawSizeChanged() {
        return _drawSizeChanged;
    }

    @Override
    public int getMaxCountLevels() {
        return _maxCountLevels;
    }

    @Override
    public void setMaxCountLevels(int maxCountLevels) {
        if (_maxCountLevels == maxCountLevels)
            return;
        synchronized (_locker) {
            if (_maxCountLevels == maxCountLevels)
                return;
            _maxCountLevels = maxCountLevels;
            _maxCountLevelsChanged.invoke(this, EventArgs.Empty);
        }
    }

    protected final IEventHandler _maxCountLevelsChanged = new EventHandler();

    @Override
    public IEventHandler maxCountLevelsChanged() {
        return _maxCountLevelsChanged;
    }
}
