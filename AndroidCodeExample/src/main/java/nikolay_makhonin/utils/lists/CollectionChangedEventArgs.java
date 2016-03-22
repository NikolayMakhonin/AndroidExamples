package nikolay_makhonin.utils.lists;

import nikolay_makhonin.utils.events.EventArgs;

public class CollectionChangedEventArgs<T> extends EventArgs {
    int _OldIndex;
    
    /** индекс первого элемента OldItems */
    public int getOldIndex() {
        return _OldIndex;
    }
    
    int _NewIndex;
    
    /** индекс первого элемента NewItems */
    public int getNewIndex() {
        return _NewIndex;
    }
    
    Object[] _OldItems;
    
    public Object[] getOldItems() {
        return _OldItems;
    }
    
    Object[] _NewItems;
    
    public Object[] getNewItems() {
        return _NewItems;
    }
    
    CollectionChangedType _ChangedType;
    
    public CollectionChangedType getChangedType() {
        return _ChangedType;
    }
    
    public CollectionChangedEventArgs(final CollectionChangedType changedType, final int oldIndex, final int newIndex, final Object[] oldItems,
        final Object[] newItems) {
        _NewIndex = newIndex;
        _OldIndex = oldIndex;
        _OldItems = oldItems;
        _NewItems = newItems;
        _ChangedType = changedType;
    }
}
