package com.smileyjoedev.iou;

import com.smileyjoedev.iou.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetQuickAdd extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_quick_add);
		
		PendingIntent quickAdd = PendingIntent.getActivity(context, 0, Intents.widgetQuickAdd(context), 0);
		
		remoteViews.setOnClickPendingIntent(R.id.iv_quick_add, quickAdd);
		
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		
	}
	
}
