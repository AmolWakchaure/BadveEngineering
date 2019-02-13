package engineering.badve.badveengineering.synchdata;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.classes.AttendanceInfoaa;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.classes.VolleyCallback;
import engineering.badve.badveengineering.classes.VolleyErrorCallback;
import engineering.badve.badveengineering.classes.VolleyResponseClass;
import engineering.badve.badveengineering.database.S;


/**
 * Created by snsystem_amol on 16-Apr-17.
 */

public class SynchData
{
    //synch attendance data
    public static void synchronizeBarcodeData(final Context context,
                                              String rfidNumber,
                                              String synchStatus,
                                              final String todaysDate)
    {


                ArrayList<AttendanceInfoaa> ATTENDACE_INFO = S.getAttendanceDetailsOffline(rfidNumber,synchStatus,todaysDate);

                if(!ATTENDACE_INFO.isEmpty())
                {

                    if(T.checkConnection(context))
                    {
                        for(int i = 0; i < ATTENDACE_INFO.size(); i++)
                        {

                            AttendanceInfoaa attendanceInfo = ATTENDACE_INFO.get(i);
                            JSONArray inAttendJsonArray = null;
                            try
                            {
                                inAttendJsonArray = new JSONArray();

                                JSONObject inAttendJsonObject = new JSONObject();

                                inAttendJsonObject.put("id",attendanceInfo.getId());
                                inAttendJsonObject.put("shift_name",attendanceInfo.getShift_name());
                                inAttendJsonObject.put("out_time",attendanceInfo.getOut_time());
                                inAttendJsonObject.put("in_time",attendanceInfo.getIn_time());
                                inAttendJsonObject.put("barcode_number",attendanceInfo.getBarcode_number());
                                inAttendJsonObject.put("rfid_number",attendanceInfo.getRfid_number());

                                inAttendJsonArray.put(inAttendJsonObject);

                            }
                            catch (Exception e)
                            {

                            }

                            synchDataOffline(context,inAttendJsonArray.toString(),attendanceInfo.getRfid_number(),todaysDate);

                        }
                    }


                }




    }

    private static void synchDataOffline(Context context, String inAttendJsonArray, final String rfid, final String date)
    {


        String[] parameters =
                {
                        "attendance" + "#" + inAttendJsonArray.toString()
                        //"cell_id"+"#"+"1109C00016"
                };


        VolleyResponseClass.synchOffline(
                new VolleyCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {
                        Log.e("DATAAAAAA","Result : "+result);
                        //parseResponsessRegister(result);
                        parseResponsessSendAttendance(result,rfid,date);
                    }
                },
                new VolleyErrorCallback()
                {
                    @Override
                    public void onError(VolleyError result)
                    {

                    }
                },
                context,
                context.getResources().getString(R.string.webUrl) + "" +context.getResources().getString(R.string.synch_attendance),
                parameters);

    }
    public static  void parseResponsessSendAttendance(String response,String rfid,String datee)
    {
        //System.out.println(response);
        try
        {
            // T.e(""+response);
            JSONObject jsonObject = new JSONObject(response);
            String successStatus = jsonObject.getString("status");
            if(successStatus.equals("1"))
            {
                //check in out entry
                JSONArray arr = jsonObject.getJSONArray("data");
                System.out.println(arr.length());
                for(int i=0; i<arr.length();i++)
                {
                    String status = S.checkInOutEntry(MyApplication.db,rfid,datee);
                    if(status != null)
                    {
                        if(status.equals("1"))
                        {
                            MyApplication.db.updateSynchAttendanceStatus(rfid,"1",datee);
                        }
                        else
                        {
                            MyApplication.db.deleteAttendanceEntry(rfid,datee,"0");

                        }

                    }

                }


            }
            else if(successStatus.equals("2"))
            {

                //MyApplication.db.deleteAttendanceEntry(rfid,T.getSystemDate());
            }
            else if(successStatus.equals("0"))
            {
                // T.tE(context,"Server problem or fail something");
            }
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            // T.tE(context,"Error Occured [Server's JSON response might be invalid]!");
            //Toast.makeText(context, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
