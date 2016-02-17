package com.nex3z.examples.widget.widget;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SimpleWidgetProvider extends AppWidgetProvider {
    private static final String LOG_TAG = SimpleWidgetProvider.class.getSimpleName();
    private static int count = 0;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(LOG_TAG, "onUpdate(): count = " + (++count));
        context.startService(new Intent(context, SimpleWidgetIntentService.class));
    }
}
