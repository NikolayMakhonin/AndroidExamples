package nikolay_makhonin.android.views.markerline;

import nikolay_makhonin.android.views.markerline.contracts.DrawSizeChangedEventArgs;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.ValueRangeChangedEventArgs;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.IEventListener;

public class DrawStateBindEventsClass {
    public DrawStateBindEventsClass(IDrawState drawState) {
        drawState.deltaPixelChanged().add(deltaPixelChanged_listener);
        drawState.drawSize().drawSizeChanged().add(drawRectChanged_listener);
        drawState.drawSize().maxCountLevelsChanged().add(maxCountLevelsChanged_listener);
        drawState.drawData().valueRangeChanged().add(valueRangeChanged_listener);
        drawState.drawData().selectedRangeChanged().add(selectedRangeChanged_listener);
    }
    
    protected void onValueRangeChanged() {}
    
    protected void onSelectedRangeChanged() {}
    
    protected void onDrawSizeChanged() {}
    
    protected void onMaxCountLevelsChanged() {}
    
    protected void onDeltaPixelChanged() {}

    IEventListener<DrawSizeChangedEventArgs> drawRectChanged_listener = new IEventListener<DrawSizeChangedEventArgs>() {
        @Override
        public boolean onEvent(Object o, DrawSizeChangedEventArgs e) {
            onDrawSizeChanged();
            return true;
        }
    };

    IEventListener<EventArgs> maxCountLevelsChanged_listener = new IEventListener<EventArgs>() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            onMaxCountLevelsChanged();
            return true;
        }
    };

    IEventListener<EventArgs> deltaPixelChanged_listener = new IEventListener<EventArgs>() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            onDeltaPixelChanged();
            return true;
        }
    };

    IEventListener<ValueRangeChangedEventArgs> valueRangeChanged_listener
        = new IEventListener<ValueRangeChangedEventArgs>() {
        @Override
        public boolean onEvent(Object o, ValueRangeChangedEventArgs e) {
            onValueRangeChanged();
            return true;
        }
    };

    IEventListener<EventArgs> selectedRangeChanged_listener = new IEventListener<EventArgs>() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            onSelectedRangeChanged();
            return true;
        }
    };
}
