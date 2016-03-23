package nikolay_makhonin.android.views.markerline.contracts;

import nikolay_makhonin.utils.contracts.ILocker;
import nikolay_makhonin.utils.events.IEventHandler;

public interface IDrawSizeState extends ILocker {
    
    /** = {width, height} */
    int[] getDrawSize();
    void setDrawSize(int[] drawSize);
    void setDrawSize(int width, int height);
    IEventHandler<DrawSizeChangedEventArgs> drawSizeChanged();
    
    int getMaxCountLevels();
    void setMaxCountLevels(int maxCountLevels);
    IEventHandler maxCountLevelsChanged();
    
}
