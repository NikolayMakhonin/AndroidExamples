package nikolay_makhonin.utils.events;


public abstract class EventHandlerBase<TEventArgs extends EventArgs> implements IEventHandler<TEventArgs> {
    protected boolean _disposed;

    @Override
    public abstract void add(IEventListener<TEventArgs> listener);

    @Override
    public abstract void remove(IEventListener<TEventArgs> listener);

    @Override
    public abstract int count();

    @Override
    public void invoke(final Object o, final TEventArgs e) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void Dispose() {
        _disposed = true;
    }
}
