package nikolay_makhonin.android.views.markerline.datetimeline;

import android.graphics.Paint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import nikolay_makhonin.android.views.markerline.MarkerTextConverter;
import nikolay_makhonin.android.views.markerline.contracts.IDateTimeDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.DrawStringUtils;
import nikolay_makhonin.utils.datetime.DateTime;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.IEventListener;

public class MarkerTextDateTimeConverter extends MarkerTextConverter {
    protected final SimpleDateFormat _dateTimeFormat;
    
    public MarkerTextDateTimeConverter(IDrawState drawState, String dateTimeFormat) {
        super(drawState);
        _dateTimeFormat = new SimpleDateFormat(dateTimeFormat);
        _dateTimeFormat.setTimeZone(((IDateTimeDrawState)drawState.drawStyle()).getTimeZone());
        ((IDateTimeDrawState)drawState.drawStyle()).timeZoneChanged().add(_timeZoneChanged_listener);
    }

    private IEventListener _timeZoneChanged_listener = new IEventListener<EventArgs>() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            _dateTimeFormat.setTimeZone(((IDateTimeDrawState) _drawState.drawStyle()).getTimeZone());
            return true;
        }
    };

    protected String dateToString(Date date) {
        return _dateTimeFormat.format(date);
    }

    @Override
    public int calcWidth(Paint paint, double delta) {
        Calendar calendar = new GregorianCalendar(DateTime.UTC);
        calendar.set(8000, 1, 28, 22, 59, 59);
        int maxWidth = 0;
        for (int i = 0; i < 12; i++) {
            String dateStr = dateToString(calendar.getTime());
            int width = DrawStringUtils.getTextDrawWidth(dateStr, paint);
            if (width > maxWidth)
                maxWidth = width;
            calendar.add(Calendar.MONTH, 1);
        }
        return maxWidth;
    }

    @Override
    protected String valueToStringPrivate(double value) {
        return dateToString(DateTime.FromSeconds(value).toDate());
    }
}
