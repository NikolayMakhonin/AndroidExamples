package nikolay_makhonin.utils;

import android.graphics.Paint;
import android.graphics.Rect;

public class DrawStringUtils {
    
    public static int getTextDrawWidth(final String text, final Paint paint) {
        final Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        final int width = bounds.width();
        return width;
    }
    
    public static int getTextMaxWidth(final DoubleStringConverter stringConverter, final String incrementString,
        final double minValue, final double maxValue, final int maxNumbersCount, final Paint paint) {
        return getTextMaxWidth(stringConverter, incrementString, minValue, maxValue, maxNumbersCount, paint, 0);
    }
    
    public static int getTextMaxWidth(final DoubleStringConverter stringConverter, final String stringForCalcSpace,
        final double minValue, final double maxValue, final int maxNumbersCount, final Paint paint,
        final int drawAreaWidth) {
        final int numDigits = MathUtils.getExponent(Math.max(Math.abs(minValue), Math.abs(maxValue)));
        final int numDeltaDigits = MathUtils.getExponent(Math.abs(maxValue - minValue) / maxNumbersCount);
        int minDigits = Math.min(numDigits, numDeltaDigits) - 1;
        int maxDigits = Math.max(numDigits, numDeltaDigits);
        final int maximumFractionDigits = stringConverter.getMaximumFractionDigits();
        if (minDigits < -maximumFractionDigits && maxDigits >= -maximumFractionDigits) {
            minDigits = -maximumFractionDigits;
        }
        if (maxDigits >= maximumFractionDigits && minDigits < maximumFractionDigits) {
            maxDigits = maximumFractionDigits;
        }
        if (maxDigits - minDigits > maximumFractionDigits) {
            minDigits = maxDigits - maximumFractionDigits;
        }
        double value = (Math.pow(10, maxDigits + 1) - Math.pow(10, minDigits)) / 3;
//        double value = ((maxDigits > 0 ? Math.pow(10, maxDigits) : 1) + (minDigits != maxDigits ? Math.pow(10,
//            minDigits) : 0)) * 9;
        if (Math.min(maxValue, minValue) < 0) {
            value = -value;
        }
        final int width = getTextDrawWidth(stringConverter.ConvertToString(value) + stringForCalcSpace, paint);
//        if (width * maxNumbersCount < drawAreaWidth) {
//            width = drawAreaWidth / maxNumbersCount;
//        }
        return width;
    }
    
}
