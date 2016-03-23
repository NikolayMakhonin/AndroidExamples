package nikolay_makhonin.android.views.markerline;

import android.graphics.Canvas;
import android.graphics.Paint;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IRangeLocker;
import nikolay_makhonin.utils.contracts.IParamsChangedStringConverter;

public abstract class MarkerTextFixDelta extends MarkerText {
    private double _delta;
    
    public MarkerTextFixDelta(IDrawState drawState, int drawOrder, double delta, IParamsChangedStringConverter stringConverter, boolean drawBetweenMarkers) {
        super(drawState, drawOrder, stringConverter, drawBetweenMarkers);
        _delta = delta;
    }

    @Override
    protected double calcDelta() {
        return _delta;
    }
    
    protected String valueToString(double value) {
        return _stringConverter.ConvertToString(value);
    }
        
    @Override
    protected void drawPrivate(Canvas canvas, int[] position, IRangeLocker rangeLocker) {
        double delta = getDelta();
        double[] valueRange = _drawState.drawData().getValueRange();
        double maxDisplayValue = Math.max(valueRange[0], valueRange[1]);
        double minDisplayValue = Math.min(valueRange[0], valueRange[1]);
        int[] drawSize = _drawState.drawSize().getDrawSize();
        int measureWidth = drawSize[0];
        int measureHeight = drawSize[1];

        double displayWidthCoef = measureWidth / (maxDisplayValue - minDisplayValue);
        double start = Math.floor(minDisplayValue / delta) * delta;
        int count = (int)Math.ceil((maxDisplayValue - start) / delta);
        
        Paint paint = getPaint(getLevel()); 
        
        float posX = position[0];
        float posY = position[1];
        
        double value2 = start;
        float x2 = 0;
        for(int i=0; i < count; i++) {
            double value = value2;
            value2 = start + (i + 1) * delta;
            float x1 = i > 0 ? x2 : (float)((value - minDisplayValue) * displayWidthCoef);
            x2 = (float)((value2 - minDisplayValue) * displayWidthCoef);
            String text = valueToString(!_drawBetweenMarkers || value >= 0 ? value : value2);

            drawText(canvas, paint, rangeLocker, text, posX + x1, posX + x2, posY, measureHeight);
        }   
    }
}
