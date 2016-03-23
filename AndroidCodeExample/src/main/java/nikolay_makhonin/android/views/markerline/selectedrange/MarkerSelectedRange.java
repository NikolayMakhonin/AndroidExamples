package nikolay_makhonin.android.views.markerline.selectedrange;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import nikolay_makhonin.android.views.markerline.MarkerBase;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IRangeLocker;
import nikolay_makhonin.androidcodeexample.R;

public class MarkerSelectedRange extends MarkerBase {
    private Paint _selectedRangePaint = new Paint();
    private Paint _selectedRangePointPaint = new Paint();
    private final float _pointRadius;
    private static final int MIN_RECT_WIDTH = 3;
    
    public MarkerSelectedRange(IDrawState drawState, int drawOrder, int pointDiameter) {
        super(drawState, drawOrder);
        _pointRadius = pointDiameter * 0.5f;
    }
    
    protected void setStyle(TypedArray a) {
        _selectedRangePaint.setColor(a.getColor(R.styleable.MarkerLineStyle_selectedRangeColor, Color.argb(100, 255, 255, 255)));
        _selectedRangePointPaint.setColor(a.getColor(R.styleable.MarkerLineStyle_selectedRangePointColor, Color.argb(100, 255, 255, 255)));
    }

    @Override
    protected double calcDelta() {
        return 0;
    }

    @Override
    protected int calcWidth(int level) {
        return 0;
    }

    @Override
    protected int calcSpaceWidth(int level) {
        return 0;
    }

    @Override
    protected void drawPrivate(Canvas canvas, int[] position, IRangeLocker rangeLocker) {
        double[] valueRange = _drawState.drawData().getValueRange();
        double[] selectedRange = _drawState.drawData().getSelectedRange();
        if (selectedRange == null || valueRange == null) return;
        double min = Math.min(valueRange[0], valueRange[1]);
        double max = Math.max(valueRange[0], valueRange[1]);
        
        int[] drawSize = _drawState.drawSize().getDrawSize();
        double _w_max_min = drawSize[0] / (max - min);
        float x1 = position[0] + (float)Math.round((selectedRange[0] - min) * _w_max_min);
        float x2 = position[0] + (float)Math.round((selectedRange[1] - min) * _w_max_min);
        
        if (Math.abs(x2 - x1) >= MIN_RECT_WIDTH) {
            canvas.drawRect(x1, position[1], x2, position[1] + drawSize[1], _selectedRangePaint);
        } else {
            float x = (x1 + x2) / 2;
            canvas.drawRect(x - MIN_RECT_WIDTH * 0.5f, position[1], x + MIN_RECT_WIDTH * 0.5f, position[1] + drawSize[1], _selectedRangePointPaint);
        }
        canvas.drawCircle(x1, position[1] + drawSize[1] * 0.5f, _pointRadius, _selectedRangePointPaint);
        canvas.drawCircle(x2, position[1] + drawSize[1] * 0.5f, _pointRadius, _selectedRangePointPaint);
    }
    
    @Override
    public double getMaxDeltaPixels() {
        return Double.MAX_VALUE;
    }
}
