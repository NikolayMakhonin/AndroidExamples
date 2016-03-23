package nikolay_makhonin.android.views.markerline.contracts;

import android.content.Context;
import android.util.AttributeSet;

import nikolay_makhonin.utils.contracts.ILocker;

public interface IDrawStyleState extends ILocker {

    Context getContext();
    AttributeSet getAttributes();
    int getDefStyleAttr();
    
    boolean isInEditMode();

}
