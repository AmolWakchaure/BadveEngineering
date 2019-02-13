package engineering.badve.badveengineering.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.interfaces.Constants;


/**
 * Created by snsystem_amol on 20-Apr-17.
 */

public class ResetScreens extends Service
{
    int i = 0;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public final static String MY_ACTION = "MY_ACTION";

    Context context;
    /*

     $device_id = $this->post('device_id');
   $email_id = $this->post('email_id');

     */
    /** indicates how to behave if the service is killed */
    int mStartMode = START_STICKY;

    /** interface for clients that bind */
    IBinder mBinder;

    /*indicates whether onRebind should be used */
    boolean mAllowRebind;

    /** Called when the service is being created. */

    public ResetScreens()
    {

    }
    @Override
    public void onCreate() {

    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        //compile 'com.jakewharton:butterknife:7.0.1'
        // @Bind(R.id.textView1) TextView title;
        try
        {
            //T.t(this,"Service started");



            context = this;
            //long INTERVAL_MSEC = 900000;



            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    String shiftAlready = S.checkAvailShift(MyApplication.db);

                    if(shiftAlready != null)
                    {
                        if(shiftAlready.equals("1"))
                        {
                            ArrayList<String> fromTimeTotimeShiftName = S.getfromTimeTotimeShiftName(MyApplication.db);

                            String shiftEndTime = S.getShiftTimeLast(MyApplication.db);
                            //get shift name here
                            String shiftName = T.manageTimesAndShifts(fromTimeTotimeShiftName);


                            if(T.getSystemTimeAmpPsm().equals(shiftEndTime))
                            {
                                Intent intent = new Intent();
                                intent.setAction(MY_ACTION);

                                intent.putExtra("DATAPASSED", "shiftend#shiftend");
                                sendBroadcast(intent);
                            }
                            else
                            {
                                if(!shiftName.equals("notfound"))
                                {
                                    //T.e("FOUND");
                                    String fromTime = S.getShiftTimeUsingName(MyApplication.db,shiftName);
                                    //String serverTimeOutput = convertServerTime(fromTime);
                                    String timeDevice = T.getSystemTimeAmpPsm();
                                    //String deviceTimeOutput = convertDeviceTime(timeDevice);

                                    if(timeDevice.equals(fromTime))
                                    {
                                        //send broad cast when data successfully inserted
                                        Intent intent = new Intent();
                                        intent.setAction(MY_ACTION);
                                        intent.putExtra("DATAPASSED", "resetscreens#"+shiftName);
                                        sendBroadcast(intent);

                                    }

                                }

                            }

                        }
                    }

                    //Do 1minutes
                    handler.postDelayed(this, 1000);

                }
            }, 1000);

        }
        catch (Exception e)
        {
            Log.e("systemTime","systemTime :"+e);
        }
        return mStartMode;
    }

    private static String convertServerTime(String timeString)
    {
        String[] timeData = timeString.split(":");

        StringBuffer buffer = new StringBuffer();

        buffer.append(timeData[0]+"#"+timeData[1]);


        return buffer.toString().replace("#", ":");

    }
    private static String convertDeviceTime(String timeString)
    {
        String[] timeData = timeString.split(" ");

        return timeData[0];

    }


    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        T.t(this,"Service stop");


    }
    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent)
    {

    }


    @Override
    public IBinder onBind(Intent intent)
    {

        return null;
    }


}
