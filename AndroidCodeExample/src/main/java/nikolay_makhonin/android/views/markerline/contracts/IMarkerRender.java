package nikolay_makhonin.android.views.markerline.contracts;

import android.graphics.Canvas;

import java.util.List;

public interface IMarkerRender {

    List<IMarkerLayer> markerLayers();
    
    boolean draw(Canvas canvas, int[] position);
    
}
