package nikolay_makhonin.android.views.markerline;

import java.util.List;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IMarker;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerBuilder;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.EventHandler;
import nikolay_makhonin.utils.events.IEventHandler;
import nikolay_makhonin.utils.events.IEventListener;

public abstract class MarkerBuilderBase implements IMarkerBuilder {
    protected final IDrawState     _drawState;
    private final   IMarker[]      _markers;
    private         Double         _maxDeltaPixels;
    private         Integer        _minLevel;
    private         IMarkerBuilder _this;
    protected final int            _drawOrder;

    public MarkerBuilderBase(IDrawState drawState, int drawOrder, int countLevels) {
        _drawOrder = drawOrder;
        _drawState = drawState;
        _markers = new IMarker[countLevels];
        _drawState.deltaPixelChanged().add(_deltaPixelsChanged_listener);
        _this = this;
    }
    
    protected abstract IMarker createMarker(int level);
    private IMarker getMarker(int level) {
        if (level >= _markers.length) return null;
        IMarker marker = _markers[level];
        if (marker != null) return marker;
        marker = createMarker(level);
        _markers[level] = marker;
        if (level == 0) {
            marker.maxDeltaPixelsChanged().add(_maxDeltaPixelsChanged_listener);
            if (_maxDeltaPixels != null) {
                _maxDeltaPixels = null;
                _maxDeltaPixelsChanged.invoke(this, EventArgs.Empty);
            } 
        }
        return marker;
    }

    private IEventListener _maxDeltaPixelsChanged_listener = new IEventListener() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            if (_maxDeltaPixels != null) {
                _maxDeltaPixels = null;
                _maxDeltaPixelsChanged.invoke(_this, EventArgs.Empty);
            }
            return true;
        }
    };

    private IEventListener _deltaPixelsChanged_listener = new IEventListener() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            _minLevel = null;
            return true;
        }
    };

    protected abstract int calcMinLevel();

    private int getMinLevel() {
        if (_minLevel != null)
            return _minLevel;
        return _minLevel = calcMinLevel();
    }

    @Override
    public void getMarkers(List<IMarker> destMarkers) {
        int minLevel       = getMinLevel();
        int countLevels    = _markers.length;
        int level          = minLevel;
        int maxCountLevels = _drawState.drawSize().getMaxCountLevels();
        while (level < countLevels && destMarkers.size() < maxCountLevels) {
            IMarker marker = getMarker(level);
            marker.setLevel(destMarkers.size());
            if (marker.allowDraw(marker.getWidth())) {
                //                int newWidth = marker.getWidth();
                //                int i = 0;
                //                while (i < destMarkers.size()) {
                //                    IMarker marker2 = destMarkers.get(i);
                //                    marker2.setLevel(i);
                //                    if (!marker2.allowDraw(newWidth)) {
                //                        destMarkers.remove(i);
                //                        marker.setLevel(destMarkers.size());
                //                        newWidth = marker.getWidth();
                //                        continue;
                //                    }
                //                    i++;
                //                }
                destMarkers.add(marker);
            }
            level++;
        }
    }

    @Override
    public double getMaxDeltaPixels() {
        if (_maxDeltaPixels != null)
            return _maxDeltaPixels;
        return _maxDeltaPixels = getMarker(_markers.length - 1).getMaxDeltaPixels();
    }

    protected final IEventHandler _maxDeltaPixelsChanged = new EventHandler();

    @Override
    public IEventHandler maxDeltaPixelsChanged() {
        return _maxDeltaPixelsChanged;
    }
}
