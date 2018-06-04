package com.nex3z.examples.changedpi;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

public class DpiUtil {

    private DpiUtil() {}

    public static String buildDpiInfo(Activity activity) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        float density = metrics.density;
        int dpi = (int)(density * 160);

        Point point = new Point();
        Display display = activity.getWindowManager().getDefaultDisplay();
        display.getRealSize(point);
        int widthPx = point.x;
        int heightPx = point.y;

        int widthDp = (int)(widthPx / density);
        int heightDp = (int)(heightPx / density);

        return String.format(activity.getString(R.string.info), density, dpi, heightPx, widthPx,
                heightDp, widthDp);
    }
}
