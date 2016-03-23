package nikolay_makhonin.android.views.markerline.numberline;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

import nikolay_makhonin.android.views.markerline.MarkerLineViewBase;
import nikolay_makhonin.android.views.markerline.MarkersLayer;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerLayer;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerLayout;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerRender;
import nikolay_makhonin.android.views.markerline.selectedrange.MarkerSelectedRangeBuilder;
import nikolay_makhonin.utils.DoubleStringConverter;

public class MarkerLineNumberView extends MarkerLineViewBase {

    public MarkerLineNumberView(Context context) {
        super(context);
    }

    public MarkerLineNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarkerLineNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void fillMarkerLayouts(List<IMarkerLayout> markerLayouts) {
        IMarkerLayout markerLayout = createMarkerLayout();
        markerLayouts.add(markerLayout);
        IMarkerRender markerRender = markerLayout.Render();
        IDrawState drawState = markerLayout.DrawState();
        
        IMarkerLayer markerTextLayer = new MarkersLayer(drawState, null);
        MarkerNumberTextBuilder markerNumberTextBuilder = new MarkerNumberTextBuilder(drawState, new DoubleStringConverter());
        markerTextLayer.markerBuilderCollection().markerBuilders().add(markerNumberTextBuilder);

        IMarkerLayer markerLineLayer = new MarkersLayer(drawState, null);
        int middleSpaceHeight = markerNumberTextBuilder.getMaxDrawHeight() * 2;
        MarkerNumberLineBuilder markerNumberLineBuilder = new MarkerNumberLineBuilder(drawState, middleSpaceHeight);
        markerLineLayer.markerBuilderCollection().markerBuilders().add(markerNumberLineBuilder);
        
        IMarkerLayer markerSelectedRangeLayer = new MarkersLayer(drawState, null);
        MarkerSelectedRangeBuilder markerSelectedRangeBuilder = new MarkerSelectedRangeBuilder(drawState, middleSpaceHeight);
        markerSelectedRangeLayer.markerBuilderCollection().markerBuilders().add(markerSelectedRangeBuilder);

        _markerLayerForRoundValue = markerLineLayer;
        _maxCountMarkerLevelsForRoundValue = drawState.drawSize().getMaxCountLevels();

        markerRender.markerLayers().add(markerSelectedRangeLayer);
        markerRender.markerLayers().add(markerLineLayer);
        markerRender.markerLayers().add(markerTextLayer);
    }
    
    private IMarkerLayer _markerLayerForRoundValue;
    private int _maxCountMarkerLevelsForRoundValue;
    
    @Override
    protected IMarkerLayer getMarkerLayerForRoundValue() {
        return _markerLayerForRoundValue;
    }
    
    @Override
    protected int getMaxCountMarkerLevelsForRoundValue() {
        return _maxCountMarkerLevelsForRoundValue;
    }
}
