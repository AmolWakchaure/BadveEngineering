package engineering.badve.badveengineering.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.activity.AddCellActivity;
import engineering.badve.badveengineering.classes.BeplLog;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.classes.VolleyCallback;
import engineering.badve.badveengineering.classes.VolleyErrorCallback;
import engineering.badve.badveengineering.classes.VolleyErrors;
import engineering.badve.badveengineering.classes.VolleyResponseClass;
import engineering.badve.badveengineering.dashboard.HomeActivity;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.interfaces.Constants;
import engineering.badve.badveengineering.pojo.CellInfo;


/**
 * Created by snsystem_amol on 13-Apr-17.
 */

public class LoadDailyData extends Service
{
    int i = 0;

    private String parseResponses,cellIdjson;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public final static String MY_ACTION = "MY_ACTION";

    Context context;

   // private SharedPreferences preferencesSeconds;
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

    public LoadDailyData()
    {

    }
    @Override
    public void onCreate() {

    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId)
    {

        //compile 'com.jakewharton:butterknife:7.0.1'
        //@Bind(R.id.textView1) TextView title;
        try
        {
            //T.t(this,"Service started");
            context = this;
            //long INTERVAL_MSEC = 900000;
            loadDataEveryThreeMin();
            //loadDataWithSpecifiedTime();
        }
        catch (Exception e)
        {
            Log.e("systemTime","systemTime :"+e);
        }
        return mStartMode;
    }

