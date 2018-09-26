package itstap.edu.homenotification;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class MyWidgwt extends AppWidgetProvider {
    public static int counter=0;
    private static final String ACTION_WIDGET_RECIVER = "ActionResiverWidget";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.my_widgwt);
        Intent activate= new Intent(context,MyWidgwt.class);
        activate.setAction(ACTION_WIDGET_RECIVER);
        PendingIntent pi= PendingIntent.getBroadcast(context, 0, activate, PendingIntent.FLAG_UPDATE_CURRENT);

        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action=intent.getAction();
        if(action.equals(ACTION_WIDGET_RECIVER)){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_widgwt);
            ComponentName thiWidget = new ComponentName(context, MyWidgwt.class);
            remoteViews.setTextViewText(R.id.tvWidg, "" + (counter));
            remoteViews.setImageViewResource(R.id.imgWidg, R.drawable.msred);
            AppWidgetManager.getInstance(context).updateAppWidget(thiWidget, remoteViews);
        }

    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

