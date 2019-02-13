package engineering.badve.badveengineering.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import engineering.badve.badveengineering.authentication.LoginActivity;
import engineering.badve.badveengineering.authentication.SplashActivity;
import engineering.badve.badveengineering.dashboard.HomeActivity;
import engineering.badve.badveengineering.interfaces.Constants;


/**
 * Created by snsystem_amol on 07-Apr-17.
 */

public class BootCompleteReceiver extends BroadcastReceiver
{
    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent)
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