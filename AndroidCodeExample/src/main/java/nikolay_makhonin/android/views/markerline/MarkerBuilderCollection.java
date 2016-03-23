package nikolay_makhonin.android.views.markerline;

import java.util.Comparator;
import java.util.List;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IMarker;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerBuilder;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerBuilderCollection;
import nikolay_makhonin.utils.CompareUtils;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.EventHandler;
import nikolay_makhonin.utils.events.IEventHandler;
import nikolay_makhonin.utils.events.IEventListener;
import nikolay_makhonin.utils.lists.CollectionChangedEventArgs;
import nikolay_makhonin.utils.lists.ICollectionChangedList;
import nikolay_makhonin.utils.lists.IList;
import nikolay_makhonin.utils.lists.SortedList;

public class MarkerBuilderCollection implements IMarkerBuilderCollection {
    private final ICollectionChangedList<IMarkerBuilder> _markerBuilders;
    private       Double                                 _maxDeltaPixels;
    private final IDrawState                             _drawState;

    private final IMarkerBuilder _currentDeltaPixels = new IMarkerBuilder() {
        @Override
        public void getMarkers(List<IMarker> destMarkers) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getMaxDeltaPixels() {
            return _drawState.getDeltaPixel();
        }

        @Override
        public IEventHandler maxDeltaPixelsChanged() {
            throw new UnsupportedOperationException();
        }
    };

    public MarkerBuilderCollection(IDrawState drawState) {
        _drawState = drawState;
        _markerBuilders = new SortedList<IMarkerBuilder>(true, false, _comparatorMaxDeltaPixels);
        _markerBuilders.CollectionChanged().add(_collectionChanged_listener);
    }

    private Comparator<IMarkerBuilder> _comparatorMaxDeltaPixels = new Comparator<IMarkerBuilder>() {
        @Override
        public int compare(IMarkerBuilder o1, IMarkerBuilder o2) {
            return CompareUtils.Compare(o1.getMaxDeltaPixels(), o2.getMaxDeltaPixels());
        }
    };

    private IEventListener<CollectionChangedEventArgs<IMarkerBuilder>> _collectionChanged_listener
        = new IEventListener<CollectionChangedEventArgs<IMarkerBuilder>>() {
        @Override
        public boolean onEvent(Object o, CollectionChangedEventArgs<IMarkerBuilder> e) {
            _collectionChanged.invoke(this, EventArgs.Empty);
            switch (e.getChangedType()) {
                case Added:
                    for (Object item : e.getNewItems()) {
                        ((IMarkerBuilder) item).maxDeltaPixelsChanged().add(_maxDeltaPixelsChanged_listener);
                    }
                    if (e.getNewIndex() >= _markerBuilders.size()) {
                        if (_maxDeltaPixels != null) {
                            _maxDeltaPixels = null;
                            _maxDeltaPixelsChanged.invoke(this, EventArgs.Empty);
                        }
                    }
                    break;
                case Removed:
                    for (Object item : e.getOldItems()) {
                        ((IMarkerBuilder) item).maxDeltaPixelsChanged().remove(_maxDeltaPixelsChanged_listener);
                    }
                    if (e.getOldIndex() >= _markerBuilders.size()) {
                        if (_maxDeltaPixels != null) {
                            _maxDeltaPixels = null;
                            _maxDeltaPixelsChanged.invoke(this, EventArgs.Empty);
                        }
                    }
                    break;
                case Setted:
                    ((IMarkerBuilder) e.getOldItems()[0]).maxDeltaPixelsChanged()
                        .remove(_maxDeltaPixelsChanged_listener);
                    ((IMarkerBuilder) e.getNewItems()[0]).maxDeltaPixelsChanged().add(_maxDeltaPixelsChanged_listener);
                    if (e.getNewIndex() >= _markerBuilders.size() - 1) {
                        if (_maxDeltaPixels != null) {
                            _maxDeltaPixels = null;
                            _maxDeltaPixelsChanged.invoke(this, EventArgs.Empty);
                        }
                    }
                    break;
            }
            return false;
        }
    };

    private IEventListener _maxDeltaPixelsChanged_listener = new IEventListener() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            int index = _markerBuilders.IndexOf((IMarkerBuilder) o);
            _markerBuilders.OnItemModified(index);
            return true;
        }
    };

    @Override
    public void getMarkers(List<IMarker> destMarkers) {
        int index = _markerBuilders.IndexOf(_currentDeltaPixels);
        if (index < 0) {
            index = ~index;
        } else {
            for (int i = index - 1; i >= 0; i--) {
                if (_markerBuilders.get(i).getMaxDeltaPixels() < _drawState.getDeltaPixel())
                    break;
                index = i;
            }
        }
        int count = _markerBuilders.size();
        for (int i = index; i < count; i++) {
            IMarkerBuilder markerBuilder = _markerBuilders.get(i);
            markerBuilder.getMarkers(destMarkers);
            if (destMarkers.size() >= _drawState.drawSize().getMaxCountLevels())
                break;
        }
    }

    @Override
    public double getMaxDeltaPixels() {
        if (_maxDeltaPixels != null)
            return _maxDeltaPixels;
        return _maxDeltaPixels = _markerBuilders.get(_markerBuilders.size()).getMaxDeltaPixels();
    }

    protected final IEventHandler _maxDeltaPixelsChanged = new EventHandler();

    @Override
    public IEventHandler maxDeltaPixelsChanged() {
        return _maxDeltaPixelsChanged;
    }

    protected final IEventHandler _collectionChanged = new EventHandler();

    @Override
    public IEventHandler collectionChanged() {
        return _collectionChanged;
    }

    @Override
    public IList<IMarkerBuilder> markerBuilders() {
        return _markerBuilders;
    }
}
