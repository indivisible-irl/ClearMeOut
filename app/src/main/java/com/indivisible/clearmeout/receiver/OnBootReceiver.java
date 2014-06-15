package com.indivisible.clearmeout.receiver;

import com.indivisible.clearmeout.service.UpdateAlarmsService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver
{

	private static final String TAG = "CMO:OnBootReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
	    //TODO pref 'clear on boot' that will run DeleteService
		Log.d(TAG, "BOOT_COMPLETE received, triggering UpdateAlarmsService...");
		Intent runClearMeOut = new Intent(context, UpdateAlarmsService.class);
		context.startService(runClearMeOut);
	}

}
