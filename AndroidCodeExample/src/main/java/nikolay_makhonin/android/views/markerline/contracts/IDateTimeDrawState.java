package nikolay_makhonin.android.views.markerline.contracts;

import java.util.TimeZone;

import nikolay_makhonin.utils.events.IEventHandler;

public interface IDateTimeDrawState extends IDrawStyleState {

    TimeZone getTimeZone();
    void setTimeZone(TimeZone timeZone);
    IEventHandler timeZoneChanged();
    
}