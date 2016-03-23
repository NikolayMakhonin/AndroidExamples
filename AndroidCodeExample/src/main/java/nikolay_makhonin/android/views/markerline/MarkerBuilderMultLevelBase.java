package nikolay_makhonin.android.views.markerline;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IMarker;

public abstract class MarkerBuilderMultLevelBase<T extends IMarker> extends MarkerBuilderBase {
    private final double _multCoefs;
    private final double[] _coefs;
    protected final double _minDelta;
    protected final double _maxDelta;
    private final double _delta0;
    //private static final double EPSILON_SUMM = 1e-10;
    private static final double EPSILON_MULT = 1.0 + 1e-5;
    private T _marker0;
    private final int _countLevelsLeft;
    private final int _countLevelsRight;
    private final double _1_log_multCoefs;
    
    private static int[] calcLeftRightCountLevels(double delta0, double[] coefs, double minDelta, double maxDelta) {
        if (minDelta <= 0) minDelta = Double.MIN_VALUE;
        if (maxDelta <= 0) maxDelta = Double.MAX_VALUE;
        
        int leftCount = 0;
        int rightCount = 0;
        int coefsCount;
        if (coefs == null || (coefsCount = coefs.length) == 0) return new int[] {leftCount, rightCount};

        //calc left count
        double delta = delta0;
        if (delta * EPSILON_MULT >= minDelta) {
            while(true) {
                boolean bBreak = false;
                for (int i = coefsCount - 1; i >= 0; i--) {
                    double coef = coefs[i];
                    delta /= coef;
                    if (delta * EPSILON_MULT <= minDelta) {
                        bBreak = true;
                        break;
                    }
                    leftCount++;
                }
                if (bBreak) break;
            }
        }
        
        //calc right count
        delta = delta0;
        if (delta <= maxDelta * EPSILON_MULT) {
            while(true) {
                boolean bBreak = false;
                for (int i = 0; i < coefsCount; i++) {
                    double coef = coefs[i];
                    delta *= coef;
                    if (delta >= maxDelta * EPSILON_MULT) {
                        bBreak = true;
                        break;
                    }
                    rightCount++;
                }
                if (bBreak) break;
            }
        }
        
        return new int[] {leftCount, rightCount};
    }
    
    /** multiple all elements of coefs must be > 1.0 */
    public MarkerBuilderMultLevelBase(IDrawState drawState, int drawOrder, double delta0, double[] coefs, double minDelta, double maxDelta) {
        this(drawState, drawOrder, delta0, coefs, minDelta, maxDelta, calcLeftRightCountLevels(delta0, coefs, minDelta, maxDelta));
    }

    private MarkerBuilderMultLevelBase(IDrawState drawState, int drawOrder, double delta0, double[] coefs, double minDelta, double maxDelta, int[] countLevelsLeftRight) {
        super(drawState, drawOrder, countLevelsLeftRight[0] + countLevelsLeftRight[1] + 1);
        _countLevelsLeft = countLevelsLeftRight[0];
        _countLevelsRight = countLevelsLeftRight[1];
        _coefs = coefs;
        _multCoefs = getMultCoefs(_coefs);
        if (_coefs != null && _coefs.length > 0 && _multCoefs <= 1.0) {
            throw new IllegalArgumentException("mult(coefs) <= 1.0");
        }
        _minDelta = minDelta > 0 ? minDelta : 0;
        _maxDelta = maxDelta > 0 ? maxDelta : Double.MAX_VALUE;
        _delta0 = delta0;
        if (_delta0 <= 0) {
            throw new IllegalArgumentException("_delta0 <= 0");
        }
        if (_minDelta > _delta0 || _maxDelta < _delta0) {
            throw new IllegalArgumentException("_minDelta > _delta0 || _maxDelta < _delta0");
        }
        
        //init constants
        _1_log_multCoefs = 1.0 / Math.log(_multCoefs);
    }
    
    protected T getMarker0() {
        if (_marker0 != null) return _marker0;
        return _marker0 = createMarker(_delta0);
    }
    
    private static double getMultCoefs(double[] coefs) {
        double multCoefs = 1;
        if (coefs == null) return multCoefs;
        for (double coef : coefs) {
            multCoefs *= coef;
        }
        return multCoefs;
    }
        
    protected abstract T createMarker(double delta);

    @Override
    protected IMarker createMarker(int level) {
        double delta = _delta0;
        if (_coefs != null && _coefs.length > 0) {
            if (level >= _countLevelsLeft) {
                int N = level - _countLevelsLeft;
                int n = N / _coefs.length;
                int m = N % _coefs.length;
                delta *= Math.pow(_multCoefs, n);
                for (int i = 0; i < m; i++) {
                    delta *= _coefs[i];
                }
            } else {
                int N = _countLevelsLeft - level;
                int n = N / _coefs.length;
                int m = N % _coefs.length;
                delta /= Math.pow(_multCoefs, n);
                for (int i = 0; i < m; i++) {
                    delta /= _coefs[_coefs.length - i - 1];
                }
            }
        }
        
        return createMarker(delta);
    }
    
    @Override
    protected int calcMinLevel() {
        double dpx = _drawState.getDeltaPixel();
        T marker0 = getMarker0();
        marker0.setLevel(0);
        double multCoef = dpx * (marker0.getWidth() + marker0.getSpaceWidth()) / _delta0;
        double n = Math.log(multCoef) * _1_log_multCoefs;
        
        boolean negative = n < 0;
        if (_coefs == null || _coefs.length == 0) {
            return negative ? 0 : 1;
        }
        
        n = Math.abs(n);
        
        int count = ((int)Math.floor(n)) * _coefs.length;
        if (negative) {
            if (count >= _countLevelsLeft) return 0;
        } else {
            if (count >= _countLevelsRight) return _countLevelsLeft + _countLevelsRight + 1;
        }
        
        double mod = Math.pow(_multCoefs, n - Math.floor(n));
        if (negative) {
            double d = 1;
            for (int i = _coefs.length - 1; i >= 0; i--) {
                double coef = _coefs[i];
                d *= coef;
                if (d > mod) break;
                count++;
            }
            count = _countLevelsLeft - count;
            if (count < 0) count = 0;
        } else {
            double d = 1;
            for (int i = 0; i < _coefs.length; i++) {
                double coef = _coefs[i];
                d *= coef;
                count++;
                if (d > mod) break;
            }
            if (count > _countLevelsRight + 1) count = _countLevelsRight + 1;
            count = _countLevelsLeft + count;
        }
       
        return count;
    }
    
}
