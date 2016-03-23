package nikolay_makhonin.android.views.markerline;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerLayer;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerRender;

public class MarkerRender implements IMarkerRender {
    private IDrawState         _drawState;
    private List<IMarkerLayer> _markerLayers;

    public MarkerRender(IDrawState drawState)
    {
        _drawState = drawState;
        _markerLayers = new ArrayList();
    }

    @Override
    public List<IMarkerLayer> markerLayers() {
        return _markerLayers;
    }

    @Override
    public boolean draw(Canvas canvas, int[] position) {
        synchronized (_drawState.Locker()) {
            boolean result = false;
            for (IMarkerLayer markerLayer : _markerLayers) {
                result |= markerLayer.draw(canvas, position);
            }
            return result;
        }
    }    
}
