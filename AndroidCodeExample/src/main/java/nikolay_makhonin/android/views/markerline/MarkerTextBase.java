package nikolay_makhonin.android.views.markerline;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IRangeLocker;
import nikolay_makhonin.androidcodeexample.R;
import nikolay_makhonin.utils.DrawStringUtils;

public abstract class MarkerTextBase extends MarkerBase {
    private final Paint _paintTextHighlighted = new Paint();
    private final Paint _paintText = new Paint();
    private Integer _maxDrawHeight;
    protected final boolean _drawBetweenMarkers;
    
    public MarkerTextBase(IDrawState drawState, int drawOrder, boolean drawBetweenMarkers) {
        super(drawState, drawOrder);
        _drawBetweenMarkers = drawBetweenMarkers;
    }

    protected Paint getPaint(int level) {
        switch (level) {
            case 0: return _paintText;
            case 1: return _paintTextHighlighted;
            default: return _paintTextHighlighted;
        }
    }
    
    @Override
    protected void setStyle(TypedArray a) {
        super.setStyle(a);
        _paintTextHighlighted.setColor(a.getColor(R.styleable.MarkerLineStyle_textHighlightedColor, Color.BLUE));
        _paintText.setColor(a.getColor(R.styleable.MarkerLineStyle_textColor, Color.BLACK));

        _paintTextHighlighted.setTextSize(_paintText.getTextSize() * 1.3f);
        Typeface typeface = _paintText.getTypeface();
        if (!_drawState.drawStyle().isInEditMode()) {
            _paintTextHighlighted.setTypeface(Typeface.create(typeface, Typeface.BOLD));
        }
    }

    @Override
    protected void onMaxCountLevelsChanged() {
        super.onMaxCountLevelsChanged();
        _maxDrawHeight = null;
    }   

    @Override
    protected abstract int calcWidth(int level);

    @Override
    protected int calcSpaceWidth(int level) {
        return DrawStringUtils.getTextDrawWidth("WW", getPaint(level));
    }    
        
    public int getMaxDrawHeight() {
        if (_maxDrawHeight != null) return _maxDrawHeight;
        return _maxDrawHeight = getMaxTextDrawHeight("0lqjyIi");
    }
    
    private int getMaxTextDrawHeight(String text) {
        Rect bounds = new Rect();
        int maxHeight = 0;
        int countLevels = _drawState.drawSize().getMaxCountLevels();
        for (int i = 0; i < countLevels; i++) {
            getPaint(i).getTextBounds(text, 0, text.length(), bounds);
            int height = bounds.height();
            if (height > maxHeight) maxHeight = height;
        }
        return maxHeight;      
    }
        
    private Rect getTextBounds(String text, Paint paint) {
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds;
    }
    
    protected void drawText(Canvas canvas, Paint paint, IRangeLocker rangeLocker, String text, float xCurrent, float xNext, float posY, float measureHeight) {
        Rect textBounds = getTextBounds(text, paint);
        int textWidth = textBounds.width();
        int spaceWidth = getSpaceWidth();
        float y = posY + (float) ((measureHeight - textBounds.top) * 0.5);
        
        float x1, x2;
        if (_drawBetweenMarkers) {
            int measureWidth = _drawState.drawSize().getDrawSize()[0];
            x1 = xCurrent;
            x2 = xNext;
            if (x1 < 0) x1 = 0;
            if (x2 > measureWidth) x2 = measureWidth;
            if (x2 - x1 >= textWidth + spaceWidth) {
                float x = (x2 + x1) * 0.5f;
                x1 = x - textWidth * 0.5f;
                x2 = x + textWidth * 0.5f;
            } else if (x2 < measureWidth) {
                x2 = x2 - spaceWidth * 0.5f;
                x1 = x2 - textWidth;
            } else {
                x1 = x1 + spaceWidth * 0.5f;
                x2 = x1 + textWidth;
            }
        } else {
            x1 = xCurrent - textWidth * 0.5f;
            x2 = x1 + textWidth;
        }
        
        if (rangeLocker == null || rangeLocker.lock(x1 - spaceWidth * 0.5f, x2 + spaceWidth * 0.5f)) {
            if (_drawBetweenMarkers) {
    //            float x = xCurrent;
    //            float lineHalfWidth = 1.f;
    //            float lineX1 = x - lineHalfWidth;
    //            float lineX2 = x + lineHalfWidth;
    //            
    //            if (rangeLocker == null || rangeLocker.lock(x1, x2)) {
    //                canvas.drawRect(lineX1, posY, lineX2, posY + measureHeight, _paintLine);
    //            }
                float lineX1 = xCurrent;
                float lineX2 = xNext - 1;
                float lineY1 = posY;
                float lineY2 = posY + measureHeight;
                
                canvas.drawLine(lineX1, lineY1, lineX2, lineY1, _paintLine);
                canvas.drawLine(lineX2, lineY1, lineX2, lineY2, _paintLine);
                canvas.drawLine(lineX2, lineY2, lineX1, lineY2, _paintLine);
                canvas.drawLine(lineX1, lineY2, lineX1, lineY1, _paintLine);
            }
            
            canvas.drawText(text, x1, y, paint);
        }
    }
}
