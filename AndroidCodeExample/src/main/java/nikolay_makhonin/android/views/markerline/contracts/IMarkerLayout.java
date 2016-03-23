package nikolay_makhonin.android.views.markerline.contracts;

import nikolay_makhonin.android.views.markerline.MarkerLayoutAlign;

public interface IMarkerLayout {
    IMarkerRender Render();
    MarkerLayoutAlign getAlign();
    void setAlign(MarkerLayoutAlign align);
    IDrawState DrawState();
}
