package engineering.badve.badveengineering.synchdata;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.classes.VolleyCallback;
import engineering.badve.badveengineering.classes.VolleyErrorCallback;
import engineering.badve.badveengineering.classes.VolleyResponseClass;
import engineering.badve.badveengineering.dashboard.HomeActivity;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.interfaces.Constants;
import engineering.badve.badveengineering.pojo.CellInfo;
import engineering.badve.badveengineering.services.LoadDailyData;

/**
 * Created by Amol on 22-February-18.
 */

public class LoadDeployment
{



    public static void loadDeploymentWifiOn(String status,Context context)
    {



            if (status.equals("first"))
            {
                if (T.checkConnection(context))
                {

                    SharedPreferences preferences = context.getSharedPreferences(Constants.NAVIGATION, 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.commit();

                    String dateSystem = preferences.getString(Constants.SYSTEM_DATE, "0");
                    String[] dateData = T.getSystemDateTime().split(" ");

                    if (!dateData[0].equals(dateSystem))
                    {

                        String dataStatus = S.checkDataAvailable();

                        if(dataStatus.equals("0"))
                        {
                            getBooking(status,context);
                        }


                    }

                }
                else
                {
                    T.tET(context, "Network connection off");
                }

            }
            else if (status.equals("second"))
            {
                if (T.checkConnection(context))
                {
                    getBooking(status,context);
                }

            }

    }
    private static void getBooking(final String status, final Context context)
    {


        ArrayList<CellInfo> cell_id = S.getAllCellIDForJsonArray();

        final String cellIdJsonArray = T.arrayListToJsonArrayCellId(cell_id);

        String[] parameters =
                {
                        "cell_id" + "#" + cellIdJsonArray,
                        "date" + "#" + T.getSystemDate()

                };
        VolleyResponseClass.getBookingData(
                new VolleyCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {

                        String[] myTaskParams = { result, status };
                        new ParseResponses().execute(myTaskParams);
                    }
                },
                new VolleyErrorCallback()
                {
                    @Override
                    public void onError(VolleyError result)
                    {



                    }
                },
                context.getResources().getString(R.string.webUrl) + "" + context.getResources().getString(R.string.load_data),
                parameters);


    }
   static class ParseResponses extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... voids)
        {

            try
            {

                String resultResponse = voids[0];
                String resultStatus = voids[1];
                //shift
                String shift_name = Constants.NA;
                String shift_from = Constants.NA;
                String shift_to = Constants.NA;

                String cell_id = Constants.NA;
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

                StringBuffer cellStringBuffer = new StringBuffer();

                String[] systemDateArray = T.getSystemDateTime().split(" ");
                String systemDate = systemDateArray[0];

                if (resultResponse != null || resultResponse.length() > 0) {
                    Object json = new JSONTokener(resultResponse).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject dataJsonObject = new JSONObject(resultResponse);
                        String success = dataJsonObject.getString("success");

                        if (success.equals("1"))
                        {

                            JSONArray cellJsonArray = dataJsonObject.getJSONArray("cell");
                            JSONArray employeeJsonArray = dataJsonObject.getJSONArray("employee");
                            JSONArray bookingJsonArray = dataJsonObject.getJSONArray("booking");
                            JSONArray shiftJsonArray = dataJsonObject.getJSONArray("shift");


                            for (int i = 0; i < cellJsonArray.length(); i++)
                            {
                                cellStringBuffer.append(cellJsonArray.get(i) + ",");
                            }

                            for (int e = 0; e < cellJsonArray.length(); e++) {
                                JSONArray eJsonArray = employeeJsonArray.getJSONArray(e);

                                if (eJsonArray.length() != 0) {
                                    for (int ae = 0; ae < eJsonArray.length(); ae++) {
                                        JSONObject employeeJsonObject = eJsonArray.getJSONObject(ae);

                                        if (employeeJsonObject.has("cell_id") && !employeeJsonObject.isNull("cell_id")) {
                                            cell_id = employeeJsonObject.getString("cell_id");
                                        }

                                        if (employeeJsonObject.has("RFID_no") && !employeeJsonObject.isNull("RFID_no")) {
                                            RFID_no = employeeJsonObject.getString("RFID_no");
                                        }

                                        if (employeeJsonObject.has("employee_code") && !employeeJsonObject.isNull("employee_code")) {
                                            employee_code = employeeJsonObject.getString("employee_code");
                                        }
                                        if (employeeJsonObject.has("contractual_type_id") && !employeeJsonObject.isNull("contractual_type_id")) {
                                            contractual_type_id = employeeJsonObject.getString("contractual_type_id");
                                        }
                                        if (employeeJsonObject.has("contractor_id") && !employeeJsonObject.isNull("contractor_id")) {
                                            contractor_id = employeeJsonObject.getString("contractor_id");
                                        }

                                        //MyApplication.db.insertEmployeeDetails(RFID_no.trim(),employee_code,contractual_type_id,contractor_id,cell_id,systemDate,Constants.SYNCH_STATUS_N);

                                        String empAlready = S.checkEmployeeAlreadyEntryf(RFID_no.trim(),employee_code,systemDate);

                                        if(empAlready != null)
                                        {
                                            if(empAlready.equals("1"))
                                            {
                                                // Log.e("UPDATE>>","if");
                                                MyApplication.db.updateEmployeeDetails(RFID_no.trim(),employee_code,contractual_type_id,contractor_id,cell_id,systemDate);
                                            }
                                            else if(empAlready.equals("0"))
                                            {
                                                //Log.e("UPDATE>>","else");
                                                MyApplication.db.insertEmployeeDetails(RFID_no.trim(),employee_code,contractual_type_id,contractor_id,cell_id,systemDate,Constants.SYNCH_STATUS_N);

                                            }
                                        }

                                    }
                                }
                            }
                            for (int e = 0; e < cellJsonArray.length(); e++)
                            {

                                JSONArray eJsonArray = bookingJsonArray.getJSONArray(e);
                                if (eJsonArray.length() != 0)
                                {

                                    for (int ae = 0; ae < eJsonArray.length(); ae++)
                                    {
                                        JSONObject bookingJsonObject = eJsonArray.getJSONObject(ae);

                                        if (bookingJsonObject.has("id") && !bookingJsonObject.isNull("id")) {
                                            id = bookingJsonObject.getString("id");
                                        }

                                        if (bookingJsonObject.has("cell_id") && !bookingJsonObject.isNull("cell_id")) {
                                            cell_id = bookingJsonObject.getString("cell_id");
                                        }
                                        if (bookingJsonObject.has("contractual_type") && !bookingJsonObject.isNull("contractual_type")) {
                                            contractual_type = bookingJsonObject.getString("contractual_type");
                                        }
                                        if (bookingJsonObject.has("contractor_code") && !bookingJsonObject.isNull("contractor_code")) {
                                            contractor_code = bookingJsonObject.getString("contractor_code");
                                        }
                                        if (bookingJsonObject.has("target") && !bookingJsonObject.isNull("target")) {
                                            target = bookingJsonObject.getString("target");
                                        }
                                        if (bookingJsonObject.has("shift") && !bookingJsonObject.isNull("shift")) {
                                            shift = bookingJsonObject.getString("shift");
                                        }

                                        if (bookingJsonObject.has("tolerance") && !bookingJsonObject.isNull("tolerance")) {
                                            tollerence = bookingJsonObject.getString("tolerance");
                                        }

                                        if (resultStatus.equals("first"))
                                        {

                                            String statusw = S.checkAlreadyBooking(contractual_type, shift, contractor_code, cell_id, systemDate,id);

                                            if(statusw != null)
                                            {
                                                if (statusw.equals("1"))
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
                                                            "" + (Integer.valueOf((int) updatedd_target)));

                                                }
                                                else  if (statusw.equals("0"))
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
                                                            "" + (Integer.valueOf((int) updatedd_target)));
                                                }
                                            }


                                        }
                                        else
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
                                                    "" + (Integer.valueOf((int) updatedd_target)));

                                        }


                                    }

                                }
                            }

                            //}
                            //shift
                            for (int i = 0; i < shiftJsonArray.length(); i++) {

                                JSONObject shiftJsonObject = shiftJsonArray.getJSONObject(i);

                                if (shiftJsonObject.has("shift_name") && !shiftJsonObject.isNull("shift_name")) {
                                    shift_name = shiftJsonObject.getString("shift_name");
                                }
                                if (shiftJsonObject.has("shift_from") && !shiftJsonObject.isNull("shift_from")) {
                                    String shift_from_convert = shiftJsonObject.getString("shift_from");
                                    String timeFormatServer = T.convertHrsTosecond(shift_from_convert);

                                    Integer timeSec = Integer.valueOf(timeFormatServer) - 1800;

                                    String main_time_after_minus_onehrs = T.timeConvert(timeSec);

                                    shift_from = main_time_after_minus_onehrs;
                                }
                                if (shiftJsonObject.has("shift_to") && !shiftJsonObject.isNull("shift_to")) {
                                    shift_to = shiftJsonObject.getString("shift_to");

                                }

                                String siftAlready = S.checkShiftAlreadyEntry(shift_name, shift_from, shift_to, systemDate, Constants.SYNCH_STATUS_N);

                                if(siftAlready != null)
                                {
                                    if (siftAlready.equals("0"))
                                    {
                                        MyApplication.db.insertShiftDetails(shift_name, shift_from, shift_to, systemDate, Constants.SYNCH_STATUS_N);
                                    }
                                }


                            }

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            String MY_ACTION = "MY_ACTION";

            String[] dateData = T.getSystemDateTime().split(" ");

            SharedPreferences preferences = MyApplication.context.getSharedPreferences(Constants.NAVIGATION, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.SYSTEM_DATE, dateData[0]);
            editor.commit();

            Intent intent = new Intent();
            intent.setAction(MY_ACTION);
            intent.putExtra("DATAPASSED", "ok#"+"wifichanged");
            MyApplication.context.sendBroadcast(intent);


        }
    }

}
