package com.nex3z.examples.widget.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.nex3z.examples.widget.R;
import com.nex3z.examples.widget.ui.activity.MainActivity;

public class StaticWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        final int N = appWidgetIds.length;

        for (int appWidgetId : appWidgetIds) {
//        for (int i = 0; i < N; i++) {
//            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.static_widget);
            views.setOnClickPendingIntent(R.id.widget_movie_poster, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
