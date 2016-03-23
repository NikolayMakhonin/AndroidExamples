package nikolay_makhonin.android.views.markerline.contracts;

public interface IRangeLocker {
    boolean lock(float start, float end);
    void clear();
}
