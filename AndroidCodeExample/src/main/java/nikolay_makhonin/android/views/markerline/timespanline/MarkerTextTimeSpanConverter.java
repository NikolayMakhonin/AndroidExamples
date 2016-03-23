package nikolay_makhonin.android.views.markerline.timespanline;

import android.graphics.Paint;

import nikolay_makhonin.android.views.markerline.MarkerTextConverter;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.DrawStringUtils;
import nikolay_makhonin.utils.datetime.TimeSpan;
import nikolay_makhonin.utils.datetime.TimeSpanFormat;
import nikolay_makhonin.utils.datetime.TimeSpanPart;

public class MarkerTextTimeSpanConverter extends MarkerTextConverter {
    protected final TimeSpanFormat _timeSpanFormat;
    private final   TimeSpan       _calcWidthTimeSpan;

    public MarkerTextTimeSpanConverter(IDrawState drawState, String[] patterns, TimeSpanPart[] timeParts) {
        super(drawState);
        _timeSpanFormat = new TimeSpanFormat(patterns, timeParts);
        _calcWidthTimeSpan = new TimeSpan(TimeSpan.TicksPerYear * 99 +
            TimeSpan.TicksPerMonth * 10 +
            TimeSpan.TicksPerDay * 25 + //day of week = 6
            TimeSpan.TicksPerHour * 23 +
            TimeSpan.TicksPerMinute * 59 +
            TimeSpan.TicksPerSecond * 59 +
            TimeSpan.TicksPerMillisecond * 999 +
            TimeSpan.TicksPerMillisecond - 1
        );
    }

    @Override
    public int calcWidth(Paint paint, double delta) {
        String text = _timeSpanFormat.format(_calcWidthTimeSpan);
        return DrawStringUtils.getTextDrawWidth(text, paint);
    }

    @Override
    protected String valueToStringPrivate(double value) {
        return _timeSpanFormat.format(TimeSpan.FromSeconds(value));
    }
}
