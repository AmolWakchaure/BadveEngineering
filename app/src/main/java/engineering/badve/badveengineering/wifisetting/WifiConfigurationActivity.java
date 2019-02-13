package engineering.badve.badveengineering.wifisetting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.authentication.LoginActivity;
import engineering.badve.badveengineering.authentication.SplashActivity;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.dashboard.HomeActivity;
import engineering.badve.badveengineering.hardware.FT311UARTInterface;
import engineering.badve.badveengineering.interfaces.Constants;

public class WifiConfigurationActivity extends BaseActivity {

    private WifiManager mainWifiObj;
    private WifiScanReceiver wifiScanReceiver;
    private WifiReceiver wifiReceiver;
    private String TAG = WifiConfigurationActivity.class.getName();
    private RecyclerView recyclerView;
    private boolean isFistTime;


    private CoordinatorLayout coordinatorLayout;
    //------------------------------------------------------Hardware communication---------------------------------------------------------

    public static final int REQUEST_CODE = 1;
    //public static FT311UARTInterface uartInterface;
    //private SharedPreferences prefsHardware;
    //private SharedPreferences.Editor editorHardware;
    int baudRate; /* baud rate */
    byte stopBit; /* 1:1stop bits, 2:2 stop bits */
    byte dataBit; /* 8:8bit, 7: 7bit */
    byte parity; /* 0: none, 1: odd, 2: even, 3: mark, 4: space */
    byte flowControl; /* 0:none, 1: flow control(CTS,RTS) */
    private boolean btnflag = false, flag = false;

