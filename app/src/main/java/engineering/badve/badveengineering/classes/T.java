package engineering.badve.badveengineering.classes;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.interfaces.Constants;
import engineering.badve.badveengineering.pojo.CellInfo;
import engineering.badve.badveengineering.pojo.TypeInOutTargetInfo;


/**
 * Created by snsystem_amol on 3/11/2017.
 */

public class T
{
    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static String getSystemTimeAmpPsm()
    {

        String systemTime = null;

        try {
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            systemTime = df.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return systemTime;

    }
    public static double roundUP(double d)
    {
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if(result==0.0)
        {
            return (int) d;
        }
        else
        {
            return (int) d<0 ? -(i+1) : i+1;
        }
    }
    public static String returnVersionName()
    {
        String version = null;
        try
        {
            PackageInfo pInfo = MyApplication.context.getPackageManager().getPackageInfo(MyApplication.context.getPackageName(), 0);
            version = pInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }
    public static Boolean isSimStateReadyorNotReady()
    {
        boolean status = false;

        try
        {
            TelephonyManager tm = (TelephonyManager) MyApplication.context.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
            if (tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT)
            {
                //the phone has a sim card
                status = true;
            }
            else
            {
                //no sim card available
                status = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return status;
    }
    public static void sendSMS(String messageText)
    {

        if(isSimStateReadyorNotReady())
        {
            Intent intent = new Intent();
            PendingIntent pi=PendingIntent.getActivity(MyApplication.context, 0, intent,0);

            SmsManager sms= SmsManager.getDefault();
            sms.sendTextMessage(Constants.CONTACT_NUMBER, null, messageText, pi,null);

            //Toast.makeText(MyApplication.context, "Message Sent successfully!", Toast.LENGTH_LONG).show();
        }

    }
    public static ArrayList<TypeInOutTargetInfo> returnDataParsing(String typeTargetInOutData) {

        ArrayList<TypeInOutTargetInfo> DATAA_TARGET = new ArrayList<>();

        //FITTER#3#1#1@WELDER#3#2#2@
        try
        {
            String [] splitAtTheRate  = typeTargetInOutData.split("@");

            for(int i = 0; i < splitAtTheRate.length; i++)
            {

                String [] dattaHAsh = splitAtTheRate[i].split("#");

                TypeInOutTargetInfo typeInOutTargetInfo = new TypeInOutTargetInfo();

                typeInOutTargetInfo.setDesignationType(dattaHAsh[0]);
                typeInOutTargetInfo.setInTarget(dattaHAsh[1]);
                typeInOutTargetInfo.setInCount(dattaHAsh[2]);
                typeInOutTargetInfo.setOutCount(dattaHAsh[3]);
                typeInOutTargetInfo.setShiftName(dattaHAsh[4]);
                typeInOutTargetInfo.setDateDate(dattaHAsh[5]);
                typeInOutTargetInfo.setOutEntryStatus("new");

                DATAA_TARGET.add(typeInOutTargetInfo);

            }

            //get previous date data where outdatetime = 0
            //T.displayYesterdasDate(-5)
            String  screenDataCheckStatus = S.getAllBookingDetailsNotShiftWise(MyApplication.db,T.displayYesterdasDate(-1));

            if(!(screenDataCheckStatus.equals("NA")))
            {
                String [] splitAtTheRate1  = screenDataCheckStatus.split("@");

                for(int i = 0; i < splitAtTheRate1.length; i++)
                {

                    String [] dattaHAsh = splitAtTheRate1[i].split("#");

                    TypeInOutTargetInfo typeInOutTargetInfo = new TypeInOutTargetInfo();

                    typeInOutTargetInfo.setDesignationType(dattaHAsh[0]);
                    typeInOutTargetInfo.setInTarget(dattaHAsh[1]);
                    typeInOutTargetInfo.setInCount(dattaHAsh[2]);
                    typeInOutTargetInfo.setOutCount(dattaHAsh[3]);
                    typeInOutTargetInfo.setShiftName(dattaHAsh[4]);
                    typeInOutTargetInfo.setDateDate(dattaHAsh[5]);
                    typeInOutTargetInfo.setOutEntryStatus("old");

                    DATAA_TARGET.add(typeInOutTargetInfo);

                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("DATA","EXCEPTION "+e);
        }

        return DATAA_TARGET;
    }



    public static void askPermission(Context context,String permission)
    {

        ActivityCompat.requestPermissions(
                (Activity) context,
                new String[]{permission},
                12
        );
    }
    public static String getSystemDate()
    {
        String systemTime = null;
        try
        {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            systemTime = df.format(Calendar.getInstance().getTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return systemTime;

    }
    public static String getSystemDateTimeTwentyFour()
    {

        String systemTime = null;

        try
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            systemTime = df.format(Calendar.getInstance().getTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return systemTime;

    }

    public static String getSystemDateTime()
    {
        String systemTime = null;
        try
        {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            systemTime = df.format(Calendar.getInstance().getTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return systemTime;
    }

    public static  void e(String message)
    {
        Log.e("BEPL",""+message);
    }


    public static  void t(Context context, String message)
    {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_LONG, MDToast.TYPE_WARNING);
        mdToast.show();
    }
    public static  void tI(Context context, String message)
    {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_LONG, MDToast.TYPE_INFO);
        mdToast.show();
    }
    public static  void tS(Context context, String message)
    {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS);
        mdToast.show();
    }
    public static  void tW(Context context, String message)
    {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_LONG, MDToast.TYPE_WARNING);
        mdToast.show();
    }
    public static  void tE(Context context, String message)
    {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_LONG, MDToast.TYPE_ERROR);
        mdToast.show();
    }
    public static  void tET(Context context, String message)
    {
        MDToast mdToast = MDToast.makeText(context, message, 5000, MDToast.TYPE_ERROR);
        mdToast.show();
    }
    public static  void tIT(Context context, String message)
    {
        MDToast mdToast = MDToast.makeText(context, message, 2500, MDToast.TYPE_INFO);
        mdToast.show();
    }
    public static  void tSS(Context context, String message)
    {
        MDToast mdToast = MDToast.makeText(context, message, 2500, MDToast.TYPE_SUCCESS);
        mdToast.show();
    }

    public static boolean validateEmptyField(EditText editText, String message, Context context)
    {
        if (editText.getText().toString().trim().isEmpty())
        {
            t(context,message);
            //requestFocus(editText);
            return false;
        }
        else
        {
            return true;
        }
    }
    public static boolean checkConnection(Context context)
    {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;

    }

    public static boolean  handleVolleyerror(com.android.volley.VolleyError error,View view)
    {
        if(error instanceof TimeoutError || error instanceof NoConnectionError)
        {
            return true;
        }
        else if(error instanceof AuthFailureError)
        {
            t(view.getContext(),"AuthFailureError");
            return true;
        }
        else if(error instanceof ServerError)
        {
            t(view.getContext(),"ServerError");
            return true;

        }
        else if (error instanceof NetworkError)
        {
            t(view.getContext(),"NetworkError");
            return true;
        }
        else if(error instanceof ParseError)
        {
            t(view.getContext(),"ParseError");
            return true;
        }
        else
        {
            return false;
        }



    }



    public static String getSystemTime()
    {

        String systemTime = null;

        try {
            DateFormat df = new SimpleDateFormat("hh:mm:ss");
            systemTime = df.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return systemTime;

    }
    public static String getSystemTimeTewntyFour()
    {

        String systemTime = null;

        try {
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            systemTime = df.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return systemTime;

    }
    public static String displayYesterdasDate(int noofDays)
    {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, noofDays);

        String datee = dateFormat.format(cal.getTime());

         return datee;

    }
    public static String getSystemTimeTwentyFourHrs()
    {

        String systemTime = null;

        try {
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            systemTime = df.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return systemTime;

    }
    public static String getSystemTimeAmpPm()
    {

        String systemTime = null;

        try {
            DateFormat df = new SimpleDateFormat("HH:mm aaa");
            systemTime = df.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return systemTime;

    }
    public static String manageTimesAndShifts(ArrayList<String> fromTimeTotimeShiftName)
    {

        ArrayList<String> SHIFT_NAMES = new ArrayList<>();

        String shiftName;


        String[] shiftname = new String[fromTimeTotimeShiftName.size()];
        String[] timessFrom = new String[fromTimeTotimeShiftName.size()];
        String[] timessTo = new String[fromTimeTotimeShiftName.size()];


        for(int  i = 0; i < fromTimeTotimeShiftName.size(); i++)
        {
            String[] fromTimeTotimeShiftNameData = fromTimeTotimeShiftName.get(i).split("#");

            shiftname[i] = fromTimeTotimeShiftNameData[0];
            timessFrom[i] = fromTimeTotimeShiftNameData[1];
            timessTo[i] = fromTimeTotimeShiftNameData[2];
        }

        for(int i = 0; i < timessFrom.length; i++)
        {
            String shiftTimesFrom = convertHrsTosecond(timessFrom[i]);
            String shiftTimesTo = convertHrsTosecond(timessTo[i]);
            String getSystemTime = getSystemTimeTwentyFourHrs();
            String systemTime = convertHrsTosecond(getSystemTime);

            if(Integer.valueOf(systemTime) >= Integer.valueOf(shiftTimesFrom) && Integer.valueOf(systemTime) <= Integer.valueOf(shiftTimesTo))
            {
                //System.out.println(""+ids[i]);
                SHIFT_NAMES.add(shiftname[i]);
            }
            else if(Integer.valueOf(systemTime) >= Integer.valueOf(shiftTimesFrom) && Integer.valueOf(systemTime) >= Integer.valueOf(shiftTimesTo))
            {
                SHIFT_NAMES.add(shiftname[i]);
            }
        }

        if(SHIFT_NAMES.isEmpty())
        {

            shiftName = "notfound";
            //System.out.println("Shift not found");
        }
        else
        {
            shiftName = SHIFT_NAMES.get(SHIFT_NAMES.size() - 1);
            //System.out.println("Shift found :"+SHIFT_NAMES.get(0));

        }

        return shiftName;
    }
    public static String convertHrsTosecond(String timeFormatString)
    {


        String timeSplit[] = timeFormatString.split(":");
        int seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 +  Integer.parseInt(timeSplit[1]) * 60 + Integer.parseInt(timeSplit[2]);

        return String.valueOf(seconds);
    }
    public static String timeConvert(int time){

        int hr;
        int mn;
        int sec;

        String data;


        hr = (int)(time/3600);
        int rem = (int)(time%3600);
        mn = rem/60;
        sec = rem%60;
        String hrStr = (hr<10 ? "0":"")+hr;
        String mnStr = (mn<10 ? "0":"")+mn;
        String secStr = (sec<10 ? "0":"")+sec;


        data = hrStr+":"+mnStr+":"+secStr;

        return data;
    }
    public static void getConnectedWifiName(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(     ConnectivityManager.TYPE_MOBILE );
        if (activeNetInfo != null)
        {

            Toast.makeText( context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
        }
        if( mobNetInfo != null )
        {
            Toast.makeText( context, "Mobile Network Type : " + mobNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
        }

    }
    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }
    public static void displaySuccessMessage(String titleText)
    {
         new SweetAlertDialog(MyApplication.context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titleText)
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
//                {
//                    @Override
//                    public void onClick(final SweetAlertDialog sDialog)
//                    {
//                        final Timer t = new Timer();
//                        t.schedule(new TimerTask()
//                        {
//                            public void run()
//                            {
//                                sDialog.dismissWithAnimation();
//                                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
//                            }
//                        }, 3000); // after 2 second (or 2000 miliseconds), the task will be active.
//                        sDialog.dismissWithAnimation();
//                    }
//                })
                .show();
    }

    public static void displayErrorMessage(String titleText,
                                             String confirmText,
                                             String contentText)
    {
        new SweetAlertDialog(MyApplication.context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titleText)
                .setConfirmText(confirmText)
                .setContentText(contentText)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog)
                    {
                        final Timer t = new Timer();
                        t.schedule(new TimerTask()
                        {
                            public void run()
                            {
                                sDialog.dismissWithAnimation();
                                t.cancel(); //also just top the timer thread, otherwise, you may receive a crash report
                            }
                        }, 3000); //after 2 second (or 2000 miliseconds), the task will be active.
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public static String arrayListToJsonArray(String rfid_no)
    {
        
        String jsonArrrayString = null;

        try
        {
            MyApplication.editor.commit();
            String plant_code = MyApplication.prefs.getString(Constants.PLANT_CODE,"0");
            MyApplication.editor.commit();


            JSONArray jsArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rfid_no",rfid_no);
            jsonObject.put("date_time",T.getSystemDateTimeTwentyFour());
            jsonObject.put("plant_code",plant_code);
            jsArray.put(jsonObject);

            jsonArrrayString = jsArray.toString().trim();
            
        }
        catch (Exception e)
        {

        }
        
        return jsonArrrayString;
    }

    public static String createCellJsonForCheckDataUpdate(ArrayList<String> arrayList)
    {

        //[{"cell_id1":"123456782","cell_id0":"123456781","cell_id3":"123456784","cell_id2":"123456783"}]
        String jsonArrrayString = null;

        try
        {
            MyApplication.editor.commit();
            String plant_code = MyApplication.prefs.getString(Constants.PLANT_CODE,"0");
            MyApplication.editor.commit();

            JSONArray jsArray = new JSONArray();

            for (int i = 0; i < arrayList.size(); i++)
            {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cell",arrayList.get(i));
                jsonObject.put("plant",plant_code);
                jsArray.put(jsonObject);

            }

            jsonArrrayString = jsArray.toString().trim();


        }
        catch (Exception e)
        {

        }

        return jsonArrrayString;
    }
    public static String arrayListToJsonArrayCellId(ArrayList<CellInfo> cellIds)
    {

        //[{"cell_id1":"123456782","cell_id0":"123456781","cell_id3":"123456784","cell_id2":"123456783"}]
        String jsonArrrayString = null;

        try
        {

            JSONArray jsArray = new JSONArray();

            MyApplication.editor.commit();
            String plant_code = MyApplication.prefs.getString(Constants.PLANT_CODE,"0");
            MyApplication.editor.commit();

            for(int i =  0; i < cellIds.size(); i++)
            {
                JSONObject jsonObject = new JSONObject();
                CellInfo cellInfo = cellIds.get(i);

                jsonObject.put("cell",cellInfo.getCell_id());
                jsonObject.put("plant",plant_code);

                jsArray.put(jsonObject);
            }
            jsonArrrayString = jsArray.toString().trim();

        }
        catch (Exception e)
        {

        }

        return jsonArrrayString;
    }
    public static String returnPlantCode(String plantCodesString)
    {

        //[{"cell_id1":"123456782","cell_id0":"123456781","cell_id3":"123456784","cell_id2":"123456783"}]
        String jsonArrrayString = null;

        try
        {
            if(plantCodesString.contains(","))
            {
                String [] data = jsonArrrayString.split(",");

                JSONArray jsArray = new JSONArray();

                for(int i =  0; i < data.length; i++)
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("plant_code",data[i]);
                    jsArray.put(jsonObject);
                }

                jsonArrrayString = jsArray.toString().trim();
            }
            else
            {

                JSONArray jsArray = new JSONArray();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("plant_code",plantCodesString);
                jsArray.put(jsonObject);

                jsonArrrayString = jsArray.toString().trim();
            }






        }
        catch (Exception e)
        {

        }

        return jsonArrrayString;
    }
    public static String getSharedPreferenceValue(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        String prevCellId = pref.getString("cell","");

        return prevCellId;
    }



}
