package nikolay_makhonin.android.views.markerline.timespanline;

import android.graphics.Canvas;
import android.graphics.Paint;

import nikolay_makhonin.android.views.markerline.MarkerText;
import nikolay_makhonin.android.views.markerline.MarkerTextConverter;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IRangeLocker;
import nikolay_makhonin.utils.datetime.TimeSpan;
import nikolay_makhonin.utils.datetime.TimeSpanPart;

public class MarkerTimeSpanText extends MarkerText {
    public static final double MAX_TIME_SECONDS = TimeSpan.MaxValue.TotalSeconds();
    public static final double MIN_TIME_SECONDS = TimeSpan.MinValue.TotalSeconds();
    protected final double              _secondsPerDelta;
    private final   MarkerTextConverter _stringConverter;
    private final   int                 _deltaPart;
    private final   TimeSpanPart        _timeSpanPart;
    private static final double _1_ticksPerSecond = 1.0 / TimeSpan.TicksPerSecond;

    public MarkerTimeSpanText(IDrawState drawState, int drawOrder, TimeSpanPart timeSpanPart, int deltaPart,
        MarkerTextConverter stringConverter, boolean drawBetweenMarkers)
    {
        super(drawState, drawOrder, stringConverter, drawBetweenMarkers);
        _deltaPart = deltaPart;
        _timeSpanPart = timeSpanPart;
        _stringConverter = stringConverter;
        _secondsPerDelta = getDeltaSeconds(_timeSpanPart);
    }
    
    public static double getDeltaSeconds(TimeSpanPart timeSpanPart) {
        switch (timeSpanPart) {
            case Year: 
                return TimeSpan.TicksPerYear * _1_ticksPerSecond;
            case Month: 
                return TimeSpan.TicksPerMonth * _1_ticksPerSecond;
            case Week: 
                return TimeSpan.TicksPerWeek * _1_ticksPerSecond;
            case Day: 
            case DayOfMonth: 
            case DayOfWeek: 
                return TimeSpan.TicksPerDay * _1_ticksPerSecond;
            case Hour: 
                return TimeSpan.TicksPerHour * _1_ticksPerSecond;
            case Minute: 
                return TimeSpan.TicksPerMinute * _1_ticksPerSecond;
            case Second: 
                return TimeSpan.TicksPerSecond * _1_ticksPerSecond;
            case Millisecond: 
                return TimeSpan.TicksPerMillisecond * _1_ticksPerSecond;
            case Tick: 
                return _1_ticksPerSecond;
            default:
                throw new IllegalArgumentException("Unknown timeSpanPart: " + timeSpanPart);                
        }
    }
    
    @Override
    protected double calcDelta() {
        return _secondsPerDelta * _deltaPart;
    }
    
    private String valueToString(double value) {
        return _stringConverter.ConvertToString(value);
    }
        
    @Override
    protected void drawPrivate(Canvas canvas, int[] position, IRangeLocker rangeLocker) {
        double[] valueRange = _drawState.drawData().getValueRange();
        double maxDisplayValue = Math.max(valueRange[0], valueRange[1]);
        double minDisplayValue = Math.min(valueRange[0], valueRange[1]);
        if (maxDisplayValue < MIN_TIME_SECONDS || minDisplayValue > MAX_TIME_SECONDS) return;
        
        double delta = calcDelta();
        long deltaTicks = (long)Math.round(delta * TimeSpan.TicksPerSecond);
        double startSeconds = Math.floor((minDisplayValue < MIN_TIME_SECONDS ? MIN_TIME_SECONDS : minDisplayValue) / delta) * delta;
        if (startSeconds < MIN_TIME_SECONDS) startSeconds += delta;
        TimeSpan time = TimeSpan.FromSeconds(startSeconds);
        
        int[] drawSize = _drawState.drawSize().getDrawSize();
        int measureWidth = drawSize[0];
        int measureHeight = drawSize[1];
        
        double displayWidthCoef = measureWidth / (maxDisplayValue - minDisplayValue);
        
        Paint paint = getPaint(getLevel());
        
        double totalSeconds2 = time.TotalSeconds();            
        float x2 = 0;
        boolean first = true;
        
        float posX = position[0];
        float posY = position[1];
        
        while (true) {
            double totalSeconds = totalSeconds2;            
            boolean last = totalSeconds > maxDisplayValue;
            if (last || totalSeconds + _secondsPerDelta * _deltaPart >= MAX_TIME_SECONDS) break;
            time = time.Add(deltaTicks);
            totalSeconds2 = time.TotalSeconds();            
            String text = valueToString(!_drawBetweenMarkers || totalSeconds >= 0 ? totalSeconds : totalSeconds2);            
            
            float x1 = (!first) ? x2 : (float) ((totalSeconds - minDisplayValue) * displayWidthCoef);
            x2 = (float) ((totalSeconds2 - minDisplayValue) * displayWidthCoef);
            drawText(canvas, paint, rangeLocker, text, posX + x1, posX + x2, posY, measureHeight);
            first = false;
        }
    }

    @Override
    protected int calcWidth(int level) {
        return _stringConverter.calcWidth(getPaint(level), getDelta());
    }
}
