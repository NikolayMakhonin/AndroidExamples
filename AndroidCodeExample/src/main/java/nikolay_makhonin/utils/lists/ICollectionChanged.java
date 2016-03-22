package nikolay_makhonin.utils.lists;

import nikolay_makhonin.utils.events.IEventHandler;

public interface ICollectionChanged<T> {
    IEventHandler<CollectionChangedEventArgs<T>> CollectionChanged();

    void OnItemModified(int index);

    void OnItemModified(int index, T oldItem);
}
