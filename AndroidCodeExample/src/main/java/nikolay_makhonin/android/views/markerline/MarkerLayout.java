package nikolay_makhonin.android.views.markerline;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerLayout;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerRender;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.IEventListener;
import nikolay_makhonin.utils.logger.Log;

public class MarkerLayout implements IMarkerLayout {
    private IDrawState _bindDrawState;
    private IDrawState _drawState;
    private MarkerLayoutAlign _align = MarkerLayoutAlign.Fill;
    private IMarkerRender _render;

    public MarkerLayout(IDrawState drawState) {
        _bindDrawState = drawState;
        _drawState = new DrawState(drawState.drawStyle(), new DrawSizeState(), drawState.drawData());
        _render = new MarkerRender(_drawState);
        int[] drawSize = drawState.drawSize().getDrawSize();
        _drawState.drawSize().setDrawSize(drawSize == null ? null : drawSize.clone());
        _drawState.drawSize().setMaxCountLevels(drawState.drawSize().getMaxCountLevels());
        drawState.drawSize().drawSizeChanged().add(_drawSizeChanged_listener);
    }

    IEventListener _drawSizeChanged_listener = new IEventListener() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            onDrawSizeChanged();
            return false;
        }
    };

    private void onDrawSizeChanged() {
        switch (_align) {
            case Top:
            case Bottom:
                _drawState.drawSize()
                    .setDrawSize(_bindDrawState.drawSize().getDrawSize()[0], _drawState.drawSize().getDrawSize()[1]);
                break;
            case Left:
            case Right:
                _drawState.drawSize()
                    .setDrawSize(_drawState.drawSize().getDrawSize()[0], _bindDrawState.drawSize().getDrawSize()[1]);
                break;
            case Fill:
                _drawState.drawSize().setDrawSize(_bindDrawState.drawSize().getDrawSize()[0],
                    _bindDrawState.drawSize().getDrawSize()[1]);
                break;
            default:
                Log.e("MarkerLineViewBase", "onDraw, unknown MarkerLayoutAlign: " + _align);
                break;
        }
    }

    @Override
    public IMarkerRender Render() {
        return _render;
    }

    @Override
    public MarkerLayoutAlign getAlign() {
        return _align;
    }

    @Override
    public void setAlign(MarkerLayoutAlign align) {
        _align = align;
    }
    
    @Override
    public IDrawState DrawState() {
        return _drawState;
    }
}