    public boolean bConfiged = false, imgFlag = false;
    public handler_thread handlerThread;
    byte[] readBuffer;
    byte status;
    int[] actualNumBytes;
    //public String act_string;
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    Button image;
    Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_configuration_activity_layout);

        getSupportActionBar().setTitle("Select Wifi Network");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //prefsHardware = this.getSharedPreferences(Constants.HARWARE_INTERFACE, Context.MODE_PRIVATE);
        //editorHardware = prefsHardware.edit();

        buttonNext = (Button)findViewById(R.id.buttonNext);

        congigureHardware();
        
        isFistTime = false;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            isFistTime = bundle.getBoolean("is_first_time");

            String firstTime = bundle.getString("FIRST");

            //  T.e("Status : "+firstTime);

            if(firstTime.equals("second"))
            {
                // T.e("Status : "+firstTime);
                buttonNext.setVisibility(View.GONE);
            }
        }

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!mainWifiObj.isWifiEnabled()){
            mainWifiObj.setWifiEnabled(true);
        }
        wifiScanReceiver = new WifiScanReceiver();
        mainWifiObj.startScan();

        wifiReceiver = new WifiReceiver();

    }

    private void congigureHardware() {

        try
        {
            image = (Button)findViewById(R.id.image) ;

            if(!bConfiged)
            {
                image.performClick();
            }

        /* allocate buffer */

            readBuffer = new byte[4096];

            actualNumBytes = new int[1];

        /* by default it is 9600 */
            baudRate = 9600;
		/* default is stop bit 1 */
            stopBit = 1;
		/* default data bit is 8 bit */
            dataBit = 8;
		/* default is none */
            parity = 0;
		/* default flow control is is none */
            flowControl = 0;

//            act_string = getIntent().getAction();
//
//            if (-1 != act_string.indexOf("android.intent.action.MAIN"))
//            {
//                restorePreference();
//            }
//            else if (-1 != act_string.indexOf("android.hardware.usb.action.USB_ACCESSORY_ATTACHED"))
//            {
//                cleanPreference();
//            }

            //uartInterface = new FT311UARTInterface(this, MyApplication.prefsHardware);


            //send first packet
            //startConnection();


            image.post(new Runnable() {
                @Override
                public void run() {
                    if (btnflag == false) {
                        //T.tET(HomeActivity.this,">>>"+"hii");

                        image.performClick();
                        btnflag = true;
                    }
                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (false == bConfiged) {
                        bConfiged = true;
                        MyApplication.uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
                        savePreference();

                        handlerThread = new handler_thread(handler);
                        handlerThread.start();

                    }
                }
            });

            //handlerThread = new handler_thread(handler);
            //handlerThread.start();


        }
        catch (Exception e)
        {
            T.tE(WifiConfigurationActivity.this,""+e);
        }
    }
    protected void savePreference()
    {
        if (true == bConfiged)
        {
            MyApplication.prefsHardware.edit().putString("configed", "TRUE").commit();
            MyApplication.prefsHardware.edit().putInt("baudRate", baudRate).commit();
            MyApplication.prefsHardware.edit().putInt("stopBit", stopBit).commit();
            MyApplication.prefsHardware.edit().putInt("dataBit", dataBit).commit();
            MyApplication.prefsHardware.edit().putInt("parity", parity).commit();
            MyApplication.prefsHardware.edit().putInt("flowControl", flowControl).commit();
        }
        else
        {
            MyApplication.prefsHardware.edit().putString("configed", "FALSE").commit();
        }
    }
    final Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            try
            {
                // T.t(HomeActivity.this,"call");

                startConnection();


                int readBufferPosition = 0;
                final byte delimiter = 10;

                for (int i = 0; i < readBuffer.length; i++)
                {
                    byte b = readBuffer[i];
                    if (b == delimiter)
                    {
                        byte[] encodedBytes = new byte[readBufferPosition];
                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                        final String rfidNumber;

                        rfidNumber = new String(encodedBytes, "US-ASCII").trim();




                        char firstChar = rfidNumber.charAt(0);
                        if(firstChar == '$')
                        {
                            String replaceRfidNumber = rfidNumber.replace("","$");



                        }
                        else
                        {


                        }


                       // readBufferPosition = 0;


                    }
                    else
                    {
                        readBuffer[readBufferPosition++] = b;
                    }
                }


            }
            catch (Exception e)
            {
                //T.tE(HomeActivity.this,""+e.getMessage());

                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiScanReceiver);
        unregisterReceiver(wifiReceiver);

        startConnection();
        if (2 == MyApplication.uartInterface.ResumeAccessory()) {
            cleanPreference();
            restorePreference();
        }
    }
    private void startConnection()
    {


        try {
            int chkNumBytes;
            byte[] chkStrSendBuffer = new byte[1];
            chkStrSendBuffer[0] = 37;

            chkNumBytes = chkStrSendBuffer.length;
            MyApplication.uartInterface.SendPacket(chkNumBytes, chkStrSendBuffer);
        }
        catch (Exception e)
        {

            T.t(WifiConfigurationActivity.this,"connection : "+e);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        registerReceiver(wifiReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        if(mainWifiObj != null && !mainWifiObj.isWifiEnabled()){
            mainWifiObj.setWifiEnabled(true);
        }

        startConnection();
        if (2 == MyApplication.uartInterface.ResumeAccessory()) {
            cleanPreference();
            restorePreference();
        }
    }

    private class handler_thread extends Thread {
        Handler mHandler;

        /* constructor */
        handler_thread(Handler h) {
            mHandler = h;
        }

        public void run() {
            Message msg;


            //startConnection();



            while (true) {
                try
                {
                    Thread.sleep(200);
                }
                catch (InterruptedException e)
                {

                }

                status = MyApplication.uartInterface.ReadData(4096, readBuffer, actualNumBytes);

                if (status == 0x00 && actualNumBytes[0] > 0) {
                    msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }

            }
        }
    }

    protected void restorePreference()
    {
        String key_name = MyApplication.prefsHardware.getString("configed", "");
        if (true == key_name.contains("TRUE"))
        {
            bConfiged = true;
        }
        else
        {
            bConfiged = false;
        }

        baudRate = MyApplication.prefsHardware.getInt("baudRate", 9600);
        stopBit = (byte) MyApplication.prefsHardware.getInt("stopBit", 1);
        dataBit = (byte) MyApplication.prefsHardware.getInt("dataBit", 8);
        parity = (byte) MyApplication.prefsHardware.getInt("parity", 0);
        flowControl = (byte) MyApplication.prefsHardware.getInt("flowControl", 0);
    }
    protected void cleanPreference()
    {
        SharedPreferences.Editor editor = MyApplication.prefsHardware.edit();
        editor.remove("configed");
        editor.remove("baudRate");
        editor.remove("stopBit");
        editor.remove("dataBit");
        editor.remove("parity");
        editor.remove("flowControl");
        editor.commit();
    }



    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            WifiRecycleViewAdapter mAdapter = new WifiRecycleViewAdapter(wifiScanList, onListItemClick);
            recyclerView.setAdapter(mAdapter);

        }
    }

    public class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();
            if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.d("WifiReceiver", "Have Wifi Connection");
                //Toast.makeText(WifiConfigurationActivity.this, "Wifi connected", Toast.LENGTH_SHORT).show();
                showSnackBar("Wifi connected");
                //finish();
            } else {
                Log.d("WifiReceiver", "Don't have Wifi Connection");
                //Toast.makeText(WifiConfigurationActivity.this, "Wifi disconnected", Toast.LENGTH_SHORT).show();
                showSnackBar("Wifi disconnected");
            }
        }
    };


    OnListItemClick onListItemClick = new OnListItemClick() {
        @Override
        public void onItemClick(ScanResult scanResult) {
            connectToWifi(scanResult);
        }
    };

    public interface OnListItemClick {

        void onItemClick(ScanResult scanResult);
    }


    private void finallyConnect(String networkPass, ScanResult scanResult) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", scanResult.SSID);


        String Capabilities =  scanResult.capabilities;
        if (Capabilities.contains("WPA2")) {
            //do something
            wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

            wifiConfig.status = WifiConfiguration.Status.ENABLED;
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        }
        else if (Capabilities.contains("WPA")) {
            //do something
            wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);
        }
        else if (Capabilities.contains("WEP")) {
            //do something
            wifiConfig.wepKeys[0] = "\"" + networkPass + "\"";
            wifiConfig.wepTxKeyIndex = 0;
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        }

        // remember id
        int netId = mainWifiObj.addNetwork(wifiConfig);
        mainWifiObj.disconnect();
        mainWifiObj.enableNetwork(netId, true);
        mainWifiObj.reconnect();
    }

    private void connectToWifi(final ScanResult scanResult) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.connect_wifi_dialog);
        dialog.setTitle("Connect to Network");
        TextView textSSID = (TextView) dialog.findViewById(R.id.textSSID1);

        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        final EditText pass = (EditText) dialog.findViewById(R.id.textPassword);
        textSSID.setText(scanResult.SSID);

        // if button is clicked, connect_wifi_dialog to the network;
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkPassword = pass.getText().toString();
                if(!checkPassword.isEmpty()) {
                    finallyConnect(checkPassword, scanResult);
                    dialog.dismiss();
                }else {
                    pass.setHint("Please Enter Password");
                }
            }
        });
        dialog.show();
    }

    public void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        /*// Changing action button text color
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout)snackbar.getView();
        layout.setPadding(0, 0, 0, 0);//set padding to 0
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);*/
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wifi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home)
        {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    public void openWifi(View view)
    {
        if(T.checkConnection(WifiConfigurationActivity.this))
        {


            Intent i = new Intent(WifiConfigurationActivity.this,LoginActivity.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
            finish();
        }
        else
        {
            T.t(WifiConfigurationActivity.this,"Network connection off");
        }

    }
}
