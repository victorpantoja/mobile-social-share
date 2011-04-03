/**
 * 
 */
package com.victorpantoja.mss.widget;

import com.victorpantoja.mss.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

/**
 * @author victor.pantoja
 *
 */
public class MssWidgetProvider extends AppWidgetProvider{
	
	@Override
    public void onUpdate( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds )
    {
        RemoteViews remoteViews;
        ComponentName watchWidget;
        DateFormat format = SimpleDateFormat.getTimeInstance( SimpleDateFormat.MEDIUM, Locale.getDefault() );

        remoteViews = new RemoteViews( context.getPackageName(), R.layout.widget );
        watchWidget = new ComponentName( context, MssWidgetProvider.class );
        remoteViews.setTextViewText( R.id.widget_textview, "Time = " + format.format( new Date()));
        appWidgetManager.updateAppWidget( watchWidget, remoteViews );
    }

}
