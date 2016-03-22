package nikolay_makhonin.utils.events;

public interface IEventListener<TEventArgs extends EventArgs> {
    boolean onEvent(Object o, TEventArgs e);
}
