package nikolay_makhonin.utils.contracts.delegates;

public interface Action3<T1, T2, T3> {
    void invoke(T1 v1, T2 v2, T3 v3);
}