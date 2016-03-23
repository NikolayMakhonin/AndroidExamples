package nikolay_makhonin.android.views.markerline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

//import nikolay_makhonin.utils.Serialization.BinaryReader;
//import nikolay_makhonin.utils.Serialization.BinaryWriter;
//import nikolay_makhonin.utils.Serialization.IStreamSerializable;
//import nikolay_makhonin.utils.Serialization.ParcelSavedState;
import nikolay_makhonin.android.views.adapters.touchstate.PointState;
import nikolay_makhonin.android.views.adapters.touchstate.TouchEventArgs;
import nikolay_makhonin.android.views.adapters.touchstate.TouchState;
import nikolay_makhonin.android.views.adapters.touchstate.TouchStateBase;
import nikolay_makhonin.android.views.adapters.touchstate.TouchStateController;
import nikolay_makhonin.android.views.markerline.contracts.IDrawDataState;
import nikolay_makhonin.android.views.markerline.contracts.IDrawSizeState;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IDrawStyleState;
import nikolay_makhonin.android.views.markerline.contracts.IMarker;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerLayer;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerLayout;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerRender;
import nikolay_makhonin.utils.DoubleStringConverter;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.IEventHandler;
import nikolay_makhonin.utils.events.IEventListener;
import nikolay_makhonin.utils.logger.Log;

public abstract class MarkerLineViewBase extends View //implements IStreamSerializable
{
    private   List<IMarkerLayout> _markerLayouts;
    protected IDrawState          _drawState;

    //region Initialize
    public MarkerLineViewBase(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public MarkerLineViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public MarkerLineViewBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private DoubleStringConverter _stringConverter = new DoubleStringConverter();
    private TouchStateController _touchStateController;

    protected IDrawStyleState createDrawStyleState(Context context, AttributeSet attrs, int defStyleAttr) {
        return new DrawStyleState(context, attrs, defStyleAttr, isInEditMode());
    }

    protected IDrawSizeState createDrawSizeState() {
        return new DrawSizeState();
    }

    protected IDrawDataState createDrawDataState() {
        return new DrawDataState();
    }

    protected void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        _drawState = new DrawState(createDrawStyleState(context, attrs, defStyleAttr), createDrawSizeState(),
            createDrawDataState());
        _drawState.drawData().setValueRange(-5, 5);
        _drawState.drawSize().setDrawSize(this.getMeasuredWidth(), this.getMeasuredHeight());
        _drawState.drawSize().setMaxCountLevels(3);
        _touchStateController = new TouchStateController(this);
        //_drawState.deltaPixelChanged().add(_drawStateChanged_listener);
        _touchStateController.TouchEvent().add(_touch_listener);
        //_touchStateController.BindCustomStates(_multiTouchValues);
        _drawState.drawSize().drawSizeChanged().add(_drawStateChanged_listener);
        _drawState.drawSize().maxCountLevelsChanged().add(_drawStateChanged_listener);
        _drawState.drawData().valueRangeChanged().add(_drawStateChanged_listener);
        _drawState.drawData().selectedRangeChanged().add(_drawStateChanged_listener);
    }

    private final int   minMargin  = 4;
    private       float marginCoef = 0.5f;

    protected int calcMargin(int size) {
        return Math.max(minMargin, Math.round(size * marginCoef));
    }

    private List<IMarkerLayout> getMarkerLayouts() {
        if (_markerLayouts != null)
            return _markerLayouts;
        List<IMarkerLayout> markerLayouts = new ArrayList<IMarkerLayout>();
        fillMarkerLayouts(markerLayouts);
        return _markerLayouts = markerLayouts;
    }

    protected IMarkerLayout createMarkerLayout() {
        return new MarkerLayout(_drawState);
    }

    protected abstract void fillMarkerLayouts(List<IMarkerLayout> markerLayouts);

    //endregion

    //region Draw

    public void ReDraw() {
        invalidate();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
	    drawMarkers(canvas);	    
	}
	   
