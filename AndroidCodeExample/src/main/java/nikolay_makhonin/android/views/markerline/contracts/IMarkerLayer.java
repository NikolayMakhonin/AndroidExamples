package nikolay_makhonin.android.views.markerline.contracts;

import android.graphics.Canvas;

import java.util.List;

public interface IMarkerLayer {
    IMarkerBuilderCollection markerBuilderCollection(); 
    List<IMarker> getDrawMarkers();
    boolean draw(Canvas canvas, int[] position);
}
