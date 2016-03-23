package nikolay_makhonin.android.views.adapters.touchstate;

import nikolay_makhonin.utils.CompareUtils;

public class PointStateBase implements Comparable<PointStateBase> {
    Integer _id;
    public PointStateBase(int id) {
        this._id = id;
    }
    @Override
    public int compareTo(PointStateBase another) {
        return CompareUtils.Compare(_id, another._id);
    }   
    
    public int getId() {
        return _id;
    }
}