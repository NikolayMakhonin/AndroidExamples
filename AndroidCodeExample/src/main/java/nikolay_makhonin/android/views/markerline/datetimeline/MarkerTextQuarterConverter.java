package nikolay_makhonin.android.views.markerline.datetimeline;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import nikolay_makhonin.android.views.markerline.contracts.IDrawState;
import nikolay_makhonin.utils.datetime.DateTime;

public class MarkerTextQuarterConverter extends MarkerTextDateTimeConverter {
    private static String[] quarterNames = new String[] { "I", "II", "III", "IV" };
    
    /** "'%s' yyyy" => "IV 2004" */
    public MarkerTextQuarterConverter(IDrawState drawState, String dateTimeWithStringFormat) {
        super(drawState, dateTimeWithStringFormat);
    }
    
    @Override
    protected String dateToString(Date date) {
        Calendar calendar = new GregorianCalendar(DateTime.UTC);
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        
        return String.format(super.dateToString(date), quarterNames[month / 3]);
    }
}
