package nikolay_makhonin.android.views.markerline;

import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseIntArray;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IMarker;
import nikolay_makhonin.android.views.markerline.contracts.IRangeLocker;
import nikolay_makhonin.androidcodeexample.R;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.EventHandler;
import nikolay_makhonin.utils.events.IEventHandler;

public abstract class MarkerBase extends DrawStateBindEventsClass implements IMarker {
    private         int            _level;
    private         Double         _maxDeltaPixels;
    private         Double         _delta;
    private         Integer        _minWidth;
    private         Integer        _minSpaceWidth;
    private         Integer        _width;
    private         Integer        _spaceWidth;
    protected final IDrawState     _drawState;
    private         boolean        _bStyleLoaded;
    private final   SparseIntArray _widthCache;
    private final   SparseIntArray _spaceWidthCache;
    protected Paint _paintLine = new Paint();
    private final int _drawOrder;

    public MarkerBase(IDrawState drawState, int drawOrder) {
        super(drawState);
        _drawState = drawState;
        _drawOrder = drawOrder;
        _widthCache = new SparseIntArray(4);// new HashMap<Integer, Integer>(4);
        _spaceWidthCache = new SparseIntArray(4);
    }

    @Override
    public int DrawOrder() {
        return _drawOrder;
    }

    private void loadStyle() {
        if (_bStyleLoaded)
            return;
        _bStyleLoaded = true;
        if (_drawState.drawStyle().getAttributes() != null) {
            Theme theme = _drawState.drawStyle().getContext().getTheme();
            TypedArray a = theme.obtainStyledAttributes(_drawState.drawStyle().getAttributes(), R.styleable.Styles, _drawState.drawStyle().getDefStyleAttr(), 0);
            TypedArray appearance = null;
            int apResourceId = a.getResourceId(R.styleable.Styles_MarkerLineStyle, -1);
            a.recycle();
            if (apResourceId != -1)
            {
                appearance = theme.obtainStyledAttributes(apResourceId, R.styleable.MarkerLineStyle);
            }
            if (appearance != null)
            {
                setStyle(appearance);
                appearance.recycle();
            }
        }
    }
    
    protected void setStyle(TypedArray a) {
        _paintLine.setColor(a.getColor(R.styleable.MarkerLineStyle_lineColor, Color.BLACK));
    }
    
    protected void recalcMinWidths() {
        _widthCache.delete(0);
        _spaceWidthCache.delete(0);
        _minWidth = null; 
        _minSpaceWidth = null;  
        _maxDeltaPixelsChanged.invoke(this, EventArgs.Empty);
    }
    
    @Override
    public void setLevel(int level) {
        if (_level == level) return;
        _level = level;
        _width = null;
        _spaceWidth = null;
    }

    @Override
    public double roundValueByMarker(double value) {
        double delta = getDelta();
        return Math.round(value / delta) * delta;
    }
    
    @Override
    public int getLevel() {
        return _level;
    }

    protected abstract double calcDelta();
    public double getDelta() {
        return (_delta != null) ? _delta : (_delta = calcDelta());
    }

    protected abstract int calcWidth(int level);
    protected abstract int calcSpaceWidth(int level);

    protected int getWidth(int level) {
        if (_widthCache.indexOfKey(level) >= 0) return _widthCache.get(level);
        int width = calcWidth(level);
        _widthCache.put(level, width);
        return width;
    }

    private int getSpaceWidth(int level) {
        if (_spaceWidthCache.indexOfKey(level) >= 0) return _spaceWidthCache.get(level);
        int width = calcSpaceWidth(level);
        _spaceWidthCache.put(level, width);
        return width;
    }
    
    private int getMinWidth() {
        return (_minWidth != null) ? _minWidth : (_minWidth = getWidth(0));
    }

    private int getMinSpaceWidth() {
        return (_minSpaceWidth != null) ? _minSpaceWidth : (_minSpaceWidth = getSpaceWidth(0));
    }

    public int getWidth() {
        return (_width != null) ? _width : (_width = getWidth(getLevel()));
    }

    public int getSpaceWidth() {
        return (_spaceWidth != null) ? _spaceWidth : (_spaceWidth = getSpaceWidth(getLevel()));
    }

    @Override
    public double getMaxDeltaPixels() {
        if (_maxDeltaPixels != null) return _maxDeltaPixels;
        return getDelta() / (getMinWidth() + getMinSpaceWidth());
    }

    protected final IEventHandler _maxDeltaPixelsChanged = new EventHandler();

    @Override
    public IEventHandler maxDeltaPixelsChanged() {
        return _maxDeltaPixelsChanged;
    }

    @Override
    public boolean allowDraw(int newWidth) {
        return getDelta() >= (Math.max(getWidth(), newWidth) + getSpaceWidth()) * _drawState.getDeltaPixel();
    }

    protected abstract void drawPrivate(Canvas canvas, int[] position, IRangeLocker rangeLocker);

    @Override
    public void draw(Canvas canvas, int[] position, IRangeLocker rangeLocker) {
        loadStyle();
        drawPrivate(canvas, position, rangeLocker);
    }
}
