package nikolay_makhonin.android.views.markerline.datetimeline;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import nikolay_makhonin.android.views.markerline.MarkerText;
import nikolay_makhonin.android.views.markerline.MarkerTextConverter;
import nikolay_makhonin.android.views.markerline.contracts.IDateTimeDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IRangeLocker;
import nikolay_makhonin.utils.datetime.DateTime;

public class MarkerDateTimeText extends MarkerText {
    public static final double DAY_SECONDS       = 3600 * 24;
    public static final double MIN_MONTH_SECONDS = DAY_SECONDS * 28;
    public static final double MAX_MONTH_SECONDS = DAY_SECONDS * 31;
    public static final double MIN_YEAR_SECONDS  = DAY_SECONDS * 365;
    public static final double MAX_YEAR_SECONDS  = DAY_SECONDS * 366;
    public static final double MAX_DATE_SECONDS  = DateTime.MaxValue.TotalSeconds();
    protected final double              _minSecondsPerDelta;
    protected final double              _maxSecondsPerDelta;
    private final   MarkerTextConverter _stringConverter;
    private final   int                 _deltaField;
    private final   int                 _calendarField;

    public MarkerDateTimeText(IDrawState drawState, int drawOrder, int calendarField, int deltaField,
        MarkerTextConverter stringConverter, boolean drawBetweenMarkers)
    {
        super(drawState, drawOrder, stringConverter, drawBetweenMarkers);
        _deltaField = deltaField;
        _calendarField = calendarField;
        _stringConverter = stringConverter;
        _minSecondsPerDelta = getMinDeltaSeconds(_calendarField);
        _maxSecondsPerDelta = getMaxDeltaSeconds(_calendarField);
    }
    
    public static double getMinDeltaSeconds(int calendarField) {
        switch(calendarField) {
            case Calendar.YEAR:
                return MIN_YEAR_SECONDS;
            case Calendar.MONTH:
                return MIN_MONTH_SECONDS;
            case Calendar.DAY_OF_MONTH:
                return DAY_SECONDS;
            case Calendar.HOUR:
                return 3600;
            case Calendar.MINUTE:
                return 60;
            case Calendar.SECOND:
                return 1;
            default:
                throw new IllegalArgumentException("Unknown calendarField: " + calendarField);                
        }
    }
    
    public static double getMaxDeltaSeconds(int calendarField) {
        switch(calendarField) {
            case Calendar.YEAR:
                return MAX_YEAR_SECONDS;
            case Calendar.MONTH:
                return MAX_MONTH_SECONDS;
            default:
                return getMinDeltaSeconds(calendarField);
        }
    }
    
    @Override
    protected double calcDelta() {
        return _minSecondsPerDelta * _deltaField;
    }
    
    private Calendar createCalendar() {
        TimeZone timeZone = ((IDateTimeDrawState)_drawState.drawStyle()).getTimeZone();
        if (timeZone == null) timeZone = DateTime.UTC;
        return new GregorianCalendar(timeZone);
    }
    
    @Override
    public double roundValueByMarker(double value) {
        Calendar calendar = createCalendar();
        setStartCalendarDate(calendar, value, getDelta());
        double totalSeconds1 = new DateTime(calendar.getTime()).TotalSeconds();
        incrementCalendar(calendar);
        double totalSeconds2 = new DateTime(calendar.getTime()).TotalSeconds();
        if (Math.abs(value - totalSeconds1) < Math.abs(value - totalSeconds2)) {
            return totalSeconds1;
        } else {
            return totalSeconds2;
        }
    }
    
    private void setStartCalendarDate(Calendar calendar, double minValue, double delta) {
        double startValue;
        switch (_calendarField) {
            case Calendar.DAY_OF_MONTH:
                startValue = Math.floor(minValue / delta) * delta;
                if (delta > DAY_SECONDS * 6) {
                    startValue -= DAY_SECONDS * 5; // for weeks
                    if (startValue < 0) startValue = DAY_SECONDS * 2;
                } else {
                    if (startValue < 0) startValue = 0;
                }
                break;
            default:
                startValue = minValue;
                break;
        }
        
        switch (_calendarField) {
            case Calendar.YEAR:
                calendar.setTime(DateTime.FromSeconds(startValue).toDate());
                calendar.set(Calendar.YEAR, (int) Math.round((calendar.get(Calendar.YEAR) - 1) / _deltaField)
                    * _deltaField + 1);
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            case Calendar.MONTH:
                calendar.setTime(DateTime.FromSeconds(startValue).toDate());
                calendar.set(Calendar.MONTH, (int) Math.round(calendar.get(Calendar.MONTH) / _deltaField) * _deltaField);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            case Calendar.DAY_OF_MONTH:
                calendar.setTime(DateTime.FromSeconds(startValue).toDate());
//                calendar.set(Calendar.DAY_OF_MONTH, (int) Math.round((calendar.get(Calendar.DAY_OF_MONTH) - 1) / _deltaField)
//                    * _deltaField + 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            case Calendar.HOUR:
            case Calendar.MINUTE:
            case Calendar.SECOND:
                calendar.setTime(DateTime.FromSeconds(Math.floor(startValue / delta) * delta).toDate());
                break;
        }        
    }
    
    private String calendarDateToString(double value) {
        return _stringConverter.ConvertToString(value);
    }
    
    private void incrementCalendar(Calendar calendar) {
        calendar.add(_calendarField, _deltaField);
    }
        
    @Override
    protected void drawPrivate(Canvas canvas, int[] position, IRangeLocker rangeLocker) {
        double[] valueRange = _drawState.drawData().getValueRange();
        double maxDisplayValue = Math.max(valueRange[0], valueRange[1]);
        double minDisplayValue = Math.min(valueRange[0], valueRange[1]);
        if (maxDisplayValue < 0 || minDisplayValue > MAX_DATE_SECONDS) return;
        
        double delta = getDelta();
        Calendar calendar = createCalendar();
        setStartCalendarDate(calendar, minDisplayValue < 0 ? 0 : minDisplayValue, delta);
        
        int[] drawSize = _drawState.drawSize().getDrawSize();
        int measureWidth = drawSize[0];
        int measureHeight = drawSize[1];
        
        double displayWidthCoef = measureWidth / (maxDisplayValue - minDisplayValue);
        
        Paint paint = getPaint(getLevel());
        
        double totalSeconds2 = new DateTime(calendar.getTime()).TotalSeconds();            
        float x2 = 0;
        boolean first = true;
        
        float posX = position[0];
        float posY = position[1];
        
        while (true) {
            double totalSeconds = totalSeconds2;            
            boolean last = totalSeconds > maxDisplayValue;
            if (last || totalSeconds + _maxSecondsPerDelta * _deltaField >= MAX_DATE_SECONDS) break;
            incrementCalendar(calendar);
            totalSeconds2 = new DateTime(calendar.getTime()).TotalSeconds();            
            String text = calendarDateToString(!_drawBetweenMarkers || totalSeconds >= 0 ? totalSeconds : totalSeconds2);            
            
            float x1 = (!first) ? x2 : (float) ((totalSeconds - minDisplayValue) * displayWidthCoef);
            first = false;
            x2 = (float) ((totalSeconds2 - minDisplayValue) * displayWidthCoef);
            drawText(canvas, paint, rangeLocker, text, posX + x1, posX + x2, posY, measureHeight);
        }
    }

    @Override
    protected int calcWidth(int level) {
        return _stringConverter.calcWidth(getPaint(level), getDelta());
    }
}
