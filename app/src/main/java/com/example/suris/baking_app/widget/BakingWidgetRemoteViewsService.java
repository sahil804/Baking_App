package com.example.suris.baking_app.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;


/**
 * Created by sahil on 10/8/2018.
 */
public class BakingWidgetRemoteViewsService extends RemoteViewsService {

    public static void updateWidget(Context context) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingWidget.class));
        BakingWidget.updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingWidgetRemoteViewFactory(this.getApplicationContext());
    }
}
