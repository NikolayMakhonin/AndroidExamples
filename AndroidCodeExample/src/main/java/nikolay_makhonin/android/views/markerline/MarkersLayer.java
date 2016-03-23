package nikolay_makhonin.android.views.markerline;

import android.graphics.Canvas;

import java.util.Comparator;
import java.util.List;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IMarker;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerBuilderCollection;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerLayer;
import nikolay_makhonin.android.views.markerline.contracts.IRangeLocker;
import nikolay_makhonin.utils.CompareUtils;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.IEventListener;
import nikolay_makhonin.utils.lists.IList;
import nikolay_makhonin.utils.lists.SortedList;

public class MarkersLayer implements IMarkerLayer {
    private final IMarkerBuilderCollection _markerBuilders;
    private final IDrawState               _drawState;
    private final IRangeLocker             _rangeLocker;
    private       IList<IMarker>           _markers;

    private Comparator<IMarker> _comparatorMarkers = new Comparator<IMarker>() {
        @Override
        public int compare(IMarker o1, IMarker o2) {
            int i;
            if ((i = CompareUtils.Compare(o1.DrawOrder(), o2.DrawOrder())) != 0)
                return i;
            return CompareUtils.Compare(o1.getMaxDeltaPixels(), o2.getMaxDeltaPixels());
        }
    };

    public MarkersLayer(IDrawState drawState, IRangeLocker rangeLocker) {
        _drawState = drawState;
        _markerBuilders = new MarkerBuilderCollection(_drawState);
        _markerBuilders.collectionChanged().add(_collectionChanged_listener);
        _drawState.deltaPixelChanged().add(_deltaPixelsChanged_listener);
        _rangeLocker = rangeLocker;
    }

    private IEventListener _deltaPixelsChanged_listener = new IEventListener() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            _markers = null;
            return true;
        }
    };

    private IEventListener _collectionChanged_listener = new IEventListener() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            _markers = null;
            return true;
        }
    };

    @Override
    public IMarkerBuilderCollection markerBuilderCollection() {
        return _markerBuilders;
    }

    public List<IMarker> getDrawMarkers() {
        if (_markers != null)
            return _markers;
        IList<IMarker> markers = new SortedList<IMarker>(false, false, _comparatorMarkers);
        _markerBuilders.getMarkers(markers);
        markers.Sort();
        return _markers = markers;
    }

    @Override
    public boolean draw(Canvas canvas, int[] position) {
        List<IMarker> markers = getDrawMarkers();
        if (markers.size() == 0)
            return false;
        if (_rangeLocker == null) {
            for (IMarker marker : markers) {
                marker.draw(canvas, position, null);
            }
        } else {
            _rangeLocker.clear();
            int count = markers.size();
            for (int i = count - 1; i >= 0; i--) {
                IMarker marker = markers.get(i);
                marker.draw(canvas, position, _rangeLocker);
            }
        }
        return true;
    }
}
