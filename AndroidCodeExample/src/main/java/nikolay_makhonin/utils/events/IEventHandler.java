package nikolay_makhonin.utils.events;

import nikolay_makhonin.utils.contracts.IDisposable;

public interface IEventHandler<TEventArgs extends EventArgs> extends IDisposable {
    int count();

    void add(IEventListener<TEventArgs> listener);

    void remove(IEventListener<TEventArgs> listener);

    void invoke(Object o, TEventArgs e);
}
