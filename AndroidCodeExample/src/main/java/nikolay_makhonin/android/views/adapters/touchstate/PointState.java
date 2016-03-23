package nikolay_makhonin.android.views.adapters.touchstate;

public class PointState extends PointStateBase {
    float[] _movePosition;
    float[] _prevMovePosition;
    float[] _downPosition;
    TouchState _touchState = TouchState.None;
    long _upDownTime = Long.MIN_VALUE;
    
    public PointState(int id) {
        super(id);
    }
    
    public float getDownX() { return _downPosition[0]; }
    public float getDownY() { return _downPosition[1]; }
    public float getPrevMoveX() { return _prevMovePosition[0]; }
    public float getPrevMoveY() { return _prevMovePosition[1]; }
    public float getMoveX() { return _movePosition[0]; }
    public float getMoveY() { return _movePosition[1]; }
    public TouchState getTouchState() { return _touchState; }
}