    private void drawMarkers(Canvas canvas) {
        List<IMarkerLayout> markerLayouts = getMarkerLayouts();
        int[] drawSize = _drawState.drawSize().getDrawSize();
        Rect rect = new Rect(0, 0, drawSize[0], drawSize[1]);
        for (IMarkerLayout markerLayout : markerLayouts) {
            MarkerLayoutAlign align = markerLayout.getAlign();
            IMarkerRender render = markerLayout.Render();
            IDrawState layoutDrawState = markerLayout.DrawState();
            int[] position;
            int fixWidth, fixHeight;
            switch(align) {
                case Top:
                    fixHeight = layoutDrawState.drawSize().getDrawSize()[1];
                    layoutDrawState.drawSize().setDrawSize(rect.width(), fixHeight);
                    position = new int[] { rect.left, rect.top };
                    if (render.draw(canvas, position)) rect.top += fixHeight;
                    break;
                case Bottom:
                    fixHeight = layoutDrawState.drawSize().getDrawSize()[1];
                    layoutDrawState.drawSize().setDrawSize(rect.width(), fixHeight);
                    position = new int[] { rect.left, rect.top + rect.height() - fixHeight };                    
                    if (render.draw(canvas, position)) rect.bottom -= fixHeight;
                    break;
                case Left:
                    fixWidth = layoutDrawState.drawSize().getDrawSize()[0];
                    layoutDrawState.drawSize().setDrawSize(fixWidth, rect.height());
                    position = new int[] { rect.left, rect.top };
                    if (render.draw(canvas, position)) rect.left += fixWidth;
                    break;
                case Right:
                    fixWidth = layoutDrawState.drawSize().getDrawSize()[0];
                    layoutDrawState.drawSize().setDrawSize(fixWidth, rect.height());
                    position = new int[] { rect.left + rect.width() - fixWidth, rect.top };                    
                    if (render.draw(canvas, position)) rect.right -= fixWidth;
                    break;
                case Fill:
                    layoutDrawState.drawSize().setDrawSize(rect.width(), rect.height());
                    position = new int[] { rect.left, rect.top };
                    render.draw(canvas, position);
                    break;
                default:
                    Log.e("MarkerLineViewBase", "onDraw, unknown MarkerLayoutAlign: " + align);
                    continue;
            }
        }
	}

    //endregion

    //region Event Handlers

    protected Double[]            _touchValues    = new Double[TouchStateBase.MAX_POINTER_COUNT];
    private   EnumSet<TouchState> _downMoveStates = EnumSet.of(TouchState.Down, TouchState.Move);

    private enum RangeValueType {
        Min,
        Max
    }

    private RangeValueType _currentMoveSelectValueType;

    private void UpdateCurrentMoveSelectValueType(double value) {
        double[] selectedRange = _drawState.drawData().getSelectedRange();
        if (selectedRange == null) {
            _currentMoveSelectValueType = RangeValueType.Max;
        } else {
            double minValue = Math.min(selectedRange[0], selectedRange[1]);
            double maxValue = Math.max(selectedRange[0], selectedRange[1]);
            if (Math.abs(value - minValue) < Math.abs(value - maxValue)) {
                _currentMoveSelectValueType = RangeValueType.Min;
            } else {
                _currentMoveSelectValueType = RangeValueType.Max;
            }
        }
    }

    IEventListener<TouchEventArgs> _touch_listener = new IEventListener<TouchEventArgs>() {
        @Override
        public boolean onEvent(Object o, TouchEventArgs e) {
            int        pointIndex = e.PointIndexes[0];
            PointState pointState = e.State.getPointState(pointIndex);
            int        pointCount = e.State.getPointCount();

            try {
                TouchState touchState = pointState.getTouchState();
                switch (touchState) {
                    case Down:
                        _touchValues[pointState.getId()] = pointToValue(pointState.getDownX());
                    case ClickDown:
                        if (pointCount == 1 && e.State.isClickDownMode()) {
                            UpdateCurrentMoveSelectValueType(pointToValue(pointState.getDownX()));
                        }
                    case Move:
                    case ClickDownMove:
                        PointState p0 = e.State.getPointState(0);
                        if (!e.State.isClickDownMode()) {
                            if (touchState != TouchState.Down) {
                                if (pointCount > 1) {
                                    PointState p1 = e.State.getPointState(1);
                                    zoomToPoints(p0.getMoveX(), p1.getMoveX(), _touchValues[p0.getId()],
                                        _touchValues[p1.getId()]);
                                } else {
                                    moveToPoint(p0.getMoveX(), _touchValues[p0.getId()]);
                                }
                            }
                        } else {
                            if (pointCount > 1) {
                                PointState p1 = e.State.getPointState(1);
                                moveSelectedValueRange(pointToValue(p0.getMoveX()), pointToValue(p1.getMoveX()));
                            } else {
                                moveSelectedValue(pointToValue(p0.getMoveX()));
                            }
                        }
                        break;
                    case Click:
                    case DoubleClick:
                    case Up:
                        if (e.State.isClickDownMode() && pointCount == 2) {
                            PointState p = e.State.getPointState(pointIndex == 0 ? 1 : 0);
                            UpdateCurrentMoveSelectValueType(pointToValue(p.getMoveX()));
                        }
                        break;
                }
            } catch (Exception exception) {
                Log.e("MarkerLineViewBase", "", exception);
            }

            return true;
        }
    };

    protected IEventListener _drawStateChanged_listener = new IEventListener() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            invalidate();
            return true;
        }
    };

    IEventListener<EventArgs> onStringConverterChanged_listener = new IEventListener<EventArgs>() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            return onStringConverterChanged(o, e);
        }
    };

    private boolean onStringConverterChanged(Object o, EventArgs e) {
        invalidate();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        _drawState.drawSize().setDrawSize(this.getMeasuredWidth(), this.getMeasuredHeight());
    }

    private int currentVersion = 0;