    private void loadDataEveryThreeMin()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {

                if(T.checkConnection(context))
                {
                    Log.e("STATUS","loadDataEveryThreeMin  Time : "+T.getSystemDateTime());
                    checkDataUpdatedOrNot();
                }
                //Do 1minutes
                handler.postDelayed(this, 180000);
                //handler.postDelayed(this, 15000);

            }
        }, 180000);
   // }, 15000);

    }
    private void checkDataUpdatedOrNot()
    {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        ArrayList<CellInfo> cell_id = S.getAllCellIDForJsonArray();
        final String cellIdJsonArray = T.arrayListToJsonArrayCellId(cell_id);

        String[] parameters =
                {
                        "cell_data" + "#" + cellIdJsonArray,
                        "data_flag" + "#" + "cell"
                };
        VolleyResponseClass.chekDataUpdtate(
                new VolleyCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {


                        ArrayList<String> UPDATE_LIST = new ArrayList<>();
                        try
                        {
                            if (result != null || result.length() > 0)
                            {
                                Object json = new JSONTokener(result).nextValue();
                                if (json instanceof JSONObject)
                                {
                                    JSONObject dataJsonObject = new JSONObject(result);
                                    String success = dataJsonObject.getString("status");

                                  //  T.t(MyApplication.context,"Status : "+success);

                                    Log.e("STATUS","checkDataUpdatedOrNot :"+result+" Time : "+T.getSystemDateTime());
                                    if (success.equals("1"))
                                    {
                                        JSONArray jsonArray = dataJsonObject.getJSONArray("result");

                                        for(int i = 0; i < jsonArray.length(); i++)
                                        {
                                            UPDATE_LIST.add(jsonArray.get(i).toString());
                                        }

                                        String updateCellJsonArray = T.createCellJsonForCheckDataUpdate(UPDATE_LIST);

                                        if(T.checkConnection(context))
                                        {
                                            loadServerDataThreeMin(updateCellJsonArray);

                                        }

                                    }



                                }
                            }
                        }
                        catch (Exception e)
                        {
                            T.e(""+e);
                        }


                    }
                },
                new VolleyErrorCallback()
                {
                    @Override
                    public void onError(VolleyError result)
                    {

                        /*BeplLog.storeerror(
                                " Webservice Name : "+getResources().getString(R.string.whatStatus)+
                                        " cell_id : "+cellIdJsonArray+
                                        " data_flag : "+"cell"+
                                        " checkDataUpdatedOrNot() : "+result+
                                        " DateTime : "+T.getSystemDateTime());*/
                        VolleyErrors.handleError(result,context);
                    }
                },
                getResources().getString(R.string.webUrl) + "" + getResources().getString(R.string.whatStatus),
                parameters);

    }


    private void loadServerDataThreeMin(final String updateCellJsonArray)
    {

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        Log.e("DATAAA",""+updateCellJsonArray);
        String[] parameters =
                {
                        "cell_id"+"#"+updateCellJsonArray,
                        "date" + "#" + T.getSystemDate()
                        //"cell_id"+"#"+"1109C00016"
                };
        VolleyResponseClass.getResponseProgressDialogThreeMin(
                new VolleyCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {

                        parseResponses = result;
                        cellIdjson = updateCellJsonArray;
                        new ParseResponses().execute();

                    }
                },
                new VolleyErrorCallback()
                {
                    @Override
                    public void onError(VolleyError result)
                    {

                        VolleyErrors.handleError(result,context);
                    }
                },
                getResources().getString(R.string.webUrl) + "" + getResources().getString(R.string.load_data),
                parameters);
    }

    class ParseResponses extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            try
            {
                //shift
                String shift_name = Constants.NA;
                String shift_from = Constants.NA;
                String shift_to = Constants.NA;

                String cell = Constants.NA;

                //employee
                String RFID_no = Constants.NA;
                String employee_code = Constants.NA;
                String contractual_type_id = Constants.NA;

                //booking
                String contractual_type = Constants.NA;
                String target = Constants.NA;
                String shift = Constants.NA;
                String contractor_code = Constants.NA;
                String contractor_id = Constants.NA;
                String id = Constants.NA;

                String tollerence = Constants.NA;

                String cell_id = Constants.NA;

                StringBuffer cellStringBuffer = new StringBuffer();

                String [] systemDateArray = T.getSystemDateTime().split(" ");
                String systemDate = systemDateArray[0];


                if(parseResponses != null || parseResponses.length() > 0)
                {
                    Object json = new JSONTokener(parseResponses).nextValue();
                    if (json instanceof JSONObject)
                    {
                        JSONObject dataJsonObject = new JSONObject(parseResponses);
                        String success = dataJsonObject.getString("success");

                        if(success.equals("1"))
                        {
                            JSONArray cellJsonArray = dataJsonObject.getJSONArray("cell");
                            JSONArray employeeJsonArray = dataJsonObject.getJSONArray("employee");
                            JSONArray bookingJsonArray = dataJsonObject.getJSONArray("booking");
                            JSONArray shiftJsonArray = dataJsonObject.getJSONArray("shift");


                            /*if(!checkEmptyArrayThreeMin(cellJsonArray,"Cell details not found."))
                            {
                                return;
                            }
                            if(!checkEmptyArrayThreeMin(employeeJsonArray,"Employee details not found."))
                            {
                                return;
                            }
                            if(!checkEmptyArrayThreeMin(bookingJsonArray,"Booking details not found."))
                            {
                                return;
                            }
                            if(!checkEmptyArrayThreeMin(shiftJsonArray,"Shift details not found."))
                            {
                                return;
                            }*/

                            //delete prev data
                            String todayDate = T.getSystemDate();
                            String yesterdayDate = T.displayYesterdasDate(-1);
                            MyApplication.db.clearTable(todayDate,yesterdayDate);

                            for(int i = 0; i < cellJsonArray.length();i++)
                            {
                                cellStringBuffer.append(cellJsonArray.get(i)+",");
                            }
                            for(int e = 0; e < cellJsonArray.length(); e++)
                            {
                                JSONArray eJsonArray = employeeJsonArray.getJSONArray(e);

                                if(eJsonArray.length() != 0)
                                {
                                    for(int ae = 0; ae < eJsonArray.length(); ae++)
                                    {
                                        JSONObject employeeJsonObject = eJsonArray.getJSONObject(ae);

                                        if(employeeJsonObject.has("cell_id") && !employeeJsonObject.isNull("cell_id"))
                                        {
                                            cell_id = employeeJsonObject.getString("cell_id");
                                        }


                                        if(employeeJsonObject.has("RFID_no") && !employeeJsonObject.isNull("RFID_no"))
                                        {
                                            RFID_no = employeeJsonObject.getString("RFID_no");
                                        }

                                        if(employeeJsonObject.has("employee_code") && !employeeJsonObject.isNull("employee_code"))
                                        {
                                            employee_code = employeeJsonObject.getString("employee_code");
                                        }
                                        if(employeeJsonObject.has("contractual_type_id") && !employeeJsonObject.isNull("contractual_type_id"))
                                        {
                                            contractual_type_id = employeeJsonObject.getString("contractual_type_id");
                                        }
                                        if(employeeJsonObject.has("contractor_id") && !employeeJsonObject.isNull("contractor_id"))
                                        {
                                            contractor_id = employeeJsonObject.getString("contractor_id");
                                        }

                                        //MyApplication.db.insertEmployeeDetails(RFID_no.trim(),employee_code,contractual_type_id,contractor_id,cell_id,systemDate,Constants.SYNCH_STATUS_N);
                                        String alreadyEmployee = S.checkEmployeeAlreadyEntryf(RFID_no.trim(),employee_code,systemDate);

                                        if(alreadyEmployee != null)
                                        {
                                            if(alreadyEmployee.equals("1"))
                                            {
                                                MyApplication.db.updateEmployeeDetails(RFID_no.trim(),employee_code,contractual_type_id,contractor_id,cell_id,systemDate);
                                            }
                                            else if(alreadyEmployee.equals("0"))
                                            {
                                                MyApplication.db.insertEmployeeDetails(RFID_no.trim(),employee_code,contractual_type_id,contractor_id,cell_id,systemDate,Constants.SYNCH_STATUS_N);

                                            }
                                        }

                                        //MyApplication.db.insertEmployeeDetails(RFID_no.trim(),employee_code,contractual_type_id,contractor_id,cell_id,systemDate,Constants.SYNCH_STATUS_N);
                                    }
                                }

                            }
                            for(int e = 0; e < cellJsonArray.length(); e++)
                            {
                                JSONArray eJsonArray = bookingJsonArray.getJSONArray(e);

                                if (eJsonArray.length() != 0)
                                {
                                    for(int ae = 0; ae < eJsonArray.length(); ae++)
                                    {
                                        JSONObject bookingJsonObject = eJsonArray.getJSONObject(ae);

                                        if(bookingJsonObject.has("id") && !bookingJsonObject.isNull("id"))
                                        {
                                            id = bookingJsonObject.getString("id");
                                        }

                                        if(bookingJsonObject.has("cell_id") && !bookingJsonObject.isNull("cell_id"))
                                        {
                                            cell_id = bookingJsonObject.getString("cell_id");
                                        }

                                        if(bookingJsonObject.has("contractual_type") && !bookingJsonObject.isNull("contractual_type"))
                                        {
                                            contractual_type = bookingJsonObject.getString("contractual_type");
                                        }
                                        if(bookingJsonObject.has("contractor_code") && !bookingJsonObject.isNull("contractor_code"))
                                        {
                                            contractor_code = bookingJsonObject.getString("contractor_code");
                                        }
                                        if(bookingJsonObject.has("target") && !bookingJsonObject.isNull("target"))
                                        {
                                            target = bookingJsonObject.getString("target");
                                        }
                                        if(bookingJsonObject.has("shift") && !bookingJsonObject.isNull("shift"))
                                        {
                                            shift = bookingJsonObject.getString("shift");
                                        }

                                        if(bookingJsonObject.has("tolerance") && !bookingJsonObject.isNull("tolerance"))
                                        {
                                            tollerence = bookingJsonObject.getString("tolerance");
                                        }

                                        //check already
                                        String status = S.checkAlreadyBooking(contractual_type, shift, contractor_code, cell_id, systemDate,id);

                                        if(status != null)
                                        {
                                            if (status.equals("1"))
                                            {

                                                double updated_target0 = Double.valueOf(target) / 100;
                                                double updated_target1 = updated_target0 * Double.valueOf(tollerence);
                                                double updated_target = T.roundUP(updated_target1);

                                                double updatedd_target = updated_target + Long.valueOf(target);

                                                MyApplication.db.updateTarget(
                                                        String.valueOf(target),
                                                        contractual_type,
                                                        shift,
                                                        contractor_code,
                                                        cell_id,
                                                        systemDate,
                                                        tollerence,
                                                        ""+(Integer.valueOf((int) updatedd_target)));
                                            }
                                            else  if (status.equals("0"))
                                            {

                                                double updated_target0 = Double.valueOf(target) / 100;
                                                double updated_target1 = updated_target0 * Double.valueOf(tollerence);
                                                double updated_target = T.roundUP(updated_target1);

                                                double updatedd_target = updated_target + Long.valueOf(target);

                                                MyApplication.db.insertBookingDetails(
                                                        contractual_type,
                                                        target,
                                                        "0",
                                                        "0",
                                                        shift,
                                                        contractor_code,
                                                        cell_id,
                                                        id,
                                                        systemDate,
                                                        Constants.SYNCH_STATUS_N,
                                                        tollerence,
                                                        ""+(Integer.valueOf((int) updatedd_target)));
                                            }
                                        }

                                    }

                                }
                            }
                            //}
                            //shift
                            for(int i = 0; i < shiftJsonArray.length(); i++)
                            {

                                JSONObject shiftJsonObject = shiftJsonArray.getJSONObject(i);

                                if(shiftJsonObject.has("shift_name") && !shiftJsonObject.isNull("shift_name"))
                                {
                                    shift_name = shiftJsonObject.getString("shift_name");
                                }
                                if(shiftJsonObject.has("shift_from") && !shiftJsonObject.isNull("shift_from"))
                                {
                                    String shift_from_convert = shiftJsonObject.getString("shift_from");
                                    String timeFormatServer = T.convertHrsTosecond(shift_from_convert);
                                    Integer timeSec = Integer.valueOf(timeFormatServer) - 1800;
                                    String main_time_after_minus_onehrs  = T.timeConvert(timeSec);
                                    shift_from = main_time_after_minus_onehrs;
                                }
                                if(shiftJsonObject.has("shift_to") && !shiftJsonObject.isNull("shift_to"))
                                {
                                    shift_to = shiftJsonObject.getString("shift_to");
                                }
                                String shiftAlready = S.checkShiftAlreadyEntry(shift_name, shift_from, shift_to, systemDate, Constants.SYNCH_STATUS_N);
                                if(shiftAlready != null)
                                {
                                    if (shiftAlready.equals("0"))
                                    {
                                        MyApplication.db.insertShiftDetails(shift_name, shift_from, shift_to, systemDate, Constants.SYNCH_STATUS_N);
                                    }
                                }

                            }

                        }
                        else
                        {
                            //T.t(context,"No data found");
                            //T.displayErrorMessage(context,"Oops","OK","No attendance details found");
                        }
                    }
                    else
                    {
                       //T.t(context, "incorect json");
                    }
                }
                else
                {
                   // T.t(context,"0 or null json");
                }
            }
            catch(Exception e)
            {
               // T.t(context, "Exception :"+e);
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            //send broad cast when data successfully inserted
            Intent intent = new Intent();
            intent.setAction(MY_ACTION);
            intent.putExtra("DATAPASSED", "ok#"+cellIdjson);
            sendBroadcast(intent);
        }
    }


    public boolean checkEmptyArrayThreeMin(JSONArray jsonArray, String message)
    {
        if (jsonArray.length() == 0)
        {
            //T.t(context,message);
            //requestFocus(editText);
            return false;
        }
        else
        {
            return true;
        }


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
