package nikolay_makhonin.utils.lists;

public enum CollectionChangedType {
    /** установлены свойства OldIndex, OldItems */
    Removed,
    /** установлены свойства NewIndex, NewItems */
    Added,
    /** установлены свойства OldIndex == NewIndex, OldItems[1], NewItems[1], */
    Setted,
    /** не установлены свойства */
    Resorted,
    /** установлены свойства OldIndex, NewIndex, NewItems[1] */
    Moved,
    /** установлены свойства OldIndex, NewIndex */
    Shift
}
