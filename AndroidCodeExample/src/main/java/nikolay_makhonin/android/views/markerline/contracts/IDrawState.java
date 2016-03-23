package nikolay_makhonin.android.views.markerline.contracts;

import nikolay_makhonin.utils.contracts.ILocker;
import nikolay_makhonin.utils.events.IEventHandler;

public interface IDrawState extends ILocker {
    
    IDrawSizeState drawSize();
    IDrawStyleState drawStyle();
    IDrawDataState drawData();
    
    /** delta value per pixel */
    double getDeltaPixel();
    IEventHandler deltaPixelChanged();
}
