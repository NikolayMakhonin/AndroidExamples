package nikolay_makhonin.android.views.markerline;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.android.views.markerline.datetimeline.MarkerLineDateTimeView;
import nikolay_makhonin.android.views.markerline.timespanline.MarkerLineTimeSpanView;
import nikolay_makhonin.utils.datetime.DateTime;
import nikolay_makhonin.utils.events.EventArgs;
import nikolay_makhonin.utils.events.IEventListener;

public class TimeLineSynchronizer {
    private final IDrawState _dateTimeDrawState;
    private final IDrawState _timeSpanDrawState;
    private final Object _valueRangeLocker    = new Object();
    private final Object _selectedRangeLocker = new Object();

    public TimeLineSynchronizer(MarkerLineDateTimeView dateTimeLineView, MarkerLineTimeSpanView timeSpanLineView) {
        _dateTimeDrawState = dateTimeLineView.getDrawState();
        _timeSpanDrawState = timeSpanLineView.getDrawState();
        _dateTimeDrawState.drawData().valueRangeChanged().add(_dateTimeValueRangeChanged_listener);
        _timeSpanDrawState.drawData().valueRangeChanged().add(_timeSpanValueRangeChanged_listener);
        _dateTimeDrawState.drawData().selectedRangeChanged().add(_dateTimeSelectedRangeChanged_listener);
        _timeSpanDrawState.drawData().selectedRangeChanged().add(_timeSpanSelectedRangeChanged_listener);
        setDateTime0(new DateTime(0));
    }

    private IEventListener _dateTimeValueRangeChanged_listener = new IEventListener<EventArgs>() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            synchronized (_valueRangeLocker) {
                double[] newValueRange = _dateTimeDrawState.drawData().getValueRange();
                _timeSpanDrawState.drawData().valueRangeChanged().remove(_timeSpanValueRangeChanged_listener);
                if (newValueRange == null) {
                    _timeSpanDrawState.drawData().setValueRange(null);
                } else {
                    _timeSpanDrawState.drawData()
                        .setValueRange(dateTimeToTimeSpan(newValueRange[0]), dateTimeToTimeSpan(newValueRange[1]));
                }
                _timeSpanDrawState.drawData().valueRangeChanged().add(_timeSpanValueRangeChanged_listener);
                return true;
            }
        }
    };

    private IEventListener _timeSpanValueRangeChanged_listener = new IEventListener<EventArgs>() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            synchronized (_valueRangeLocker) {
                double[] newValueRange = _timeSpanDrawState.drawData().getValueRange();
                _dateTimeDrawState.drawData().valueRangeChanged().remove(_dateTimeValueRangeChanged_listener);
                if (newValueRange == null) {
                    _dateTimeDrawState.drawData().setValueRange(null);
                } else {
                    _dateTimeDrawState.drawData()
                        .setValueRange(timeSpanToDateTime(newValueRange[0]), timeSpanToDateTime(newValueRange[1]));
                }
                _dateTimeDrawState.drawData().valueRangeChanged().add(_dateTimeValueRangeChanged_listener);
                return true;
            }
        }
    };

    private IEventListener _dateTimeSelectedRangeChanged_listener = new IEventListener<EventArgs>() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            synchronized (_selectedRangeLocker) {
                double[] newSelectedRange = _dateTimeDrawState.drawData().getSelectedRange();
                _timeSpanDrawState.drawData().selectedRangeChanged().remove(_timeSpanSelectedRangeChanged_listener);
                if (newSelectedRange == null) {
                    _timeSpanDrawState.drawData().setSelectedRange(null);
                } else {
                    _timeSpanDrawState.drawData().setSelectedRange(dateTimeToTimeSpan(newSelectedRange[0]),
                        dateTimeToTimeSpan(newSelectedRange[1]));
                }
                _timeSpanDrawState.drawData().selectedRangeChanged().add(_timeSpanSelectedRangeChanged_listener);
                return true;
            }
        }
    };

    private IEventListener _timeSpanSelectedRangeChanged_listener = new IEventListener<EventArgs>() {
        @Override
        public boolean onEvent(Object o, EventArgs e) {
            synchronized (_selectedRangeLocker) {
                double[] newSelectedRange = _timeSpanDrawState.drawData().getSelectedRange();
                _dateTimeDrawState.drawData().selectedRangeChanged().remove(_dateTimeSelectedRangeChanged_listener);
                if (newSelectedRange == null) {
                    _dateTimeDrawState.drawData().setSelectedRange(null);
                } else {
                    _dateTimeDrawState.drawData().setSelectedRange(timeSpanToDateTime(newSelectedRange[0]),
                        timeSpanToDateTime(newSelectedRange[1]));
                }
                _dateTimeDrawState.drawData().selectedRangeChanged().add(_dateTimeSelectedRangeChanged_listener);
                return true;
            }
        }
    };

    private double dateTimeToTimeSpan(double value) {
        return value - _dateTime0.TotalSeconds();
    }

    private double timeSpanToDateTime(double value) {
        return _dateTime0.TotalSeconds() + value;
    }

    DateTime _dateTime0;

    public void setDateTime0(DateTime dateTime0) {
        _dateTime0 = dateTime0;
        _dateTimeSelectedRangeChanged_listener.onEvent(_dateTimeDrawState, EventArgs.Empty);
        _timeSpanValueRangeChanged_listener.onEvent(_timeSpanDrawState, EventArgs.Empty);
    }

    public DateTime getDateTime0(DateTime dateTime0) {
        return _dateTime0;
    }
}
