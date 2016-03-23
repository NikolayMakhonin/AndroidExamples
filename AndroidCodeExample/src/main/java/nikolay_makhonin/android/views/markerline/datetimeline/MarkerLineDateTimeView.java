package nikolay_makhonin.android.views.markerline.datetimeline;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Calendar;
import java.util.List;

import nikolay_makhonin.android.views.markerline.MarkerLayoutAlign;
import nikolay_makhonin.android.views.markerline.MarkerLineViewBase;
import nikolay_makhonin.android.views.markerline.MarkersLayer;
import nikolay_makhonin.android.views.markerline.RangeLocker;
import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.contracts.IDrawStyleState;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerLayer;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerLayout;
import nikolay_makhonin.android.views.markerline.contracts.IMarkerRender;
import nikolay_makhonin.android.views.markerline.selectedrange.MarkerSelectedRangeBuilder;
import nikolay_makhonin.android.views.markerline.timespanline.MarkerNumberTextModBuilder;
import nikolay_makhonin.utils.DoubleStringConverter;
import nikolay_makhonin.utils.MathUtils;

public class MarkerLineDateTimeView extends MarkerLineViewBase {

    public MarkerLineDateTimeView(Context context) {
        super(context);
    }

    public MarkerLineDateTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarkerLineDateTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        super.initView(context, attrs, defStyleAttr);
        _drawState.drawSize().drawSizeChanged().add(_drawStateChanged_listener);
    }

    @Override
    protected IDrawStyleState createDrawStyleState(Context context, AttributeSet attrs, int defStyleAttr) {
        return new DateTimeDrawState(context, attrs, defStyleAttr, isInEditMode());
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
    
            MarkerDateTimeTextBuilder markerTopDaysTextBuilder1 = new MarkerDateTimeTextBuilder(drawState, "dd MMM yyyy", Calendar.DAY_OF_MONTH, null, 1, 1, true);
            MarkerDateTimeTextBuilder markerTopDaysTextBuilder2 = new MarkerDateTimeTextBuilder(drawState, "dd MMMM yyyy", Calendar.DAY_OF_MONTH, null, 1, 1, true);
            MarkerDateTimeTextBuilder markerTopMonthsTextBuilder1 = new MarkerDateTimeTextBuilder(drawState, "MMM yyyy", Calendar.MONTH, null, 1, 1, true);
            MarkerDateTimeTextBuilder markerTopMonthsTextBuilder2 = new MarkerDateTimeTextBuilder(drawState, "MMMM yyyy", Calendar.MONTH, null, 1, 1, true);
            MarkerDateTimeTextBuilder markerTopQuartersTextBuilder = new MarkerDateTimeTextBuilder(drawState, new MarkerTextQuarterConverter(drawState, "'%s'  yyyy"), Calendar.MONTH, null, 3, 3, true);
            MarkerDateTimeTextBuilder markerTopYearsTextBuilder = new MarkerDateTimeTextBuilder(drawState, "yyyy", Calendar.YEAR, null, 1, 1, true);
    
            markerTopTextLayer1.markerBuilderCollection().markerBuilders().add(markerTopDaysTextBuilder1);
            markerTopTextLayer1.markerBuilderCollection().markerBuilders().add(markerTopDaysTextBuilder2);
            markerTopTextLayer1.markerBuilderCollection().markerBuilders().add(markerTopMonthsTextBuilder1);
            markerTopTextLayer1.markerBuilderCollection().markerBuilders().add(markerTopMonthsTextBuilder2);
            markerTopTextLayer1.markerBuilderCollection().markerBuilders().add(markerTopQuartersTextBuilder);
            markerTopTextLayer1.markerBuilderCollection().markerBuilders().add(markerTopYearsTextBuilder);
    
            markerLayout.setAlign(MarkerLayoutAlign.Top);
            int maxHeight = MathUtils.max(
                markerTopDaysTextBuilder1.getMaxDrawHeight(),
                markerTopDaysTextBuilder2.getMaxDrawHeight(),
                markerTopMonthsTextBuilder1.getMaxDrawHeight(),
                markerTopMonthsTextBuilder2.getMaxDrawHeight(),
                markerTopQuartersTextBuilder.getMaxDrawHeight(),
                markerTopYearsTextBuilder.getMaxDrawHeight()
            );
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
            MarkerDateTimeTextBuilder markerTopSecondsTextBuilder = new MarkerDateTimeTextBuilder(drawState, "H:mm:ss", Calendar.SECOND, null, 1, 1, true);
            MarkerDateTimeTextBuilder markerTopMinutesTextBuilder = new MarkerDateTimeTextBuilder(drawState, "H:mm", Calendar.MINUTE, null, 1, 1, true);
            MarkerDateTimeTextBuilder markerTopHoursTextBuilder = new MarkerDateTimeTextBuilder(drawState, "H'h'", Calendar.HOUR, null, 1, 1, true);
            
            markerTopTextLayer2.markerBuilderCollection().markerBuilders().add(markerTopSecondsTextBuilder);
            markerTopTextLayer2.markerBuilderCollection().markerBuilders().add(markerTopMinutesTextBuilder);
            markerTopTextLayer2.markerBuilderCollection().markerBuilders().add(markerTopHoursTextBuilder);
    
            markerLayout.setAlign(MarkerLayoutAlign.Top);
            int maxHeight = MathUtils
                .max(markerTopSecondsTextBuilder.getMaxDrawHeight(), markerTopMinutesTextBuilder.getMaxDrawHeight(),
                    markerTopHoursTextBuilder.getMaxDrawHeight());
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
            drawState.drawSize().setMaxCountLevels(4);
    
            IMarkerLayer markerTextLayer = new MarkersLayer(drawState, new RangeLocker());
            DoubleStringConverter doubleStringConverter = new DoubleStringConverter();
            MarkerNumberTextModBuilder markerMillisecondsTextBuilder = new MarkerNumberTextModBuilder(drawState, doubleStringConverter, new double[] { 5, 2 }, -1, 1, "");
            MarkerDateTimeTextBuilder markerSecondsTextBuilder = new MarkerDateTimeTextBuilder(drawState, "s's'", Calendar.SECOND, new double[] { 5, 2, 3, 2 }, 1, 30, false);
            MarkerDateTimeTextBuilder markerMinutesTextBuilder = new MarkerDateTimeTextBuilder(drawState, "m'm'", Calendar.MINUTE, new double[] { 5, 2, 3, 2 }, 1, 30, false);
            MarkerDateTimeTextBuilder markerHoursTextBuilder = new MarkerDateTimeTextBuilder(drawState, "H'h'", Calendar.HOUR, new double[] { 3, 2, 2, 2 }, 1, 12, false);
            MarkerDateTimeTextBuilder markerDaysTextBuilder = new MarkerDateTimeTextBuilder(drawState, "dd", Calendar.DAY_OF_MONTH, new double[] { 7 }, 1, 7, false);
            MarkerDateTimeTextBuilder markerQuartersTextBuilder = new MarkerDateTimeTextBuilder(drawState, 1, new MarkerTextQuarterConverter(drawState, "'%s'"), Calendar.MONTH, null, 3, 3, false);
            MarkerDateTimeTextBuilder markerMonthsTextBuilder1 = new MarkerDateTimeTextBuilder(drawState, 2, "MMM", Calendar.MONTH, null, 1, 1, false);
            MarkerDateTimeTextBuilder markerMonthsTextBuilder2 = new MarkerDateTimeTextBuilder(drawState, 3, "MMMM", Calendar.MONTH, null, 1, 1, false);
            MarkerDateTimeTextBuilder markerMonthsTextBuilder3 = new MarkerDateTimeTextBuilder(drawState, 4, "MMM", Calendar.MONTH, null, 3, 3, false);
            MarkerDateTimeTextBuilder markerMonthsTextBuilder4 = new MarkerDateTimeTextBuilder(drawState, 5, "MMMM", Calendar.MONTH, null, 3, 3, false);
            MarkerDateTimeTextBuilder markerYearsTextBuilder = new MarkerDateTimeTextBuilder(drawState, 6, "yyyy", Calendar.YEAR, new double[] { 5.0, 2.0 }, 1, 9999, false);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerMillisecondsTextBuilder);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerSecondsTextBuilder);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerMinutesTextBuilder);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerHoursTextBuilder);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerDaysTextBuilder);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerQuartersTextBuilder);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerMonthsTextBuilder1);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerMonthsTextBuilder2);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerMonthsTextBuilder3);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerMonthsTextBuilder4);
            markerTextLayer.markerBuilderCollection().markerBuilders().add(markerYearsTextBuilder);
    
            IMarkerLayer markerLineLayer = new MarkersLayer(drawState, null);
            int middleSpaceHeight = markerMillisecondsTextBuilder.getMaxDrawHeight() * 2;
            MarkerDateTimeLineBuilder markerSecondLineBuilder = new MarkerDateTimeLineBuilder(drawState, Calendar.SECOND, new double[] { 5, 2, 3, 2 }, 1, 30, middleSpaceHeight);
            MarkerDateTimeLineBuilder markerMinuteLineBuilder = new MarkerDateTimeLineBuilder(drawState, Calendar.MINUTE, new double[] { 5, 2, 3, 2 }, 1, 30, middleSpaceHeight);
            MarkerDateTimeLineBuilder markerHourLineBuilder = new MarkerDateTimeLineBuilder(drawState, Calendar.HOUR, new double[] { 3, 2, 2, 2 }, 1, 12, middleSpaceHeight);
            MarkerDateTimeLineBuilder markerDayLineBuilder = new MarkerDateTimeLineBuilder(drawState, Calendar.DAY_OF_MONTH, new double[] { 7 }, 1, 7, middleSpaceHeight);
            MarkerDateTimeLineBuilder markerMonthLineBuilder = new MarkerDateTimeLineBuilder(drawState, Calendar.MONTH, new double[] { 3 }, 1, 3, middleSpaceHeight);
            MarkerDateTimeLineBuilder markerYearLineBuilder = new MarkerDateTimeLineBuilder(drawState, Calendar.YEAR, new double[] { 5.0, 2.0 }, 1, 9999, middleSpaceHeight);
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
