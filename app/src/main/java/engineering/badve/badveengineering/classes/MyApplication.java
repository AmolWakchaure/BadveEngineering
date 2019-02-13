package engineering.badve.badveengineering.classes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.BatteryManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;

import engineering.badve.badveengineering.database.DBHelper;
import engineering.badve.badveengineering.hardware.FT311UARTInterface;
import engineering.badve.badveengineering.interfaces.Constants;


/* Created by snsystem_amol on 4/25/2017.*/

public class MyApplication extends Application
{

    public static DBHelper db = null;
    public static Context context;

    public static SharedPreferences prefsHardware;
    public static SharedPreferences.Editor editorHardware;
    public static FT311UARTInterface uartInterface;

    public static SharedPreferences prefs;
    public static SharedPreferences.Editor editor;

    public static RequestQueue requestQueue = null;
    public static RetryPolicy retryPolicy;

    //for get battery status
    public static BatteryManager batteryManager = null;
    @Override
    public void onCreate()
    {
        super.onCreate();

        context = getApplicationContext();

        if (db == null)
        {
            db = new DBHelper(context);
            db.getWritableDatabase();
        }

        //initialise BatteryManager when null found
        if (batteryManager == null)
        {
            batteryManager = (BatteryManager)getSystemService(BATTERY_SERVICE);
        }

        if(requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(context);
        }
        retryPolicy = new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        prefsHardware = this.getSharedPreferences(Constants.HARWARE_INTERFACE, Context.MODE_PRIVATE);
        editorHardware = prefsHardware.edit();
        uartInterface = new FT311UARTInterface(this, prefsHardware);

        prefs = getSharedPreferences(Constants.PREF_KEY, 0);
        editor = prefs.edit();
        editor.commit();

    }


}
