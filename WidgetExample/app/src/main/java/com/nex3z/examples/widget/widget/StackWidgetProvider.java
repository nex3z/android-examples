package com.nex3z.examples.widget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.nex3z.examples.widget.R;
import com.nex3z.examples.widget.ui.activity.MainActivity;
import com.nex3z.examples.widget.ui.activity.MovieActivity;


public class StackWidgetProvider extends AppWidgetProvider {
    private static final String LOG_TAG = StackWidgetProvider.class.getSimpleName();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Log.v(LOG_TAG, "onUpdate(): appWidgetId = " + appWidgetId);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_stack);

            Intent launchIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_stack_container, pendingIntent);

            Intent intent = new Intent(context, StackWidgetRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(R.id.widget_stack, intent);

            Intent clickIntentTemplate =  new Intent(context, MovieActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_stack, clickPendingIntentTemplate);

            views.setEmptyView(R.id.widget_stack, R.id.widget_empty);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
