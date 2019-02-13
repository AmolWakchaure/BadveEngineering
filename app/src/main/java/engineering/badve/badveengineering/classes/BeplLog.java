package engineering.badve.badveengineering.classes;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.pojo.CellInfo;

/**
 * Created by sns003 on 02-Nov-17.
 */

public class BeplLog
{
    public static void passwordBackup(String newPassword)
    {

        String sBody = " Password : "+newPassword+" Date Time : "+T.getSystemDateTime();
        try
        {

            if(!sBody.equals("NA"))
            {
                File root = new File(Environment.getExternalStorageDirectory(), "BeplLog");
                if (!root.exists())
                {
                    root.mkdirs();
                }
                File gpxfile = new File(root, "password.txt");
                FileWriter writer = new FileWriter(gpxfile,true);

                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(sBody+"\n\n");
                bufferedWriter.close();

            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void generateServerLog(String location)
    {

        String sBody = returnCellIDSS() + " Date Time : "+T.getSystemDateTime()+"Location : "+location+" Status : Server not responding.";
        try
        {

            if(!sBody.equals("NA"))
            {
                File root = new File(Environment.getExternalStorageDirectory(), "BeplLog");
                if (!root.exists())
                {
                    root.mkdirs();
                }
                File gpxfile = new File(root, "serverLog.txt");
                FileWriter writer = new FileWriter(gpxfile,true);

                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(sBody+"\n\n");
                bufferedWriter.close();

            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void saveResponse(String response)
    {


        try
        {

            if(!response.equals("NA"))
            {
                File root = new File(Environment.getExternalStorageDirectory(), "BeplLog");
                if (!root.exists())
                {
                    root.mkdirs();
                }
                File gpxfile = new File(root, "apiAttendannceResponse.txt");
                FileWriter writer = new FileWriter(gpxfile,true);

                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(response+"\r\n----------------------------------------------------------------------------------------------------");
                bufferedWriter.close();

            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void storeJSON(String json)
    {

        String sBody = json;
        try
        {

            if(!sBody.equals("NA"))
            {
                File root = new File(Environment.getExternalStorageDirectory(), "BeplLog");
                if (!root.exists())
                {
                    root.mkdirs();
                }
                File gpxfile = new File(root, "jsonLog.txt");
                FileWriter writer = new FileWriter(gpxfile,true);

                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(sBody+"\n\n");
                bufferedWriter.close();

            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void deviceLog(String logData)
    {

        String sBody = logData;
        try
        {

            if(!sBody.equals("NA"))
            {
                File root = new File(Environment.getExternalStorageDirectory(), "BeplLog");
                if (!root.exists())
                {
                    root.mkdirs();
                }
                File gpxfile = new File(root, "deviceLog.txt");
                FileWriter writer = new FileWriter(gpxfile,true);

                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(sBody+"\n\n");
                bufferedWriter.close();

            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void storeerror(String error)
    {

        String sBody = error;
        try
        {

            if(!sBody.equals("NA"))
            {
                File root = new File(Environment.getExternalStorageDirectory(), "BeplLog");
                if (!root.exists())
                {
                    root.mkdirs();
                }
                File gpxfile = new File(root, "volleyErrorLog.txt");
                FileWriter writer = new FileWriter(gpxfile,true);

                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(sBody+"\n\n");
                bufferedWriter.close();

            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void generateWifilogLog(String connectDisconnectStatus)
    {

        String sBody = returnCellIDSS() + " Date Time : "+T.getSystemDateTime()+" Status : "+connectDisconnectStatus;
        try
        {

            if(!sBody.equals("NA"))
            {
                File root = new File(Environment.getExternalStorageDirectory(), "BeplLog");
                if (!root.exists())
                {
                    root.mkdirs();
                }
                File gpxfile = new File(root, "wifiLog.txt");
                FileWriter writer = new FileWriter(gpxfile,true);

                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(sBody+"\n\n");
                bufferedWriter.close();

               /* writer.append(sBody);
                writer.flush();
                writer.close();*/

            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String returnCellIDSS()
    {
        String cellNames = null;
        try
        {
            ArrayList<CellInfo> cell_id = S.getAllCellIDForJsonArray();

            if(cell_id.isEmpty())
            {
                cellNames = "NA";
            }
            else
            {
                StringBuffer stringBuffer = new StringBuffer();

                for(int i = 0; i < cell_id.size(); i++)
                {
                    CellInfo cellInfo = cell_id.get(i);
                    stringBuffer.append(""+cellInfo.getCell_name()+", ");
                }

                cellNames = "Cell Name : "+stringBuffer.toString();
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return cellNames;
    }
}
