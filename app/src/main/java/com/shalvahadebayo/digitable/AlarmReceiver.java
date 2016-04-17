package com.shalvahadebayo.digitable;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;


public class AlarmReceiver extends WakefulBroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
					.setSmallIcon(android.R.drawable.stat_notify_more)
					.setContentTitle("Alarm Notif")
					.setContentText("Alarm Time");

			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(context, AssignmentListActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(AssignmentListActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
					stackBuilder.getPendingIntent(
							0,
							PendingIntent.FLAG_UPDATE_CURRENT
					);

			int mId = 0;
			nb.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
					(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
			mNotificationManager.notify(mId, nb.build());

		}

		public class SampleBootReceiver extends BroadcastReceiver
			{

				@Override
				public void onReceive(Context context, Intent intent)
				{
					/*
					if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
					{
						// Set the alarm here.
					}*/
				}
			}
	}
