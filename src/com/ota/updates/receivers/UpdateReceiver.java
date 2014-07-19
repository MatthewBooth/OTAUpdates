/*
 * Copyright (C) 2014 Matt Booth (Kryten2k35).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ota.updates.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import com.ota.updates.R;
import com.ota.updates.RomUpdate;
import com.ota.updates.activities.MainActivity;
import com.ota.updates.utils.Constants;


public class UpdateReceiver extends BroadcastReceiver implements Constants{

	public final String TAG = this.getClass().getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action.equals(MANIFEST_CHECK_BACKGROUND)){
			if(DEBUGGING)
				Log.d(TAG, "Receiving background check confirmation");
			
			boolean updateAvailable = RomUpdate.getUpdateAvailability(context);
			String filename = RomUpdate.getFilename(context);
			
			if(updateAvailable){
				setupNotification(context, filename);
			}
		}
	}        

	private void setupNotification(Context context, String filename){
		if(DEBUGGING)
			Log.d(TAG, "Showing notification");
		NotificationManager mNotifyManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent notificationIntent = new Intent(context, MainActivity.class);
	    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    PendingIntent intent = PendingIntent.getActivity(context, 0,
	            notificationIntent, 0);
		Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle(context.getString(R.string.update_available))
		.setContentText(filename)
		.setSmallIcon(R.drawable.ic_notif)
		.setContentIntent(intent)
		.setAutoCancel(true)
		.setPriority(NotificationCompat.PRIORITY_HIGH)
		.setDefaults(NotificationCompat.DEFAULT_ALL);
		mNotifyManager.notify(0, mBuilder.build());
	}
	
}
