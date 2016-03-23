package nikolay_makhonin.android.views.markerline.contracts;

import java.util.List;

import nikolay_makhonin.utils.events.IEventHandler;

public interface IMarkerBuilder {     
    void getMarkers(List<IMarker> destMarkers); 
    /** max(markers.getMaxDeltaPixels); */
    double getMaxDeltaPixels(); 
    IEventHandler maxDeltaPixelsChanged();
}
