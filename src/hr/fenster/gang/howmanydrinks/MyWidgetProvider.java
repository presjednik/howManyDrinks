package hr.fenster.gang.howmanydrinks;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

	private static final String ACTION_CLICK = "ClickWidget";
	private static final String ACTION_RESET = "ResetWidget";
	private static final String ACTION_MINUS = "MinusWidget";
	private static final String ACTION_CHANGE = "ChangeWidget";

	private RemoteViews remoteViews;

	private SharedPreferences prefs;
	private Editor editor;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		ComponentName thisWidget = new ComponentName(context,
				MyWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		for (int widgetId : allWidgetIds) {

			prefs = context.getSharedPreferences("", Context.MODE_PRIVATE);
			editor = prefs.edit();

			editor.putBoolean("drink", true);
			editor.commit();

			remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.activity_main);

			Intent configIntent = new Intent(context, MyWidgetProvider.class);
			configIntent.setAction(ACTION_CLICK);
			PendingIntent actionPendingIntent = PendingIntent.getBroadcast(
					context, 0, configIntent, 0);

			Intent resetIntent = new Intent(context, MyWidgetProvider.class);
			resetIntent.setAction(ACTION_RESET);
			PendingIntent resetAPI = PendingIntent.getBroadcast(context, 0,
					resetIntent, 0);

			Intent minusIntent = new Intent(context, MyWidgetProvider.class);
			minusIntent.setAction(ACTION_MINUS);
			PendingIntent minusAPI = PendingIntent.getBroadcast(context, 0,
					minusIntent, 0);

			Intent changeIntent = new Intent(context, MyWidgetProvider.class);
			changeIntent.setAction(ACTION_CHANGE);
			PendingIntent changeAPI = PendingIntent.getBroadcast(context, 0,
					changeIntent, 0);

			remoteViews.setOnClickPendingIntent(R.id.button_update,
					actionPendingIntent);
			remoteViews.setOnClickPendingIntent(R.id.button_reset, resetAPI);
			remoteViews.setOnClickPendingIntent(R.id.button_minus, minusAPI);
			remoteViews.setOnClickPendingIntent(R.id.button_change, changeAPI);

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		final String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {
			remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.activity_main);
			if (intent.getAction().equals(ACTION_CLICK)) {

				prefs = context.getSharedPreferences("", Context.MODE_PRIVATE);
				editor = prefs.edit();

				if (prefs.getBoolean("drink", true) == true) {
					Integer i = prefs.getInt("number", 0);
					i = i + 1;
					editor.putInt("number", i);
					remoteViews.setTextViewText(R.id.button_update,
							i.toString());
				} else {
					Integer i = prefs.getInt("shooter", 0);
					i = i + 1;
					editor.putInt("shooter", i);
					remoteViews.setTextViewText(R.id.button_update,
							i.toString());
				}

				editor.commit();
			}
			if (intent.getAction().equals(ACTION_RESET)) {

				prefs = context.getSharedPreferences("", Context.MODE_PRIVATE);
				editor = prefs.edit();

				if (prefs.getBoolean("drink", true) == true) {
					editor.putInt("number", 0);
					editor.commit();
					remoteViews.setTextViewText(R.id.button_update,
							String.valueOf(prefs.getInt("number", 0)));
				} else {
					editor.putInt("shooter", 0);
					editor.commit();
					remoteViews.setTextViewText(R.id.button_update,
							String.valueOf(prefs.getInt("shooter", 0)));
				}

			}
			if (intent.getAction().equals(ACTION_MINUS)) {

				prefs = context.getSharedPreferences("", Context.MODE_PRIVATE);
				editor = prefs.edit();

				if (prefs.getBoolean("drink", true) == true) {
					Integer i = prefs.getInt("number", 0);
					if (i > 0)
						i = i - 1;
					editor.putInt("number", i);
					remoteViews.setTextViewText(R.id.button_update,
							i.toString());
				} else {
					Integer i = prefs.getInt("shooter", 0);
					if (i > 0)
						i = i - 1;
					editor.putInt("shooter", i);
					remoteViews.setTextViewText(R.id.button_update,
							i.toString());
				}

				editor.commit();

			}
			if (intent.getAction().equals(ACTION_CHANGE)) {

				prefs = context.getSharedPreferences("", Context.MODE_PRIVATE);
				editor = prefs.edit();

				if (prefs.getBoolean("drink", true) == true) {
					remoteViews.setTextViewText(R.id.button_update,
							String.valueOf(prefs.getInt("shooter", 50)));
					remoteViews.setInt(R.id.button_update,
							"setBackgroundResource", R.drawable.badel);
					remoteViews.setInt(R.id.button_change,
							"setBackgroundResource", R.drawable.beer);
					editor.putBoolean("drink", false);
				} else {
					remoteViews.setTextViewText(R.id.button_update,
							String.valueOf(prefs.getInt("number", 50)));
					remoteViews.setInt(R.id.button_update,
							"setBackgroundResource", R.drawable.beer);
					remoteViews.setInt(R.id.button_change,
							"setBackgroundResource", R.drawable.badel);
					editor.putBoolean("drink", true);
				}
				editor.commit();

			}
			AppWidgetManager.getInstance(context).updateAppWidget(
					new ComponentName(context, MyWidgetProvider.class),
					remoteViews);
			super.onReceive(context, intent);
		}
	}
}