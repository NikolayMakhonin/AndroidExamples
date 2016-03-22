package nikolay_makhonin.utils.contracts.delegates;

public interface FuncN<TArgs, TResult> {
    TResult invoke(TArgs... args);
}

