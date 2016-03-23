package nikolay_makhonin.android.views.markerline.timespanline;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

import nikolay_makhonin.android.views.markerline.MarkerLayoutAlign;
import nikolay_makhonin.android.views.markerline.MarkerLineViewBase;
import nikolay_makhonin.android.views.markerline.MarkersLayer;
import nikolay_makhonin.android.views.markerline.RangeLocker;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerLayer;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerLayout;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerRender;
import nikolay_makhonin.android.views.markerline.selectedrange.MarkerSelectedRangeBuilder;
import nikolay_makhonin.utils.DoubleStringConverter;
import nikolay_makhonin.utils.MathUtils;
import nikolay_makhonin.utils.datetime.TimeSpanPart;

public class MarkerLineTimeSpanView extends MarkerLineViewBase {

    public MarkerLineTimeSpanView(Context context) {
        super(context);
    }

    public MarkerLineTimeSpanView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarkerLineTimeSpanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void fillMarkerLayouts(List<IMarkerLayout> markerLayouts) {
        {
            IMarkerLayout markerLayout = createMarkerLayout();
            markerLayouts.add(markerLayout);
            IMarkerRender markerRender = markerLayout.Render();
            IDrawState drawState = markerLayout.DrawState();
            drawState.drawSize().setMaxCountLevels(1);
    
            IMarkerLayer markerTopTextLayer1 = new MarkersLayer(drawState, null);
    
            MarkerTimeSpanTextBuilder markerTopDaysTextBuilder = new MarkerTimeSpanTextBuilder(drawState, new String[] { "%dy  ", "%dM  ", "%dd" }, new TimeSpanPart[] { TimeSpanPart.Year, TimeSpanPart.Month, TimeSpanPart.DayOfMonth }, TimeSpanPart.DayOfMonth, null, 1, 1, true);
            MarkerTimeSpanTextBuilder markerTopMonthsTextBuilder = new MarkerTimeSpanTextBuilder(drawState, new String[] { "%dy  ", "%dM" }, new TimeSpanPart[] { TimeSpanPart.Year, TimeSpanPart.Month }, TimeSpanPart.Month, null, 1, 1, true);
            MarkerTimeSpanTextBuilder markerTopYearsTextBuilder = new MarkerTimeSpanTextBuilder(drawState, new String[] { "%dy" }, new TimeSpanPart[] { TimeSpanPart.Year }, TimeSpanPart.Year, null, 1, 1, true);
    
            markerTopTextLayer1.markerBuilderCollection().markerBuilders().add(markerTopDaysTextBuilder);
            markerTopTextLayer1.markerBuilderCollection().markerBuilders().add(markerTopMonthsTextBuilder);
            markerTopTextLayer1.markerBuilderCollection().markerBuilders().add(markerTopYearsTextBuilder);
    
            markerLayout.setAlign(MarkerLayoutAlign.Top);
            int maxHeight = MathUtils
                .max(markerTopDaysTextBuilder.getMaxDrawHeight(), markerTopMonthsTextBuilder.getMaxDrawHeight(),
                    markerTopYearsTextBuilder.getMaxDrawHeight());
            maxHeight += calcMargin(maxHeight);
            drawState.drawSize().setDrawSize(drawState.drawSize().getDrawSize()[0], maxHeight);
            markerRender.markerLayers().add(markerTopTextLayer1);
        }
        
        //========================================================
        
        {
            IMarkerLayout markerLayout = createMarkerLayout();
            markerLayouts.add(markerLayout);
            IMarkerRender markerRender = markerLayout.Render();
            IDrawState drawState = markerLayout.DrawState();
            drawState.drawSize().setMaxCountLevels(1);
    
            IMarkerLayer markerTopTextLayer2 = new MarkersLayer(drawState, null);
            MarkerTimeSpanTextBuilder markerTopSecondsTextBuilder = new MarkerTimeSpanTextBuilder(drawState, new String[] { "%dh  ", "%dm  ", "%ds" }, new TimeSpanPart[] { TimeSpanPart.Hour, TimeSpanPart.Minute, TimeSpanPart.Second }, TimeSpanPart.Second, null, 1, 1, true);
            MarkerTimeSpanTextBuilder markerTopMinutesTextBuilder = new MarkerTimeSpanTextBuilder(drawState, new String[] { "%dh  ", "%dm" }, new TimeSpanPart[] { TimeSpanPart.Hour, TimeSpanPart.Minute }, TimeSpanPart.Minute, null, 1, 1, true);
            MarkerTimeSpanTextBuilder markerTopHoursTextBuilder = new MarkerTimeSpanTextBuilder(drawState, new String[] { "%dh" }, new TimeSpanPart[] { TimeSpanPart.Hour }, TimeSpanPart.Hour, null, 1, 1, true);
            
            markerTopTextLayer2.markerBuilderCollection().markerBuilders().add(markerTopSecondsTextBuilder);
            markerTopTextLayer2.markerBuilderCollection().markerBuilders().add(markerTopMinutesTextBuilder);
            markerTopTextLayer2.markerBuilderCollection().markerBuilders().add(markerTopHoursTextBuilder);
    
            markerLayout.setAlign(MarkerLayoutAlign.Top);
            int maxHeight = MathUtils.max(
                markerTopSecondsTextBuilder.getMaxDrawHeight(),
                markerTopMinutesTextBuilder.getMaxDrawHeight(),
                markerTopHoursTextBuilder.getMaxDrawHeight()
            );
            maxHeight += calcMargin(maxHeight);
            drawState.drawSize().setDrawSize(drawState.drawSize().getDrawSize()[0], maxHeight);
            markerRender.markerLayers().add(markerTopTextLayer2);
        }
        
        //========================================================
        
        {
            IMarkerLayout markerLayout = createMarkerLayout();
            markerLayouts.add(markerLayout);
            IMarkerRender markerRender = markerLayout.Render();
            IDrawState drawState = markerLayout.DrawState();
    
            IMarkerLayer markerTextLayer = new MarkersLayer(drawState, new RangeLocker());
            MarkerNumberTextModBuilder markerMillisecondsTextBuilder = new MarkerNumberTextModBuilder(drawState, new DoubleStringConverter(), new double[] { 5, 2 }, -1, 1, "");
            MarkerTimeSpanTextBuilder markerSecondsTextBuilder = new MarkerTimeSpanTextBuilder(drawState, new String[] { "%ss" }, new TimeSpanPart[] { TimeSpanPart.Second }, TimeSpanPart.Second, new double[] { 5, 2, 3, 2 }, 1, 30, false);
            MarkerTimeSpanTextBuilder markerMinutesTextBuilder = new MarkerTimeSpanTextBuilder(drawState, new String[] { "%sm" }, new TimeSpanPart[] { TimeSpanPart.Minute }, TimeSpanPart.Minute, new double[] { 5, 2, 3, 2 }, 1, 30, false);
            MarkerTimeSpanTextBuilder markerHoursTextBuilder = new MarkerTimeSpanTextBuilder(drawState, new String[] { "%sh" }, new TimeSpanPart[] { TimeSpanPart.Hour }, TimeSpanPart.Hour, new double[] { 3, 2, 2, 2 }, 1, 12, false);
            MarkerTimeSpanTextBuilder markerDaysTextBuilder = new MarkerTimeSpanTextBuilder(drawState, new String[] { "%sd" }, new TimeSpanPart[] { TimeSpanPart.DayOfMonth }, TimeSpanPart.DayOfMonth, new double[] { 7, 2, 2 }, 1, 7, false);
            MarkerTimeSpanTextBuilder markerMonthsTextBuilder = new MarkerTimeSpanTextBuilder(drawState, new String[] { "%sM" }, new TimeSpanPart[] { TimeSpanPart.Month }, TimeSpanPart.Month, new double[] { 3, 4 }, 1, 3, false);
            MarkerTimeSpanTextBuilder markerYearsTextBuilder = new MarkerTimeSpanTextBuilder(drawState, new String[] { "%sy" }, new TimeSpanPart[] { TimeSpanPart.Year }, TimeSpanPart.Year, new double[] { 5, 2 }, 1, 5000, false);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerMillisecondsTextBuilder);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerSecondsTextBuilder);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerMinutesTextBuilder);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerHoursTextBuilder);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerDaysTextBuilder);
            //markerTextLayer.markerBuilderCollection().markerBuilders().add(markerWeeksTextBuilder);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerMonthsTextBuilder);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerYearsTextBuilder);
    
            IMarkerLayer markerLineLayer = new MarkersLayer(drawState, null);
            int middleSpaceHeight = markerMillisecondsTextBuilder.getMaxDrawHeight() * 2;
            MarkerTimeSpanLineBuilder markerSecondLineBuilder = new MarkerTimeSpanLineBuilder(drawState, TimeSpanPart.Second, new double[] { 5, 2, 3, 2 }, 1, 30, middleSpaceHeight);
            MarkerTimeSpanLineBuilder markerMinuteLineBuilder = new MarkerTimeSpanLineBuilder(drawState, TimeSpanPart.Minute, new double[] { 5, 2, 3, 2 }, 1, 30, middleSpaceHeight);
            MarkerTimeSpanLineBuilder markerHourLineBuilder = new MarkerTimeSpanLineBuilder(drawState, TimeSpanPart.Hour, new double[] { 3, 2, 2, 2 }, 1, 12, middleSpaceHeight);
            MarkerTimeSpanLineBuilder markerDayLineBuilder = new MarkerTimeSpanLineBuilder(drawState, TimeSpanPart.DayOfMonth, new double[] { 7 }, 1, 7, middleSpaceHeight);
            MarkerTimeSpanLineBuilder markerMonthLineBuilder = new MarkerTimeSpanLineBuilder(drawState, TimeSpanPart.Month, new double[] { 3 }, 1, 3, middleSpaceHeight);
            MarkerTimeSpanLineBuilder markerYearLineBuilder = new MarkerTimeSpanLineBuilder(drawState, TimeSpanPart.Year, new double[] { 5.0, 2.0 }, 1, 5000, middleSpaceHeight);
            markerLineLayer.markerBuilderCollection().markerBuilders().add(markerSecondLineBuilder);
            markerLineLayer.markerBuilderCollection().markerBuilders().add(markerMinuteLineBuilder);
            markerLineLayer.markerBuilderCollection().markerBuilders().add(markerHourLineBuilder);
            markerLineLayer.markerBuilderCollection().markerBuilders().add(markerDayLineBuilder);
            markerLineLayer.markerBuilderCollection().markerBuilders().add(markerMonthLineBuilder);
            markerLineLayer.markerBuilderCollection().markerBuilders().add(markerYearLineBuilder);
    
            IMarkerLayer markerSelectedRangeLayer = new MarkersLayer(drawState, null);
            MarkerSelectedRangeBuilder markerSelectedRangeBuilder = new MarkerSelectedRangeBuilder(drawState, middleSpaceHeight);
            markerSelectedRangeLayer.markerBuilderCollection().markerBuilders().add(markerSelectedRangeBuilder);
            
            _markerLayerForRoundValue = markerLineLayer;
            _maxCountMarkerLevelsForRoundValue = drawState.drawSize().getMaxCountLevels();
            
            markerRender.markerLayers().add(markerSelectedRangeLayer);
            markerRender.markerLayers().add(markerLineLayer);
            markerRender.markerLayers().add(markerTextLayer);
        }
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
