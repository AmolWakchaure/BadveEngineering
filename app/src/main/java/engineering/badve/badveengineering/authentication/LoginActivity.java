package engineering.badve.badveengineering.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.singh.daman.proprogressviews.DoubleArcProgress;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.activity.AddCellActivity;
import engineering.badve.badveengineering.activity.ChangePasswordActivity;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.classes.V;
import engineering.badve.badveengineering.classes.VolleyCallback;
import engineering.badve.badveengineering.classes.VolleyErrorCallback;
import engineering.badve.badveengineering.classes.VolleyResponseClass;
import engineering.badve.badveengineering.dashboard.HomeActivity;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.hardware.FT311UARTInterface;
import engineering.badve.badveengineering.interfaces.Constants;
import engineering.badve.badveengineering.pojo.CellInfo;
import engineering.badve.badveengineering.wifisetting.WifiConfigurationActivity;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.submitButton)
    Button submitButton;

//    @Bind(R.id.progressBarr)
//    DoubleArcProgress progressBarr;

    String tabID;
    @BindView(R.id.et_pass)
    EditText et_pass;

    @BindView(R.id.hideLinearLayoutLayOut)
    LinearLayout hideLinearLayoutLayOut;

    @BindView(R.id.plantCodeEditext)
    EditText plantCodeEditext;

    @BindView(R.id.cellIdSearchableSpinner)
    SearchableSpinner cellIdSearchableSpinner;

    @BindView(R.id.progressTextView)
    TextView progressTextView;

    @BindView(R.id.hideRelativeLayout)
    RelativeLayout hideRelativeLayout;

    private SharedPreferences preferences;


    private ArrayList<String> CELL_DETAILS = new ArrayList<>();
    //------------------------------------------------------Hardware communication---------------------------------------------------------
    public static final int REQUEST_CODE = 1;
    //public static FT311UARTInterface uartInterface;
    //private SharedPreferences prefsHardware;
   // private SharedPreferences.Editor editorHardware;
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


    SweetAlertDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        //prefsHardware = this.getSharedPreferences(Constants.HARWARE_INTERFACE, Context.MODE_PRIVATE);
        //editorHardware = prefsHardware.edit();
        congigureHardware();
        setClickListner();
        //set tab serial number
       // tabID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID + "");
        //tabIdButton.setText("Tab ID: "+tabID);

        /*//send tab id and application type
        if(T.checkConnection(LoginActivity.this))
        {
            sendTabIdAnApplicationType();
           // getCellId();
        }
        else
        {
            T.tW(LoginActivity.this,getString(R.string.network));
        }*/

    }
    private void setClickListner()
    {


        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(!V.validateSpinner(cellIdSearchableSpinner,"Warning ! select cell details"))
                {
                    return;
                }
                MyApplication.editor.commit();
                MyApplication.editor.putString(Constants.PLANT_CODE,plantCodeEditext.getText().toString());
                MyApplication.editor.commit();

                String [] cellDetailsData = cellIdSearchableSpinner.getSelectedItem().toString().split("\n");

                MyApplication.db.insertCellIds(cellDetailsData[0],cellDetailsData[1]);


                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!V.validateEmptyField(LoginActivity.this,plantCodeEditext,"Warning ! plant code"))
                {
                    return;
                }

                if(T.checkConnection(LoginActivity.this))
                {
                    getCellId();

                }
                else
                {
                    T.tW(LoginActivity.this,getString(R.string.network));
                }



            }
        });


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

            //uartInterface = new FT311UARTInterface(this, prefsHardware);


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
            T.tE(LoginActivity.this,""+e);
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

                        //readBufferPosition = 0;

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
    protected void onPause()
    {
        super.onPause();

        startConnection();
        if (2 == MyApplication.uartInterface.ResumeAccessory())
        {
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

            T.t(LoginActivity.this,"connection : "+e);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    private void setCellId(ArrayList<String> cellId)
    {

        ArrayAdapter<String> pallet_a_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview,R.id.spinnerTxtView, cellId);
        pallet_a_adapter.setDropDownViewResource(R.layout.spinner_textview);
        cellIdSearchableSpinner.setAdapter(pallet_a_adapter);
        cellIdSearchableSpinner.setTitle("Select cell ID");



        /*ArrayAdapter<String> pallet_a_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item,cellId);
        //pallet_a_adapter.setDropDownViewResource(R.layout.spinner_textview);
        cellIdSearchableSpinner.setAdapter(pallet_a_adapter);*/



    /*    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,cellId);
        //Getting the instance of AutoCompleteTextView
        cellIdButton.setThreshold(1);//will start working from first character
        cellIdButton.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        cellIdButton.setTextColor(Color.BLACK);*/


    }



    private void sendTabIdAnApplicationType()
    {

        String[] parameters =
                {
                        "deviceid"+"#"+tabID,
                        "type"+"#"+"attendance"
                };
        VolleyResponseClass.checkAuthentication(
                new VolleyCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {


                        parseResponsess(result);


                    }
                },
                new VolleyErrorCallback()
                {
                    @Override
                    public void onError(VolleyError result)
                    {

                        handleError(result,"first");

                        //sendTabIdAnApplicationType();
                    }
                },
                getResources().getString(R.string.saveDeviceId),
                parameters,
                "Registering tab...",LoginActivity.this);


    }
    private void handleError(VolleyError error,String callType)
    {
        try
        {

            if(error instanceof TimeoutError || error instanceof NoConnectionError)
            {

                displayError("TimeoutError/NoConnectionError","Server not responding or no connection.",callType);


            }
            else if(error instanceof AuthFailureError)
            {

                displayError("AuthFailureError","Remote server returns (401) Unauthorized?.",callType);

            }
            else if(error instanceof ServerError)
            {


                displayError("ServerError","Wrong webservice call or wrong webservice url.",callType);

            }
            else if (error instanceof NetworkError)
            {
                displayError("NetworkError","you doesn't have a data connection and wi-fi Connection.",callType);

            }
            else if(error instanceof ParseError)
            {

                displayError("NetworkError","Incorrect json response.",callType);
            }



        }
        catch (Exception e)
        {

        }

    }
    private void displayError(String title, String error, final String callType)
    {

        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setConfirmText("Try again")
                .setContentText(error)
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {
                        sDialog.dismissWithAnimation();
                        if(callType.equals("first"))
                        {
                            sendTabIdAnApplicationType();
                        }
                        else if(callType.equals("second"))
                        {
                            followLogin();
                        }
                    }
                })
                .show();

    }
    private void getCellId()
    {

        final String plant_codeJsonArray = T.returnPlantCode(plantCodeEditext.getText().toString());

        String[] parameters =
                {
                        "plant_code" + "#" + plant_codeJsonArray
                };
        VolleyResponseClass.getBookingData(
                new VolleyCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {
                        parseCell(result);
                    }
                },
                new VolleyErrorCallback()
                {
                    @Override
                    public void onError(VolleyError result)
                    {


                        //handleError(result);
                    }
                },
                LoginActivity.this,
                getResources().getString(R.string.webUrl) + "" + getResources().getString(R.string.getAllCell),
                parameters,
                "Fetching cell...",
                "first");





    }
    private void parseCell(String response)
    {

        String cell_id = Constants.NA;
        String cell_name = Constants.NA;
        ArrayList<String> CELL_ID = new ArrayList<>();
        try
        {

            if (response != null || response.length() > 0)
            {

                JSONObject jsonResponse = new JSONObject(response);
                String successString = jsonResponse.getString("status");
                if (successString.equals("1"))
                {
                    JSONArray jsonArray = jsonResponse.getJSONArray("cell");

                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonResponsef = jsonArray.getJSONObject(i);

                        if(jsonResponsef.has("cell_id") && !jsonResponsef.isNull("cell_id"))
                        {
                            cell_id = jsonResponsef.getString("cell_id");
                        }
                        if(jsonResponsef.has("cell_name") && !jsonResponsef.isNull("cell_name"))
                        {
                            cell_name = jsonResponsef.getString("cell_name");
                        }

                        CELL_ID.add(cell_id+"\n"+cell_name);


                    }
                    submitButton.setVisibility(View.GONE);
                    hideLinearLayoutLayOut.setVisibility(View.VISIBLE);
                    setCellId(CELL_ID);

                }
                else if(successString.equals("0"))
                {
                    T.tE(LoginActivity.this,"Warning ! Invalid Plant Code.");

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void parseResponsess(String response)
    {

        try
        {

            if (response != null || response.length() > 0)
            {

                JSONObject jsonResponse = new JSONObject(response);
                String successString = jsonResponse.getString("status");
                if (successString.equals("1"))
                {
                    T.tI(LoginActivity.this,"Success ! Tab Id Successfully save.");
                    //BeplLog.d(">>", "resif>>>" + successString);
                }
                else
                {
                    //T.tI(LoginActivity.this,"Error ! Problem to save Tab Id.");
                    //BeplLog.d(">>", "reselse>>>" + successString);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void followLogin() {

        String[] parameters =
                {
                        "deviceid"+"#"+tabID,
                        "license_key"+"#"+et_pass.getText().toString()

                };
        VolleyResponseClass.checkAuthentication(
                new VolleyCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {
                        parseResponsessRegister(result);
                    }
                },
                new VolleyErrorCallback()
                {
                    @Override
                    public void onError(VolleyError result)
                    {
                        handleError(result,"second");

                        //followLogin();
                    }
                },
                getResources().getString(R.string.registerDevice),
                parameters,
                "Authenticating...",LoginActivity.this);


    }
    private void parseResponsessRegister(String response) {


        try {

            if (response != null || response.length() > 0) {

                JSONObject jsonResponse = new JSONObject(response);
                String successString = jsonResponse.getString("status");
                if (successString.equals("1"))
                {


                    /*Intent i = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                    i.putExtra(Constants.PASSWORD_STATUS,"first");
                    i.putExtra(Constants.OLD_PASSWORD,cellIdEditext.getText().toString());
                    startActivity(i);
                    finish();
*/

                }
                else
                {

                    T.tE(LoginActivity.this,"Warning ! incorrect input credentials");

                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
