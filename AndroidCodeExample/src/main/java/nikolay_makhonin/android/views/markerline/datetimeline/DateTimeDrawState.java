package nikolay_makhonin.android.views.markerline.datetimeline;

import android.content.Context;
import android.util.AttributeSet;

import java.util.TimeZone;

import nikolay_makhonin.android.views.markerline.DrawStyleState;
import nikolay_makhonin.android.views.markerline.contracts.IDateTimeDrawState;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.EventHandler;
import nikolay_makhonin.utils.events.IEventHandler;

public class DateTimeDrawState extends DrawStyleState implements IDateTimeDrawState {
    public DateTimeDrawState(Context context, AttributeSet attrs, int defStyleAttr, boolean isInEditMode) {
        super(context, attrs, defStyleAttr, isInEditMode);
        _timeZone = TimeZone.getDefault();
    }

    private TimeZone _timeZone;
    
    @Override
    public TimeZone getTimeZone() {
        return _timeZone;
    }
    
    @Override
    public void setTimeZone(TimeZone timeZone) {
        if (_timeZone == timeZone || _timeZone != null && _timeZone.equals(timeZone)) {
            return;
        }
        _timeZone = timeZone;
        _timeZoneChanged.invoke(this, EventArgs.Empty);
    }

    protected final IEventHandler _timeZoneChanged = new EventHandler();

    @Override
    public IEventHandler timeZoneChanged() {
        return _timeZoneChanged;
    }
}