//    public void Serialize(BinaryWriter writer) {
//        writer.write(currentVersion);
//        double[] valueRange = _drawState.drawData().getValueRange();
//        writer.write(valueRange[0]);
//        writer.write(valueRange[1]);
//        writer.write(_drawState.drawSize().getMaxCountLevels());
//    }
//
//    public Object DeSerialize(BinaryReader reader) {
//        int version = reader.readInt();
//        _drawState.drawData().setValueRange(new double[] { reader.readDouble(), reader.readDouble() });
//        _drawState.drawSize().setMaxCountLevels(reader.readInt());
//        return this;
//    }
//
//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//
//        boolean save = true;
//        if (save) {
//            return new ParcelSavedState(superState).saveObject(this);
//        }
//
//        return superState;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        if (!(state instanceof ParcelSavedState)) {
//            super.onRestoreInstanceState(state);
//            return;
//        }
//
//        ParcelSavedState thisState = (ParcelSavedState) state;
//        super.onRestoreInstanceState(thisState.getSuperState());
//
//        thisState.loadObject(this);
//    }

    private double pointToValue(float X) {
        double[] valueRange = _drawState.drawData().getValueRange();
        double value = X * Math.abs(valueRange[1] - valueRange[0]) / _drawState.drawSize().getDrawSize()[0] + Math
            .min(valueRange[1], valueRange[0]);
        return value;
    }

    private void zoomToPoints(float X, float Y, double valueToX, double valueToY) {
        double x1              = X;
        double y1              = Y;
        double x0              = valueToX;
        double y0              = valueToY;
        double _1_x1y1         = 1.0 / (x1 - y1);
        double minDisplayValue = (x1 * y0 - x0 * y1) * _1_x1y1;
        double maxDisplayValue = minDisplayValue + (x0 - y0) * _1_x1y1 * _drawState.drawSize().getDrawSize()[0];
        _drawState.drawData().setValueRange(minDisplayValue, maxDisplayValue);
    }

    private void moveToPoint(float X, double valueToX) {
        double   x1              = X;
        double   x0              = valueToX;
        double[] valueRange      = _drawState.drawData().getValueRange();
        double   c               = Math.abs(valueRange[1] - valueRange[0]);
        double   minDisplayValue = x0 - x1 / _drawState.drawSize().getDrawSize()[0] * c;
        double   maxDisplayValue = minDisplayValue + c;
        _drawState.drawData().setValueRange(minDisplayValue, maxDisplayValue);
    }

    protected void moveSelectedValue(double newValue) {
        double[] selectedRange = _drawState.drawData().getSelectedRange();
        double   roundValue    = roundValueByMarker(newValue);
        if (selectedRange == null) {
            _drawState.drawData().setSelectedRange(roundValue, roundValue);
        } else {
            double minValue = Math.min(selectedRange[0], selectedRange[1]);
            double maxValue = Math.max(selectedRange[0], selectedRange[1]);
            switch (_currentMoveSelectValueType) {
                case Max:
                    if (newValue < minValue) {
                        _currentMoveSelectValueType = RangeValueType.Min;
                        _drawState.drawData().setSelectedRange(roundValue, minValue);
                    } else {
                        _drawState.drawData().setSelectedRange(minValue, roundValue);
                    }
                    break;
                case Min:
                    if (newValue > maxValue) {
                        _currentMoveSelectValueType = RangeValueType.Max;
                        _drawState.drawData().setSelectedRange(maxValue, roundValue);
                    } else {
                        _drawState.drawData().setSelectedRange(roundValue, maxValue);
                    }
                    break;
            }
        }
    }

    protected void moveSelectedValueRange(double newValue1, double newValue2) {
        _drawState.drawData().setSelectedRange(roundValueByMarker(Math.min(newValue1, newValue2)),
            roundValueByMarker(Math.max(newValue1, newValue2)));
    }

    protected abstract IMarkerLayer getMarkerLayerForRoundValue();

    protected abstract int getMaxCountMarkerLevelsForRoundValue();

    protected double roundValueByMarker(double value) {
        List<IMarker> markers       = getMarkerLayerForRoundValue().getDrawMarkers();
        double        minDelta      = Double.MAX_VALUE;
        double        minRoundValue = value;
        int           count         = markers.size();
        for (int i = Math.max(0, count - getMaxCountMarkerLevelsForRoundValue()); i < count; i++) {
            double roundValue = markers.get(i).roundValueByMarker(value);
            double delta = Math.abs(value - roundValue);
            if (delta < minDelta) {
                minDelta = delta;
                minRoundValue = roundValue;
            }
        }
        return minRoundValue;
    }
    //endregion

    //region Properties

    //region DrawState

    public IDrawState getDrawState() {
        return _drawState;
    }

    //endregion

    //endregion

    public final IEventHandler<TouchEventArgs> TouchEvent() {
        return _touchStateController.TouchEvent();
    }
}
