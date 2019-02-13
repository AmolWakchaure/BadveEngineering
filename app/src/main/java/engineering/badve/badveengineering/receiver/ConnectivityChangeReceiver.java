package engineering.badve.badveengineering.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import engineering.badve.badveengineering.classes.BeplLog;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.dashboard.HomeActivity;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.synchdata.LoadDeployment;
import engineering.badve.badveengineering.synchdata.SynchData;

/**
 * Created by snsystem_amol on 29/06/2017.
 */

public class ConnectivityChangeReceiver extends BroadcastReceiver
{
    public final static String MY_ACTION = "MY_ACTION";

    @Override
    public void onReceive(final Context context, Intent intent)
    {
        //for synching local db
        if(T.checkConnection(context))
        {
            String [] systemDateArray = T.getSystemDateTime().split(" ");
            String systemDate = systemDateArray[0];
            //checkWifi(context);
            ArrayList<String> RFID_NUMBER_yesterday = S.selectRfidNumbers("0",T.displayYesterdasDate(-1));
            if(RFID_NUMBER_yesterday.isEmpty())
            {
                ArrayList<String> RFID_NUMBER_TODAY = S.selectRfidNumbers("0",systemDate);
                if(!RFID_NUMBER_TODAY.isEmpty())
                {
                    for(int i = 0; i < RFID_NUMBER_TODAY.size(); i++)
                    {
                        SynchData.synchronizeBarcodeData(context,RFID_NUMBER_TODAY.get(i),"0",systemDate);
                    }
                }
            }
            else
            {
                if(!RFID_NUMBER_yesterday.isEmpty())
                {
                    for(int i = 0; i < RFID_NUMBER_yesterday.size(); i++)
                    {
                        SynchData.synchronizeBarcodeData(context,RFID_NUMBER_yesterday.get(i),"0",T.displayYesterdasDate(-1));
                    }
                }
                ArrayList<String> RFID_NUMBER_TODAY = S.selectRfidNumbers("0",systemDate);
                if(!RFID_NUMBER_TODAY.isEmpty())
                {
                    for(int i = 0; i < RFID_NUMBER_TODAY.size(); i++)
                    {
                        SynchData.synchronizeBarcodeData(context,RFID_NUMBER_TODAY.get(i),"0",systemDate);
                    }
                }
            }
            LoadDeployment.loadDeploymentWifiOn("first",context);
        }
        else
        {
            //Log.e("Wifi Status","Disconnect");
        }


    }



}
