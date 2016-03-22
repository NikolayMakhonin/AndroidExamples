package nikolay_makhonin.utils.events;

public class IndexEventArgs<TValue> extends EventArgs {
    private final TValue _index;

    public final TValue getIndex() {
        return _index;
    }

    public IndexEventArgs(TValue index) {
        _index = index;
    }
}
