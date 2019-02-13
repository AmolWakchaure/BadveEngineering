package engineering.badve.badveengineering.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;

import engineering.badve.badveengineering.authentication.SplashActivity;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.interfaces.Constants;

/**
 * Created by snsystem_amol on 07/07/2017.
 */

public class PowerConnectionReceiver extends BroadcastReceiver
{
    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        //Start/stop your service here
        if(!isCharging)
        {

            preferences = context.getSharedPreferences(Constants.NAVIGATION, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent i = new Intent(context, SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }
}
