package nikolay_makhonin.utils;

import java.text.DecimalFormat;

import nikolay_makhonin.utils.contracts.IParamsChangedStringConverter;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.EventHandler;
import nikolay_makhonin.utils.events.IEventHandler;
import nikolay_makhonin.utils.logger.Log;

public class DoubleStringConverter implements IParamsChangedStringConverter {
    
    final DecimalFormat _decimalFormat;
    
    public DoubleStringConverter() {
        _decimalFormat = new DecimalFormat();
        _decimalFormat.setMinimumFractionDigits(0);
    }
    
    @Override
    public String ConvertToString(final Object value) {
        if (value instanceof Double) { return ConvertToString(((Double)value).doubleValue()); }
        Log.e("StringConverter", "value is not Double");
        return "";
    }
    
    public String ConvertToString(final double value) {
        final int exponent = MathUtils.getExponent(value);
        if (exponent > _minimumDigitsForExponent - 1 || exponent < -_minimumDigitsForExponent) {
            final double mantissa = MathUtils.getMantissa(value, exponent);
            return _decimalFormat.format(mantissa) + "e" + exponent;
        } else {
            return _decimalFormat.format(value);
        }
    }
    
    // region Properties
    
    // region MinimumDigitsForExponent
    
    private int _minimumDigitsForExponent = 5;
    
    public void setMinimumDigitsForExponent(final int minimumDigitsForExponent) {
        if (_minimumDigitsForExponent == minimumDigitsForExponent) { return; }
        _minimumDigitsForExponent = minimumDigitsForExponent;
        _paramsChanged.invoke(this, EventArgs.Empty);
    }
    
    public int getMinimumDigitsForExponent() {
        return _minimumDigitsForExponent;
    }
    
    // endregion
    
    // region MaximumIntegerDigits
    
    private int _maximumIntegerDigits = _minimumDigitsForExponent + 1;
    
    public void setMaximumIntegerDigits(final int maximumIntegerDigits) {
        if (_maximumIntegerDigits == maximumIntegerDigits) { return; }
        _maximumIntegerDigits = maximumIntegerDigits;
        _decimalFormat.setMaximumIntegerDigits(_maximumIntegerDigits);
        _paramsChanged.invoke(this, EventArgs.Empty);
    }
    
    public int getMaximumIntegerDigits() {
        return _maximumIntegerDigits;
    }
    
    // endregion
    
    // region MaximumIntegerDigits
    
    private int _maximumFractionDigits = _minimumDigitsForExponent + 1;
    
    public void setMaximumFractionDigits(final int maximumFractionDigits) {
        if (_maximumFractionDigits == maximumFractionDigits) { return; }
        _maximumFractionDigits = maximumFractionDigits;
        _decimalFormat.setMaximumFractionDigits(_maximumFractionDigits);
        _paramsChanged.invoke(this, EventArgs.Empty);
    }
    
    public int getMaximumFractionDigits() {
        return _maximumFractionDigits;
    }
    
    // endregion
    
    // region MaximumIntegerDigits
    
    private int _minimumFractionDigits = 0;
    
    public void setMinimumFractionDigits(final int minimumFractionDigits) {
        if (_minimumFractionDigits == minimumFractionDigits) { return; }
        _minimumFractionDigits = minimumFractionDigits;
        _decimalFormat.setMinimumFractionDigits(_minimumFractionDigits);
        _paramsChanged.invoke(this, EventArgs.Empty);
    }
    
    public int getMiniimumFractionDigits() {
        return _minimumFractionDigits;
    }

    // endregion

    // endregion

    // region Events

    public final IEventHandler _paramsChanged = new EventHandler();

    @Override
    public IEventHandler ParamsChanged() {
        return _paramsChanged;
    }

    // endregion

}
