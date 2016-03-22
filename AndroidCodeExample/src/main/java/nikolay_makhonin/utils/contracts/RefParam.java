package nikolay_makhonin.utils.contracts;

public class RefParam<TParam> {
    public RefParam() {}
    
    public RefParam(final TParam value) {
        this.value = value;
    }
    
    public TParam value;
}
