package nikolay_makhonin.utils.contracts.delegates;

public interface Func2<T1, T2, TResult> {
    TResult invoke(T1 v1, T2 v2);
}
