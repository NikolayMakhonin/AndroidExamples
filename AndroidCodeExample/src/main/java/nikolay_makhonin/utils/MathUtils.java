package nikolay_makhonin.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class MathUtils {
    
    // http://www.coderanch.com/t/410797/java/java/Mantissa-exponent-double
    // public static DoubleParts getDoubleParts(double value) {
    // long bits = Double.doubleToRawLongBits(value);
    // boolean negative = (bits & 0x8000000000000000L) != 0;
    // long exponent_part = (bits & 0x7ff0000000000000L) >>> 52;
    // long mantissa_part = bits & 0x000fffffffffffffL;
    // DoubleParts doublePatrs = new DoubleParts(0, 0);
    // //doublePatrs.exponent = Double.longBitsToDouble(exponent_part);
    // doublePatrs.mantissa = (negative ? -1.0 : 1.0) * Double.longBitsToDouble(mantissa_part);
    // return doublePatrs;
    // }
    
    // public static DoubleParts getDoubleParts(double value) {
    // long lbits = Double.doubleToLongBits(value);
    // long lsign = lbits >>> 63;
    // long lexp = (lbits >>> 52 & ((1 << 11) - 1)) - ((1 << 10) - 1);
    // long lmantissa = lbits & ((1L << 52) - 1);
    // double mantissa = Double.longBitsToDouble((lsign << 63) | ((1 << 10) - 1) << 52 | lmantissa);
    // DoubleParts doublePatrs = new DoubleParts(mantissa, lexp);
    // return doublePatrs;
    // }
    
    public static int getExponent(final double value) {
        // int exponent = -BigDecimal.valueOf(value).round(new MathContext(1,
        // RoundingMode.DOWN)).stripTrailingZeros().scale();
        if (value == 0) { return 0; }
        final double log10 = Math.log10(Math.abs(value));
        final int exponent = log10 < 0 ? -(int) Math.ceil(-log10) : (int) log10;
        return exponent;
    }
    
    public static double getMantissa(final double value) {
        return getMantissa(value, getExponent(value));
    }
    
    public static double getMantissa(final double value, final int exponent) {
        // double mantissa = BigDecimal.valueOf(BigDecimal.valueOf(value).unscaledValue().longValue(),
        // exponent+BigDecimal.valueOf(value).scale()).doubleValue();
        final double mantissa = value * Math.pow(10, -exponent);
        return mantissa;
    }
    
    public final static double getDouble(final double mantissa, final double exponent) {
        return mantissa * Math.pow(10, exponent);
    }
    
    // /** Количество цифр до запятой в десятичном числе (2030.0 => 4; 203.010 => 3; 0 => 0; 0.1 => 0; 0.023 => -1)
    // * BigDecimal тормозит жутко*/
    // public final static int digitToCommaCount(double value) {
    // int digitToComma = 1 - BigDecimal.valueOf(MathUtils.round(value, 1)).stripTrailingZeros().scale();
    // return digitToComma;
    // }
    //
    // /** Количество цифр до нулей в десятичном числе (2030.0 => 3; 203.010 => 5)
    // * BigDecimal тормозит жутко*/
    // public final static int digitToZeroCount(double value) {
    // int digitToComma = digitToCommaCount(value);
    // int digitToZero = digitToComma + BigDecimal.valueOf(value).stripTrailingZeros().scale();
    // return digitToZero;
    // }
    
    /** BigDecimal тормозит жутко */
    public static double round(final double value, final int numDigits) {
        return round(value, numDigits, RoundingMode.HALF_UP);
    }
    
    /** BigDecimal тормозит жутко */
    private static double round(final double value, int numDigits, final RoundingMode roundingMode) {
        if (numDigits <= 0) {
            numDigits = 1;
        }
        final BigDecimal decimal = BigDecimal.valueOf(value);
        return decimal.round(new MathContext(numDigits, roundingMode)).doubleValue();
    }
    
    public static double getIntegerValue(double value) {
        final boolean negative = value < 0;
        value = Math.abs(value);
        value = Math.floor(value);
        if (negative) {
            value = -value;
        }
        return value;
    }
    
    public static int max(final int... values) {
        final int length = values.length;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < length; i++) {
            final int value = values[i];
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
    
    public static int min(final int... values) {
        final int length = values.length;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            final int value = values[i];
            if (value < min) {
                min = value;
            }
        }
        return min;
    }
}
