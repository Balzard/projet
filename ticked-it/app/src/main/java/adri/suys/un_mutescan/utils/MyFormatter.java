package adri.suys.un_mutescan.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * A formatter used to changes all the float to integer
 * This formatter is used for the pieChart (cf EventStatFragment)
 */
public class MyFormatter implements IValueFormatter {

    public MyFormatter() {
        DecimalFormat mFormat = new DecimalFormat("###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return "" + ((int) value);
    }
}
