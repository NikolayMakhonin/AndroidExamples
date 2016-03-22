package nikolay_makhonin.utils.events;

import java.util.HashSet;
import java.util.Set;

import nikolay_makhonin.utils.logger.Log;

public class EventHandler<TEventArgs extends EventArgs> extends EventHandlerBase<TEventArgs> {
    private Set<IEventListener<TEventArgs>> _listeners;
    private final Object _locker = new Object();

    private Set<IEventListener<TEventArgs>> getListeners() {
        if (_listeners == null) {
            synchronized (_locker) {
                if (_listeners == null) {
                    if (_disposed) {
                        Log.w("EventHandler", "remove, object disposed");
                    } else {
                        _listeners = new HashSet<IEventListener<TEventArgs>>();
                    }
                }
            }
        }
        return _listeners;
    }

    @Override
    public int count() {
        return _listeners == null ? 0 : getListeners().size();
    }

    @Override
    public void add(final IEventListener<TEventArgs> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener == null");
        }
        if (_disposed) {
            throw new IllegalStateException("Object disposed");
        }
        synchronized (_locker) {
            Set<IEventListener<TEventArgs>> listeners = getListeners();
            if (listeners.contains(listener)) {
                Log.w("EventHandler", "listeners.contains(listener)");
                return;
            }
            listeners.add(listener);
        }
    }

    @Override
    public void remove(final IEventListener<TEventArgs> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener == null");
        }
        if (_disposed) {
            Log.w("EventHandler", "remove, object disposed");
            return;
        }
        if (_listeners == null) {
            return;
        }
        synchronized (_locker) {
            getListeners().remove(listener);
        }
    }

    @Override
    public void invoke(final Object o, final TEventArgs e) {
        if (_disposed) {
            throw new IllegalStateException("Object disposed");
        }
        if (_listeners == null) {
            return;
        }
        Object[] listeners;
        synchronized (_locker) {
            Set<IEventListener<TEventArgs>> listenersBase = getListeners();
            if (listenersBase.size() == 0) {
                return;
            }
            listeners = listenersBase.toArray();
        }
        for (final Object listener : listeners) {
            if (!((IEventListener<TEventArgs>) listener).onEvent(o, e)) {
                return;
            }
        }
    }

    @Override
    public void Dispose() {
        if (_disposed) {
            Log.w("EventHandlerBase", "Object already disposed");
        } else {
            _disposed = true;
            _listeners = null;
        }
        super.Dispose();
    }
}
