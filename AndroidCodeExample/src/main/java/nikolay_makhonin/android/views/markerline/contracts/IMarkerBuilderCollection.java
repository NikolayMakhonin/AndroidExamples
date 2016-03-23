package nikolay_makhonin.android.views.markerline.contracts;

import nikolay_makhonin.utils.events.IEventHandler;
import nikolay_makhonin.utils.lists.IList;

public interface IMarkerBuilderCollection extends IMarkerBuilder {
    /** sorted by maxDeltaPixels */
    IList<IMarkerBuilder> markerBuilders();
    IEventHandler collectionChanged();
}
