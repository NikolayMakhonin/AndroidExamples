package nikolay_makhonin.android.views.markerline.contracts;

import android.graphics.Canvas;

import nikolay_makhonin.utils.events.IEventHandler;

public interface IMarker {
    /** 0 - самый мелкий маркер */
    void setLevel(int level);
    double getDelta();
    double roundValueByMarker(double value);
    int getLevel();
    int getWidth();
    int getSpaceWidth();
    /** Draw order. Markers order by Order, maxDeltaPixels. Default value == 0 */
    int DrawOrder();
    /** delta / (minWidth + minSpaceWidth) */
    double getMaxDeltaPixels(); 
    IEventHandler maxDeltaPixelsChanged();
    /** delta >= (max(width, newWidth) + spaceWidth) * deltaPixels */
    boolean allowDraw(int newWidth);
    void draw(Canvas canvas, int[] position, IRangeLocker rangeLocker);
}
