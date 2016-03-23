package nikolay_makhonin.android.views.markerline;

import java.util.Comparator;

import nikolay_makhonin.android.views.markerline.contracts.IRangeLocker;
import nikolay_makhonin.utils.CompareUtils;
import nikolay_makhonin.utils.lists.IList;
import nikolay_makhonin.utils.lists.SortedList;

public class RangeLocker implements IRangeLocker {
    private class Range {
        public final float Start;
        public final float End;
        
        public Range(float start, float end) {
            this.Start = start;
            this.End = end;
        }
    }

    Comparator<Range> _comparatorByStart = new Comparator<Range>() {
        @Override
        public int compare(Range r1, Range r2) {
            return CompareUtils.Compare(r1.Start, r2.Start);
        }
    };

    Comparator<Range> _comparatorByEnd = new Comparator<Range>() {
        @Override
        public int compare(Range r1, Range r2) {
            return CompareUtils.Compare(r1.End, r2.End);
        }
    };


    private IList<Range> _rangesByStart;
    private IList<Range> _rangesByEnd;
    private Object _locker = new Object();

    public RangeLocker() {
        _rangesByStart = new SortedList<Range>(true, false, _comparatorByStart);
        _rangesByEnd = new SortedList<RangeLocker.Range>(true, false, _comparatorByEnd);
    }

    @Override
    public boolean lock(float start, float end) {
        synchronized (_locker) {
            //Search space
            Range invertRange = new Range(end, start);
            int startIndex = _rangesByStart.IndexOf(invertRange);
            int endIndex = _rangesByEnd.IndexOf(invertRange);
            if (startIndex < 0) {
                startIndex = ~startIndex;
            } else {
                startIndex++;
            }
            if (endIndex < 0) {
                startIndex = ~startIndex;
            }
            if (startIndex != endIndex) return false;
            
            Range range = new Range(start, end);
            _rangesByStart.Insert(startIndex, range);
            _rangesByEnd.Insert(endIndex, range);
            return true;
        }
    }
    
    @Override
    public void clear() {
        synchronized(_locker) {
            _rangesByStart.clear();
            _rangesByEnd.clear();
        }
    }   
}
