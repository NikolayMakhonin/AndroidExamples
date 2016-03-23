package nikolay_makhonin.android.views.markerline;

import android.graphics.Canvas;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IRangeLocker;

public abstract class MarkerLine extends MarkerBase {
    protected final int _middleSpaceHeight;
    private static final int minLineSpace = 5;
    
    public MarkerLine(IDrawState drawState, int drawOrder, int middleSpaceHeight) {
        super(drawState, drawOrder);
        _middleSpaceHeight = middleSpaceHeight;
    }
    
    @Override
    protected int calcWidth(int level) {
        return 1 + level;
    }

    @Override
    protected int calcSpaceWidth(int level) {
        int space = getWidth(level);
        if (space < minLineSpace) space = minLineSpace;
        return space * 2;
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
        int maxCountLevels = _drawState.drawSize().getMaxCountLevels();
        int level = getLevel();
        
        float minLineLength = (float)((measureHeight - _middleSpaceHeight) * 0.5 / maxCountLevels);
        float lineLength = minLineLength * (level + 1);
        
        double start = Math.floor(minDisplayValue / delta) * delta;
        float lineHalfWidth = getWidth() * 0.5f;

        double displayWidthCoef = 1.f / (maxDisplayValue - minDisplayValue);
        
        float posX = position[0];
        float posY = position[1];
        
        int i = 0;
        while (true) {
            double value = start + i * delta;
            
            float x = posX + (float)((value - minDisplayValue) * displayWidthCoef) * measureWidth;
            float x1 = x - lineHalfWidth;
            float x2 = x + lineHalfWidth;
            
            if (rangeLocker == null || rangeLocker.lock(x1, x2)) {
                canvas.drawRect(x1, posY, x2, posY + lineLength, _paintLine);
                canvas.drawRect(x1, posY + measureHeight - lineLength, x2, posY + measureHeight, _paintLine);
            }
            
            if (value > maxDisplayValue) break;
            i++;
        }
    }    
}
