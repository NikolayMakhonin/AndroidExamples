package nikolay_makhonin.android.views.markerline.timespanline;

import android.graphics.Canvas;

import nikolay_makhonin.android.views.markerline.MarkerLine;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IRangeLocker;
import nikolay_makhonin.utils.datetime.TimeSpan;
import nikolay_makhonin.utils.datetime.TimeSpanPart;

public class MarkerTimeSpanLine extends MarkerLine {
    public static final double MAX_TIME_SECONDS = TimeSpan.MaxValue.TotalSeconds();
    public static final double MIN_TIME_SECONDS = TimeSpan.MinValue.TotalSeconds();
    protected final double       _secondsPerDelta;
    private final   int          _deltaPart;
    private final   TimeSpanPart _timeSpanPart;
    private static final double _1_ticksPerSecond = 1.0 / TimeSpan.TicksPerSecond;

    public MarkerTimeSpanLine(IDrawState drawState, int drawOrder, TimeSpanPart timeSpanPart, int deltaPart,
        int middleSpaceHeight)
    {
        super(drawState, drawOrder, middleSpaceHeight);
        _deltaPart = deltaPart;
        _timeSpanPart = timeSpanPart;
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
        int maxCountLevels = _drawState.drawSize().getMaxCountLevels();
        int level = getLevel();
        
        float minLineLength = (float)((measureHeight - _middleSpaceHeight) * 0.5 / maxCountLevels);
        float lineLength = minLineLength * (level + 1);
        float lineHalfWidth = getWidth() * 0.5f;

        double displayWidthCoef = 1.f / (maxDisplayValue - minDisplayValue);
        
        float posX = position[0];
        float posY = position[1];
        
        while (true) {
            double value = time.TotalSeconds();            
            
            float x = posX + (float)((value - minDisplayValue) * displayWidthCoef) * measureWidth;
            float x1 = x - lineHalfWidth;
            float x2 = x + lineHalfWidth;
            
            if (rangeLocker == null || rangeLocker.lock(x1, x2)) {
                canvas.drawRect(x1, posY, x2, posY + lineLength, _paintLine);
                canvas.drawRect(x1, posY + measureHeight - lineLength, x2, posY + measureHeight, _paintLine);
            }
            
            if (value > maxDisplayValue || value + _secondsPerDelta * _deltaPart >= MAX_TIME_SECONDS) break;
            time = time.Add(deltaTicks);
        }
    }
}