package nikolay_makhonin.android.views.adapters.touchstate;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import nikolay_makhonin.utils.ArrayUtils;
import nikolay_makhonin.utils.events.EventHandler;
import nikolay_makhonin.utils.events.IEventHandler;
import nikolay_makhonin.utils.logger.Log;

public class TouchStateController extends TouchStateBase {
    private static final int MAX_CLICK_DELAY = 500;
    private final View _view;
    private final Object _locker = new Object();
    
    public TouchStateController(View view) {
        _view = view;
        _view.setOnTouchListener(_touch_listener);
    }
        
    private OnTouchListener _touch_listener = new OnTouchListener() {        
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return onTouchEvent(v, event);
        }
    };
    
    private void logMotionEvent(MotionEvent event) {
        StringBuilder sb = new StringBuilder();
        int count = event.getPointerCount();
        sb.append("TouchIds: ");
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(", ");
            sb.append(event.getPointerId(i));
        }
        Log.d("TouchStateController", sb.toString());
    }
    
    private boolean onTouchEvent(View v, MotionEvent event) {
        synchronized(_locker) {
            long time = System.currentTimeMillis();
            
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    action = MotionEvent.ACTION_DOWN;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    action = MotionEvent.ACTION_UP;
                    break;
            }
            
            //logMotionEvent(event);
            
            Integer index = event.getActionIndex();
            switch(action) {
                case MotionEvent.ACTION_MOVE:
                    int pointCount = _downStates.size();
                    int[] indexes = new int[pointCount];
                    float[][] positions = new float[pointCount][];
                    TouchState[] touchStates = new TouchState[pointCount];
                    int nIndex = 0;
                    for (int i = 0; i < pointCount; i++) {
                        PointState downState = _downStates.get(i);
                        float[] movePosition = downState._movePosition;
                        float x = event.getX(i);
                        float y = event.getY(i);
                        if (movePosition != null && (movePosition[0] != x || movePosition[1] != y)) {
                            indexes[nIndex] = i;
                            touchStates[nIndex] = downState._touchState;
                            positions[nIndex] = new float[] { x, y };
                            nIndex++;
                        }
                    }    
                    if (nIndex > 0) {
                        if (_downStates.size() != event.getPointerCount()) {
                            Log.e("TouchStateController", "[Move] _downStates.size() != event.getPointerCount()");
                        }
                        onMove(ArrayUtils.copyOf(indexes, nIndex), positions, touchStates);
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    {
                        float minDistance = Float.MAX_VALUE;
                        PointState minUpState = null;
                        int minIndex = -1;
                        int count = _upStates.size();
                        float[] position = new float[] { event.getX(index), event.getY(index) };
                        for (int i = 0; i < count; i++) {
                            PointState upState = _upStates.get(i);
                            float[] movePosition = upState._movePosition;
                            float dx = position[0] - movePosition[0];
                            float dy = position[1] - movePosition[1];
                            float distance = dx * dx + dy * dy;
                            if (distance < minDistance) {
                                minIndex = i;
                                minDistance = distance; 
                                minUpState = upState;
                            }
                        }
                        
                        int id = event.getPointerId(index);
                        
                        PointState downState;
                        if (minUpState != null) {
                            downState = minUpState;
                            _upStates.RemoveAt(minIndex);
                            downState._id = id;
                        } else {
                            downState = new PointState(id);
                        }
                        
                        _downStates.add(downState);
                        if (_downStates.size() != event.getPointerCount()) {
                            Log.e("TouchStateController", "[Down] _downStates.size() != event.getPointerCount()");
                        }
                        if (_downStates.indexOf(downState) != index) {
                            Log.e("TouchStateController", "[Down] _downStates.indexOf(downState) != index");
                        }
                        onDown(index, position, downState, time);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    {
                        index = event.getActionIndex();
                        float[] position = new float[] { event.getX(index), event.getY(index) };
                        PointState downState = _downStates.get(index);
                        if (_downStates.size() != event.getPointerCount()) {
                            Log.e("TouchStateController", "[Up] _downStates.size() != event.getPointerCount()");
                        }
                        onUp(index, position, downState, time);
                        _downStates.RemoveAt(index);
                        _upStates.add(downState);
                        if (_downStates.size() == 0) _clickDownMode = false;
                    }
                    break;
            }
                    
            return true;
        }
    }
    
    private void onDown(int index, float[] position, PointState downState, long time) {
        downState._movePosition = position;
        downState._downPosition = position;
        downState._prevMovePosition = position;
        
        TouchState newTouchState;
        switch (downState._touchState) {
            case None:
            case Up:
                newTouchState = TouchState.Down;
                break;
            case Click:
            case DoubleClick:
                if (time - downState._upDownTime < MAX_CLICK_DELAY) {
                    newTouchState = TouchState.ClickDown;
                    _clickDownMode = true;
                } else {
                    newTouchState = TouchState.Down;
                }
                break;
            default:
                Log.e("TouchStateController", "[Down] Incorrect touchState: " + downState._touchState);
                newTouchState = TouchState.Down;
                break;
        }
        
        downState._touchState = newTouchState;
        downState._upDownTime = time;
        onTouch(index);
    }
    
    private void onMove(int[] indexes, float[][] positions, TouchState[] touchStates) {
        int count = indexes.length;
        for (int i = 0; i < count; i++) {
            int index = indexes[i];
            PointState downState = _downStates.get(index);
            downState._prevMovePosition = downState._movePosition;
            downState._movePosition = positions[i];
            
            TouchState newTouchState;
            switch (touchStates[i]) {
                case ClickDown:
                case ClickDownMove:
                    newTouchState = TouchState.ClickDownMove;
                    break;
                case Down:
                case Move:
                    newTouchState = TouchState.Move;
                    break;
                default:
                    Log.e("TouchStateController", "[Move] Incorrect touchState: " + touchStates[i]);
                    newTouchState = TouchState.Move;
                    break;
            }
            downState._touchState = newTouchState;
        }
        onTouch(indexes);
    }
    
    private void onUp(int index, float[] position, PointState downState, long time) {
        downState._prevMovePosition = downState._movePosition;
        downState._movePosition = position;
        
        TouchState newTouchState;
        if (time - downState._upDownTime < MAX_CLICK_DELAY) {
            switch (downState._touchState) {
                case Down:
                    newTouchState = TouchState.Click;
                    break;
                case ClickDown:
                    newTouchState = TouchState.DoubleClick;
                    break;
                case None:
                case Up:
                    Log.e("TouchStateController", "[Up] Incorrect touchState: " + downState._touchState);
                default:
                    newTouchState = TouchState.Up;
                    break;
            }
        } else {
            newTouchState = TouchState.Up;
        }
        
        downState._touchState = newTouchState;
        downState._upDownTime = time;
        onTouch(index);
    }
    
//    private void move(int oldIndex, int newIndex) {
//        if (oldIndex == newIndex) return;
//        
//        float[] movePosition = _movePosition[oldIndex];
//        float[] downPosition = _downPosition[oldIndex];
//        float[] prevMovePosition = _prevMovePosition[oldIndex];
//        TouchState touchState = _touchState[oldIndex];
//        long time = _upDownTime[oldIndex];
//        
//        Object[][] customStates = _customStates.toArray(new Object[0][]);
//        int customStatesCount = customStates.length;
//        Object[] customStatesSelect = new Object[customStatesCount];
//        for (int j = 0; j < customStatesCount; j++) {
//            customStatesSelect[j] = customStates[j][oldIndex];
//        }
//        
//        int di = newIndex < oldIndex ? -1 : 1;
//        int i = oldIndex + di;
//        
//        while (i - di == newIndex) {
//            int i0 = di;
//            _downPosition[i0] = _downPosition[i];
//            _movePosition[i0] = _movePosition[i];
//            _prevMovePosition[i0] = _prevMovePosition[i];
//            _touchState[i0] = _touchState[i];
//            _upDownTime[i0] = _upDownTime[i];
//            for (int j = 0; j < customStatesCount; j++) {
//                customStates[j][i0] = customStates[j][i]; 
//            }
//            i += di;
//        }
//        
//        _downPosition[newIndex] = downPosition;
//        _movePosition[newIndex] = movePosition;
//        _prevMovePosition[newIndex] = prevMovePosition;
//        _touchState[newIndex] = touchState;
//        _upDownTime[newIndex] = time;
//        for (int j = 0; j < customStatesCount; j++) {
//            customStates[j][newIndex] = customStatesSelect[j]; 
//        }
//    }
    
    private void onTouch(int[] indexes) {
        _touchEventHandler.invoke(_view, new TouchEventArgs(this, indexes));
    }
    
    private void onTouch(int index) {
        _touchEventHandler.invoke(_view, new TouchEventArgs(this, index));
    }

    private IEventHandler<TouchEventArgs> _touchEventHandler = new EventHandler<TouchEventArgs>();

    public IEventHandler<TouchEventArgs> TouchEvent() {
        return _touchEventHandler;
    }
}
