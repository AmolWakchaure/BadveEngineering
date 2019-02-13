package engineering.badve.badveengineering.dashboard;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.activity.AddCellActivity;
import engineering.badve.badveengineering.adapter.TypeInTargetOutAdapter;
import engineering.badve.badveengineering.classes.AttendanceInfoaa;
import engineering.badve.badveengineering.classes.BeplLog;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.classes.V;
import engineering.badve.badveengineering.classes.VolleyCallback;
import engineering.badve.badveengineering.classes.VolleyErrorCallback;
import engineering.badve.badveengineering.classes.VolleyResponseClass;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.hardware.FT311UARTInterface;
import engineering.badve.badveengineering.interfaces.Constants;
import engineering.badve.badveengineering.pojo.CellInfo;
import engineering.badve.badveengineering.pojo.TypeInOutTargetInfo;
import engineering.badve.badveengineering.reports.MenuActivity;
import engineering.badve.badveengineering.services.FloatingFaceBubbleService;
import engineering.badve.badveengineering.services.LoadDailyData;
import engineering.badve.badveengineering.services.ResetScreens;
import engineering.badve.badveengineering.wifisetting.WifiConfigurationActivity;

public class HomeActivity extends AppCompatActivity {


    private String bookingResult,bookingStatus;

    @BindView(R.id.testError)
    TextView testError;

    @BindView(R.id.txt_date)
    TextView txt_date;

    @BindView(R.id.txt_cell_name)
    TextView txt_cell_name;

    @BindView(R.id.inTargetTextView)
    TextView inTargetTextView;

    @BindView(R.id.inCountTextView)
    TextView inCountTextView;

    @BindView(R.id.designationTypeId)
    RecyclerView designationTypeId;

    @BindView(R.id.inCountLinearLayout)
    LinearLayout inCountLinearLayout;

    @BindView(R.id.outCountLinearLayout)
    LinearLayout outCountLinearLayout;

    @BindView(R.id.wifiNetworkName)
    TextView wifiNetworkName;

    @BindView(R.id.txt_out_count)
    TextView txt_out_count;

    @BindView(R.id.txt_shift_name)
    TextView txt_shift_name;

    @BindView(R.id.iotButton)
    Button iotButton;

    @BindView(R.id.addCellsButton)
    Button addCellsButton;

    @BindView(R.id.hideRelativeLayout)
    RelativeLayout hideRelativeLayout;

    @BindView(R.id.progressTextViewEnglish)
    TextView progressTextViewEnglish;

    @BindView(R.id.progressTextViewHindi)
    TextView progressTextViewHindi;

    @BindView(R.id.rfidNumberDemo)
    TextView rfidNumberDemo;

    @BindView(R.id.successFailImageView)
    ImageView successFailImageView;

    @BindView(R.id.hideRelativeLayoutGate)
    RelativeLayout hideRelativeLayoutGate;


    @BindView(R.id.progressTextViewGate)
    TextView progressTextViewGate;


    @BindView(R.id.wifiImageView)
    ImageView wifiImageView;

    @BindView(R.id.versionNameTextView)
    TextView versionNameTextView;

    @BindView(R.id.batteryLevelTextView)
    TextView batteryLevelTextView;

    @BindView(R.id.rfidEditext)
    AutoCompleteTextView rfidEditext;
    //@Bind(R.id.shiftNameLayout)
    //LinearLayout shiftNameLayout;
    @BindView(R.id.inTargetLinearLayout)
    LinearLayout inTargetLinearLayout;

    private MyReceiver myReceiver;

    ArrayList<String> SHIFT_NAMES = new ArrayList<>();

    private TypeInTargetOutAdapter
            typeInTargetOutAdapter;

    PowerManager.WakeLock screenLock;

    ArrayList<TypeInOutTargetInfo> BOOKING_DATA_TYPE = new ArrayList<>();

    //  private MyReceiver myReceiver;

    //------------------------------------------------------Hardware communication---------------------------------------------------------

    public static final int REQUEST_CODE = 1;
    public static FT311UARTInterface uartInterface;
    private SharedPreferences prefsHardware;
    private SharedPreferences.Editor editorHardware;
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
    //SweetAlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        //for set app screen full
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set version details of app
        //T.e("Version : " + T.returnVersionName());

        //get ftdi global object session from application class
        prefsHardware = MyApplication.prefsHardware;
        editorHardware = MyApplication.editorHardware;

        //initialise butternife
        ButterKnife.bind(this);

        //set version details of app
        //versionNameTextView.setText("v" + T.returnVersionName());

        //function for initialise widgets
        initialise();

        //checkWifi();
        checkWifiInthread();

        //configure ftdi
        congigureHardware();

        //set watch timer on screen
        setDateTime();

        //set running shift on screen
        manageTimesAndShifts();

        //display deployment details
        setScreenData();

        //function for fetch deployment from server
        checkAvailableBooking("first");



    }

    private void initialise()
    {

        iotButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showChangeLangDialog("iot");

             /*   final SweetAlertDialog progressDialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.PROGRESS_TYPE);


                    progressDialog.getProgressHelper().setBarColor(Color.parseColor("#3F51B5"));
                    progressDialog.setTitleText("Loading...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();*/

            }
        });

        addCellsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showChangeLangDialog("cell");
            }
        });

        designationTypeId.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        typeInTargetOutAdapter = new TypeInTargetOutAdapter(HomeActivity.this);
        designationTypeId.setAdapter(typeInTargetOutAdapter);

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LoadDailyData.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        Intent i = new Intent(HomeActivity.this, LoadDailyData.class);
        startService(i);

        myReceiver = new MyReceiver();
        IntentFilter intentFilterS = new IntentFilter();
        intentFilterS.addAction(ResetScreens.MY_ACTION);
        registerReceiver(myReceiver, intentFilterS);

        Intent iA = new Intent(HomeActivity.this, ResetScreens.class);
        startService(iA);

        Intent iff = new Intent(HomeActivity.this, FloatingFaceBubbleService.class);
        startService(iff);

    }
    @SuppressLint("NewApi")
    private void checkWifiInthread()
    {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable()
        {

            public void run()
            {
                //upgrade battery level ui
                /*int batLevel = MyApplication.batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                if(batLevel <= 15)
                {
                    batteryLevelTextView.setText("Battery : "+batLevel+"%");
                    batteryLevelTextView.setTextColor(getResources().getColor(R.color.colorRed));
                }
                else
                {
                    batteryLevelTextView.setText("Battery : "+batLevel+"%");
                    batteryLevelTextView.setTextColor(getResources().getColor(R.color.colorBlack));
                }*/


                if (T.checkConnection(HomeActivity.this))
                {

                    String wifiName = T.getCurrentSsid(HomeActivity.this);
                    int numberOfLevels = 5;

                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);

                    if (level == 1) {
                        wifiImageView.setImageResource(R.drawable.ic_wifi_one);
                        wifiNetworkName.setText(wifiName);
                    } else if (level == 2) {
                        wifiImageView.setImageResource(R.drawable.ic_wifi_two);
                        wifiNetworkName.setText(wifiName);
                    } else if (level == 3) {
                        wifiImageView.setImageResource(R.drawable.ic_wifi_three);
                        wifiNetworkName.setText(wifiName);
                    } else if (level == 4) {
                        wifiImageView.setImageResource(R.drawable.ic_wifi_four);
                        wifiNetworkName.setText(wifiName);
                    }

                    //BeplLog.e("WIFI",""+level);


                }
                else
                {
                    wifiImageView.setImageResource(R.drawable.ic_signal_wifi_off_black_48dp);
                    wifiNetworkName.setText("No Wifi");
                }
                handler.postDelayed(this, 50);  //for interval...
            }
        };
        handler.postDelayed(runnable, 2000); //for initial delay..
    }

    private void congigureHardware() {

        try {
            image = (Button) findViewById(R.id.image);

            if (!bConfiged) {
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

            uartInterface = MyApplication.uartInterface;


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
                        uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
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
            T.tE(HomeActivity.this, "" + e);
        }
    }
    private void setDateTime() {

        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                txt_date.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date()));
                someHandler.postDelayed(this, 1000);
            }
        }, 10);
    }
    private void manageTimesAndShifts()
    {

        try
        {
            //get shift details with from time and to time
            ArrayList<String> fromTimeTotimeShiftName = S.getfromTimeTotimeShiftName(MyApplication.db);

            //for separate shift
            String[] shiftname = new String[fromTimeTotimeShiftName.size()];
            //for separate shift from time
            String[] timessFrom = new String[fromTimeTotimeShiftName.size()];
            //for separate shift to time
            String[] timessTo = new String[fromTimeTotimeShiftName.size()];

            //loop for store shif from time and to time into separate array
            for (int i = 0; i < fromTimeTotimeShiftName.size(); i++)
            {
                String[] fromTimeTotimeShiftNameData = fromTimeTotimeShiftName.get(i).split("#");

                shiftname[i] = fromTimeTotimeShiftNameData[0];
                timessFrom[i] = fromTimeTotimeShiftNameData[1];
                timessTo[i] = fromTimeTotimeShiftNameData[2];
            }

            //for get running shift
            for (int i = 0; i < timessFrom.length; i++)
            {

                String shiftTimesFrom = convertHrsTosecond(timessFrom[i]);
                String shiftTimesTo = convertHrsTosecond(timessTo[i]);
                String getSystemTime = T.getSystemTimeTwentyFourHrs();
                String systemTime = convertHrsTosecond(getSystemTime);

                if (Integer.valueOf(systemTime) >= Integer.valueOf(shiftTimesFrom) && Integer.valueOf(systemTime) <= Integer.valueOf(shiftTimesTo))
                {
                    SHIFT_NAMES.add(shiftname[i]);
                }
                else if (Integer.valueOf(systemTime) >= Integer.valueOf(shiftTimesFrom) && Integer.valueOf(systemTime) >= Integer.valueOf(shiftTimesTo))
                {
                    //T.t(HomeActivity.this,""+shiftname[i]);
                    SHIFT_NAMES.add(shiftname[i]);
                }
            }

            if (SHIFT_NAMES.isEmpty()) {
                //if shift not found
                T.tI(HomeActivity.this, "Shift details not found");
            }
            else
            {
                //if shift name is found
                txt_shift_name.setText(SHIFT_NAMES.get(SHIFT_NAMES.size() - 1));
                Log.e("Shift_name", "" + SHIFT_NAMES.get(SHIFT_NAMES.size() - 1));
            }
        } catch (Exception e) {

        }


    }

    private void setScreenData()
    {
        try
        {

            String[] systemDateArray = T.getSystemDateTime().split(" ");
            String systemDate = systemDateArray[0];

            String screenDataCheckStatus = S.getAllTargetTotal(MyApplication.db, systemDate);

            //T.t(HomeActivity.this,""+screenDataCheckStatus);
            //T.e("4.00 : " + screenDataCheckStatus);
            if (!(screenDataCheckStatus.equals("NA") || screenDataCheckStatus.equals("null#null#null")))
            {
                String[] screenData = screenDataCheckStatus.split("#");
                String typeTargetInOutData = S.getAllBookingDetailsNotShiftWise(MyApplication.db, systemDate);
                String cellNAme = returnCellNames();
                BOOKING_DATA_TYPE = T.returnDataParsing(typeTargetInOutData);

                txt_cell_name.setText("" + cellNAme.substring(0, cellNAme.length() - 1));

                inTargetTextView.setText(screenData[0]);
                inCountTextView.setText(screenData[1]);
                txt_out_count.setText(screenData[2]);
            }
            if(!BOOKING_DATA_TYPE.isEmpty())
            {
                typeInTargetOutAdapter.setBookingData(BOOKING_DATA_TYPE);
                typeInTargetOutAdapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            T.e("setScreenData :" + e);
        }

    }
    //function for fetch deployment from server
    private void checkAvailableBooking(String status)
    {

        if (status.equals("first"))
        {
            if (T.checkConnection(HomeActivity.this))
            {
                SharedPreferences preferences = getSharedPreferences(Constants.NAVIGATION, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.commit();

                String dateSystem = preferences.getString(Constants.SYSTEM_DATE, "0");
                String[] dateData = T.getSystemDateTime().split(" ");

                Log.e("DAAAA", "data " + dateData[0]);
                Log.e("DAAAA", "System " + dateSystem);

                //boolean rr = S.checkShift();
                if (!dateData[0].equals(dateSystem))
                {

                    //check booking data availe in booking table
                    String dataStatus = S.checkDataAvailable();
                    if(dataStatus.equals("0"))
                    {
                        inCountLinearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                        outCountLinearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                        getBooking(status);
                    }

                }

            }
            else
            {

                T.tET(HomeActivity.this, "Network connection off");
            }

        }
        else if (status.equals("second"))
        {
            getBooking(status);
        }

    }
    //function for if we want to test app without reader
    private void setStaticRFID()
    {
        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, Constants.language);
        rfidEditext.setThreshold(1);//will start working from first character
        rfidEditext.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        rfidEditext.setTextColor(Color.RED);
    }

    private void getBooking(final String status)
    {

        ArrayList<CellInfo> cell_id = S.getAllCellIDForJsonArray();

        final String cellIdJsonArray = T.arrayListToJsonArrayCellId(cell_id);

        Log.e("cellIdJsonArray",""+cellIdJsonArray);
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
                       // parseResponsess(result, status);
                        bookingResult = result;
                        bookingStatus = status;
                        new ParseBookingResponse().execute();
                    }
                },
                new VolleyErrorCallback()
                {
                    @Override
                    public void onError(VolleyError result)
                    {
                        handleError(result);
                    }
                },
                HomeActivity.this,
                getResources().getString(R.string.webUrl) + "" + getResources().getString(R.string.load_data),
                parameters,
                "Fetching Booking...",
                status);


    }


    class ParseBookingResponse extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {


            try
            {
                //shift
                String shift_name = Constants.NA;
                String shift_from = Constants.NA;
                String shift_to = Constants.NA;

                String cell_id = Constants.NA;


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



                String[] systemDateArray = T.getSystemDateTime().split(" ");
                String systemDate = systemDateArray[0];

                if (bookingResult != null || bookingResult.length() > 0)
                {
                    Object json = new JSONTokener(bookingResult).nextValue();
                    if (json instanceof JSONObject)
                    {
                        JSONObject dataJsonObject = new JSONObject(bookingResult);
                        String success = dataJsonObject.getString("success");

                        if (success.equals("1"))
                        {

                            JSONArray cellJsonArray = dataJsonObject.getJSONArray("cell");
                            JSONArray employeeJsonArray = dataJsonObject.getJSONArray("employee");
                            JSONArray bookingJsonArray = dataJsonObject.getJSONArray("booking");
                            JSONArray shiftJsonArray = dataJsonObject.getJSONArray("shift");

                            if (bookingStatus.equals("first"))
                            {
                                //delete prev data
                                String todayDate = T.getSystemDate();
                                String yesterdayDate = T.displayYesterdasDate(-1);
                                MyApplication.db.clearTable(todayDate, yesterdayDate);

                                SharedPreferences preferences = getSharedPreferences(Constants.NAVIGATION, 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(Constants.SYSTEM_DATE, todayDate);
                                editor.commit();


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
                                        String alreadyEmpoyee = S.checkEmployeeAlreadyEntryf(RFID_no.trim(),employee_code,systemDate);

                                        if(alreadyEmpoyee != null)
                                        {
                                            if(alreadyEmpoyee.equals("1"))
                                            {
                                                // Log.e("UPDATE>>","if");
                                                MyApplication.db.updateEmployeeDetails(RFID_no.trim(),employee_code,contractual_type_id,contractor_id,cell_id,systemDate);
                                            }
                                            else if(alreadyEmpoyee.equals("0"))
                                            {
                                                //Log.e("UPDATE>>","else");
                                                MyApplication.db.insertEmployeeDetails(RFID_no.trim(),employee_code,contractual_type_id,contractor_id,cell_id,systemDate,Constants.SYNCH_STATUS_N);

                                            }
                                        }
                                    }
                                }
                            }
                            for (int e = 0; e < cellJsonArray.length(); e++) {

                                JSONArray eJsonArray = bookingJsonArray.getJSONArray(e);

                                if (eJsonArray.length() != 0) {

                                    //Integer targetNewww = 0;

                                    for (int ae = 0; ae < eJsonArray.length(); ae++) {


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

                                        if (bookingStatus.equals("first"))
                                        {

                                            String statusw = S.checkAlreadyBooking(contractual_type, shift, contractor_code, cell_id, systemDate,id);

                                            if(statusw != null)
                                            {
                                                if (statusw.equals("1"))
                                                {
                                                    //T.t(HomeActivity.this,"2 :"+systemDate);

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

                                            Log.e("updatedd_target",""+updatedd_target);

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

                                if (shiftJsonObject.has("shift_name") && !shiftJsonObject.isNull("shift_name"))
                                {
                                    shift_name = shiftJsonObject.getString("shift_name");
                                }
                                if (shiftJsonObject.has("shift_from") && !shiftJsonObject.isNull("shift_from"))
                                {
                                    String shift_from_convert = shiftJsonObject.getString("shift_from");
                                    String timeFormatServer = T.convertHrsTosecond(shift_from_convert);

                                    Integer timeSec = Integer.valueOf(timeFormatServer) - 1800;

                                    String main_time_after_minus_onehrs = T.timeConvert(timeSec);

                                    shift_from = main_time_after_minus_onehrs;
                                }
                                if (shiftJsonObject.has("shift_to") && !shiftJsonObject.isNull("shift_to"))
                                {
                                    shift_to = shiftJsonObject.getString("shift_to");

                                }
                                //MyApplication.db.insertShiftDetails(shift_name, shift_from, shift_to, systemDate, Constants.SYNCH_STATUS_N);
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

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {

            String[] dateData = T.getSystemDateTime().split(" ");
            SharedPreferences preferences = getSharedPreferences(Constants.NAVIGATION, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.SYSTEM_DATE, dateData[0]);
            editor.commit();
            manageTimesAndShifts();
            setScreenData();



        }
    }

    private void handleError(VolleyError error)
    {

        try
        {

            if (error instanceof TimeoutError || error instanceof NoConnectionError)
            {

                T.tET(HomeActivity.this, "Warning ! Server not responding or no connection.");
                //displayError("TimeoutError/NoConnectionError","Server not responding or no connection.",status);


            }
            else if (error instanceof AuthFailureError)
            {
                T.tET(HomeActivity.this, "Warning ! Remote server returns (401) Unauthorized?.");
                //displayError("AuthFailureError","Remote server returns (401) Unauthorized?.",status);

            } else if (error instanceof ServerError) {

                T.tET(HomeActivity.this, "Warning ! Wrong webservice call or wrong webservice url.");

                //displayError("ServerError","Wrong webservice call or wrong webservice url.",status);

            } else if (error instanceof NetworkError) {
                T.tET(HomeActivity.this, "Warning !  You doesn't have a data connection and wi-fi Connection.");
                //displayError("NetworkError","you doesn't have a data connection and wi-fi Connection.",status);

            } else if (error instanceof ParseError) {
                T.tET(HomeActivity.this, "Warning ! Incorrect json response.");

                //displayError("NetworkError","Incorrect json response.",status);
            }


        } catch (Exception e) {

        }

    }
    public boolean checkEmptyArray(JSONArray jsonArray, String title, String confirmText, String message) {
        if (jsonArray.length() == 0) {
           // T.t(HomeActivity.this, message);
            //requestFocus(editText);
            return false;
        } else {
            return true;
        }


    }

    public boolean checkEmptyArrayyy(JSONArray jsonArray) {
        if (jsonArray.length() == 0) {

            //requestFocus(editText);
            return false;
        } else {
            return true;
        }
    }



    public void deleteData(View view)
    {
        showChangeLangDialogDevelopere("delete");

    }

    public void synchData(View view)
    {

        showChangeLangDialogDevelopere("loadData");


    }

    private class handler_thread extends Thread {
        Handler mHandler;

        /* constructor */
        handler_thread(Handler h) {
            mHandler = h;
        }

        public void run()
        {
            Message msg;


            //startConnection();

//            BeplLog.deviceLog("Ele Device String : $ Date Time : "+T.getSystemDateTime());
            while (true) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {

                }

                status = uartInterface.ReadData(4096, readBuffer, actualNumBytes);

                if (status == 0x00 && actualNumBytes[0] > 0) {
                    msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }

            }
        }
    }

    final Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            try
            {

                startConnection();
                //BeplLog.deviceLog("Ele Device String : $ Date Time : "+T.getSystemDateTime());
                //checkWifi();

                int readBufferPosition = 0;
                final byte delimiter = 10;

                //Thread.sleep(200);
                for (int i = 0; i < readBuffer.length; i++)
                {
                    byte b = readBuffer[i];
                    if (b == delimiter)
                    {
                        byte[] encodedBytes = new byte[readBufferPosition];
                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                        final String rfidNumber;


                        rfidNumber = new String(encodedBytes, "US-ASCII").trim();
                        String RFID_NUMBER = rfidNumber.replace("$","");

                        //on screen here
                        onScreen();
                        //set text here
                        rfidNumberDemo.setText(""+RFID_NUMBER);
                        followMatchingProcess(RFID_NUMBER);

                        readBufferPosition = 0;
                        //break;
                    }
                    else
                    {
                        readBuffer[readBufferPosition++] = b;
                    }
                }

            } catch (Exception e) {
                T.tE(HomeActivity.this, "Card not read properly");
                e.printStackTrace();
            }

        }
    };
    private void onScreen()
    {
        screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();
        screenLock.release();
    }


    public void sendAttendance(View view)
    {

       // MyApplication.db.updateOutAttendanceEntry();
        if (!V.validateEmptyField(HomeActivity.this, rfidEditext, "Enter RFID number")) {
            return;
        }

        followMatchingProcess(rfidEditext.getText().toString());
        rfidEditext.setText("");

    }

    private void checkWifi()
    {
        if (T.checkConnection(HomeActivity.this))
        {

            String wifiName = T.getCurrentSsid(HomeActivity.this);
            int numberOfLevels = 5;

            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);

            if (level == 1) {
                wifiImageView.setImageResource(R.drawable.ic_wifi_one);
                wifiNetworkName.setText(wifiName);
            } else if (level == 2) {
                wifiImageView.setImageResource(R.drawable.ic_wifi_two);
                wifiNetworkName.setText(wifiName);
            } else if (level == 3) {
                wifiImageView.setImageResource(R.drawable.ic_wifi_three);
                wifiNetworkName.setText(wifiName);
            } else if (level == 4) {
                wifiImageView.setImageResource(R.drawable.ic_wifi_four);
                wifiNetworkName.setText(wifiName);
            }

            //BeplLog.e("WIFI",""+level);


        }
        else
        {
            wifiImageView.setImageResource(R.drawable.ic_signal_wifi_off_black_48dp);
            wifiNetworkName.setText("No Wifi");
        }
    }

    private void followMatchingProcess(String rfidNumber)
    {


        //T.t(HomeActivity.this,rfidNumber);
        try
        {
            String read_date_time = T.getSystemDateTime();
            String[] sysDate = read_date_time.split(" ");
            String systemDate = sysDate[0];


            String status = S.checkBarcodeAvailableIAttendance(rfidNumber, T.displayYesterdasDate(-1));

            if (status.equals("1"))
            {
                //get barcode number for yesterdays entry
                String barcodeNumberCellIdData = S.getBarcodeNumber(rfidNumber, T.displayYesterdasDate(-1));

                if (barcodeNumberCellIdData == null || barcodeNumberCellIdData.equals("NA"))
                {
                    manageTimesAndShifts();
                    setScreenData();
                    displayDialogMessageSuccess(R.drawable.ic_fail, "RFID details not found.", getResources().getString(R.string.noentry_hindi));
                }
                else
                {
                    String[] barcodeNumberCellId = barcodeNumberCellIdData.split("#");
                    String barcodeNumber = barcodeNumberCellId[0];//1109000269
                    String cellId = barcodeNumberCellId[1];//1109AL01

                    String[] returnBarcodeEmpDesignationType = S.returnBarcodeType(MyApplication.db, rfidNumber, cellId, T.displayYesterdasDate(-1)).split("#");//all right

                    if (returnBarcodeEmpDesignationType.length > 0)
                    {
                        String DESIGNATION = returnBarcodeEmpDesignationType[0];//SEMI-SKILL
                        String CONTRACTOR_CODE = returnBarcodeEmpDesignationType[1];//1109_40886

                        //get shift name
                        String shiftName = S.getShiftName(rfidNumber, T.displayYesterdasDate(-1));//A

                        if (shiftName == null || shiftName.equals("NA"))
                        {
                            manageTimesAndShifts();
                            setScreenData();
                            displayDialogMessageSuccess(R.drawable.ic_fail, "Shift name not found", getResources().getString(R.string.noentry_hindi));
                        }
                        else
                        {

                            if (T.checkConnection(HomeActivity.this))
                            {

                                //MyApplication.db.increaseOutCount(DESIGNATION, shiftName, CONTRACTOR_CODE, cellId, T.displayYesterdasDate(-1));
                                MyApplication.db.updateYesterdayOutEntry(rfidNumber, read_date_time, "0", T.displayYesterdasDate(-1));
                                //synch all json array always

                                sendInAttendanceHere(
                                        "Your OUT attendance is registered.",
                                        getResources().getString(R.string.in_hindi),
                                        R.color.colorGreen,
                                        "0",
                                        "out",
                                        T.displayYesterdasDate(-1),
                                        rfidNumber,
                                        DESIGNATION,
                                        CONTRACTOR_CODE,
                                        cellId,
                                        shiftName,
                                        "yesterday",
                                        barcodeNumber
                                );


                            }
                            else
                            {

                                String outEntryAlready = S.outAttendanceAlready(rfidNumber, T.displayYesterdasDate(-1));
                                if (outEntryAlready.equals("1"))
                                {
                                    displayDialogMessageSuccess(R.drawable.ic_fail, "Your OUT attendance is already registered.", getResources().getString(R.string.noentry_hindi));
                                }
                                else if (outEntryAlready.equals("0"))
                                {
                                    MyApplication.db.updateYesterdayOutEntry(rfidNumber, read_date_time, "0", T.displayYesterdasDate(-1));
                                    MyApplication.db.increaseOutCount(DESIGNATION, shiftName, CONTRACTOR_CODE, cellId, T.displayYesterdasDate(-1));
                                    manageTimesAndShifts();
                                    setScreenData();
                                    displayDialogMessageSuccess(R.drawable.ic_success, "Your OUT attendance is registered.", getResources().getString(R.string.in_hindi));
                                }


                            }

                        }

                    }
                    else
                    {
                        manageTimesAndShifts();
                        setScreenData();
                        displayDialogMessageSuccess(R.drawable.ic_fail, "Employee deployment not available.", getResources().getString(R.string.noentry_hindi));
                    }

                }
            }
            else if(status.equals("0"))
            {
                //if(S.checkBarcodeAvailable(MyApplication.db,rfidNumber,cellId,systemDate))
                String barcodeNumberCellIdData = S.getBarcodeNumber(rfidNumber, systemDate);

                if (barcodeNumberCellIdData == null || barcodeNumberCellIdData.equals("NA"))
                {
                    manageTimesAndShifts();
                    setScreenData();
                    displayDialogMessageSuccess(R.drawable.ic_fail, "RFID details not found.", getResources().getString(R.string.noentry_hindi));
                }
                else
                {
                    String[] barcodeNumberCellId = barcodeNumberCellIdData.split("#");
                    String barcodeNumber = barcodeNumberCellId[0];
                    String cellId = barcodeNumberCellId[1];

                    String sstaus = S.checkBarcodeAvailable(MyApplication.db, rfidNumber, cellId, systemDate);

                    if (sstaus.equals("1"))//barcode avail in emp table
                    {
                        //get barcode number
                        //check in out entry if entry avail in attendance table so consider this is out entry else in entry
                        if (S.cehckInoutEntry(MyApplication.db, rfidNumber, systemDate).equals("1"))
                        {
                            //first we need to check barcode wise type is full or not
                            //check barcode number  type e.g fitter or welder
                            String[] returnBarcodeEmpDesignationType = S.returnBarcodeType(MyApplication.db, rfidNumber, cellId, systemDate).split("#");//all right

                            if (returnBarcodeEmpDesignationType.length > 0)
                            {
                                String DESIGNATION = returnBarcodeEmpDesignationType[0];
                                String CONTRACTOR_CODE = returnBarcodeEmpDesignationType[1];
                                //get shift name
                                String shiftName = S.getShiftName(rfidNumber, systemDate);

                                if (shiftName == null || shiftName.equals("NA"))
                                {
                                    manageTimesAndShifts();
                                    setScreenData();
                                    displayDialogMessageSuccess(R.drawable.ic_fail, "Employee deployment not available.", getResources().getString(R.string.noentry_hindi));
                                }
                                else
                                {
                                    //if(S.checkDesignationAvailForBooking(MyApplication.db,returnBarcodeEmpDesignationType,SHIFT_NAMES.get(SHIFT_NAMES.size() - 1)))
                                    String designationStatus = S.checkDesignationAvailForBooking(MyApplication.db, DESIGNATION, CONTRACTOR_CODE, cellId, systemDate);
                                    if (designationStatus.equals("1"))
                                    {

                                        String inTime = S.getInEntryTime(MyApplication.db, rfidNumber, systemDate);
                                        String enterTime = T.getSystemTimeTewntyFour();

                                        String inTimeSeconds = convertHrsTosecond(inTime);
                                        String enterTimeSeconds = convertHrsTosecond(enterTime);

                                        int compareTime = Integer.valueOf(inTimeSeconds) + 1800;//30 min


                                        if (compareTime < Integer.valueOf(enterTimeSeconds))
                                        {
                                            if (T.checkConnection(HomeActivity.this))
                                            {

                                                MyApplication.db.updateOutEntry(rfidNumber, barcodeNumber, read_date_time, shiftName, "0", systemDate);

                                                sendInAttendanceHere(
                                                        "Your OUT attendance is registered.",
                                                        getResources().getString(R.string.in_hindi),
                                                        R.color.colorGreen,
                                                        "0",
                                                        "out",
                                                        systemDate,
                                                        rfidNumber,
                                                        DESIGNATION,
                                                        CONTRACTOR_CODE,
                                                        cellId,
                                                        shiftName,
                                                        "today",
                                                        barcodeNumber
                                                );

                                            }
                                            else
                                            {

                                                String outEntryAlready = S.outAttendanceAlready(rfidNumber, systemDate);
                                                if (outEntryAlready.equals("1"))
                                                {
                                                    displayDialogMessageSuccess(R.drawable.ic_fail, "Your OUT attendance is already registered.", getResources().getString(R.string.noentry_hindi));
                                                }
                                                else if (outEntryAlready.equals("0"))
                                                {
                                                    //increse out count
                                                    MyApplication.db.increaseOutCount(DESIGNATION, shiftName, CONTRACTOR_CODE, cellId, systemDate);
                                                    //update out entry
                                                    MyApplication.db.updateOutEntry(rfidNumber, barcodeNumber, read_date_time, shiftName, "0", systemDate);
                                                    manageTimesAndShifts();
                                                    setScreenData();
                                                    displayDialogMessageSuccess(R.drawable.ic_success, "Your OUT attendance is registered.", getResources().getString(R.string.in_hindi));
                                                    //T.tSS(HomeActivity.this,""+"Success.. Your OUT attendance is registered.\n\n"+getResources().getString(R.string.in_hindi));
                                                }

                                            }

                                        }
                                        else
                                        {
                                            manageTimesAndShifts();
                                            setScreenData();
                                            displayDialogMessageSuccess(R.drawable.ic_fail, "Within 30 minutes out entry is not possible.", getResources().getString(R.string.thirty_min));
                                            //T.tET(HomeActivity.this,""+"Within 30 minutes out entry is not possible.\n\n"+getResources().getString(R.string.thirty_min));
                                        }

                                    }
                                    else if(designationStatus.equals("0"))
                                    {
                                        manageTimesAndShifts();
                                        setScreenData();
                                        // T.t(MyApplication.context,"one");
                                        //displayDialogMessageSuccess(R.drawable.ic_fail, "" + DESIGNATION + " This designation type is not allocated for todays shift or contractor.", getResources().getString(R.string.noentry_hindi));
                                        displayDialogMessageSuccess(R.drawable.ic_fail, "No " + DESIGNATION + " deployment found for contractor : "+CONTRACTOR_CODE, getResources().getString(R.string.noentry_hindi));
                                        //"No " + DESIGNATION + " deployment found for contractor : "+CONTRACTOR_CODE

                                    }
                                }

                            }
                            else
                            {
                                manageTimesAndShifts();
                                setScreenData();
                                //deployment not available.
                                displayDialogMessageSuccess(R.drawable.ic_fail, "Employee deployment not available.", getResources().getString(R.string.noentry_hindi));
                            }


                        }
                        else if(S.cehckInoutEntry(MyApplication.db, rfidNumber, systemDate).equals("0"))
                        {

                            //consider in entry
                            //check barcode number  type e.g fitter or welder and contractor_code
                            String[] returnBarcodeEmpDesignationType = S.returnBarcodeType(MyApplication.db, rfidNumber, cellId, systemDate).split("#");

                            if (returnBarcodeEmpDesignationType.length > 0)
                            {
                                String DESIGNATION = returnBarcodeEmpDesignationType[0];
                                String CONTRACTOR_CODE = returnBarcodeEmpDesignationType[1];
                                //check already out
                                String outTime = S.getOutEntryTime(MyApplication.db, rfidNumber, systemDate);

                                if (outTime == null || outTime.equals("NA"))
                                {
                                    //we need to check this barcode type designation allocated to shift or not

                                    String desStatus = S.checkDesignationAvailForBooking(
                                            MyApplication.db,
                                            DESIGNATION,
                                            CONTRACTOR_CODE,
                                            cellId,
                                            systemDate);
                                    if (desStatus.equals("1"))
                                    {
                                        //get fromtime totime with shiftname

                                        if (SHIFT_NAMES.isEmpty())
                                        {

                                            manageTimesAndShifts();
                                            setScreenData();
                                            T.tET(HomeActivity.this, "Sorry... Shift ended.");

                                        }
                                        else
                                        {
                                            String targetCountOldd = S.getTargetCount(
                                                    DESIGNATION,
                                                    SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                                    cellId,
                                                    systemDate);

                                            if (targetCountOldd == null || targetCountOldd.equals("NA"))
                                            {
                                                manageTimesAndShifts();
                                                setScreenData();
                                                displayDialogMessageSuccess(R.drawable.ic_fail, "Deployment not available.", getResources().getString(R.string.noentry_hindi));
                                            }
                                            else
                                            {
                                                Integer targetCountOld = Integer.valueOf(targetCountOldd);
                                                String inCountOldd = S.getInCount(
                                                        DESIGNATION,
                                                        SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                                        cellId,
                                                        systemDate);

                                                if (inCountOldd == null || inCountOldd.equals("NA"))
                                                {
                                                    manageTimesAndShifts();
                                                    setScreenData();
                                                    displayDialogMessageSuccess(R.drawable.ic_fail, " Deployment not available.", getResources().getString(R.string.noentry_hindi));
                                                }
                                                else
                                                {
                                                    Integer inCountOld = Integer.valueOf(inCountOldd);//incount

                                                    if (inCountOld < targetCountOld)
                                                    {
                                                        //(with tollerence)
                                                        //get target  count
                                                        Integer targetCount = Integer.valueOf(
                                                                S.getTargetCountTollerence(
                                                                        DESIGNATION,
                                                                        CONTRACTOR_CODE,
                                                                        SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                                                        cellId,
                                                                        systemDate));

                                                        //get in count
                                                        Integer inCount = Integer.valueOf(
                                                                S.getInCountTollerence(
                                                                        DESIGNATION,
                                                                        CONTRACTOR_CODE,
                                                                        SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                                                        cellId,
                                                                        systemDate));//incount


                                                        if (inCount < targetCount)
                                                        {
                                                            if (T.checkConnection(HomeActivity.this))
                                                            {
                                                                //check interlock here
                                                                checkInterLockHere(
                                                                        DESIGNATION,
                                                                        rfidNumber,
                                                                        barcodeNumber,
                                                                        read_date_time,
                                                                        CONTRACTOR_CODE,
                                                                        cellId,
                                                                        systemDate);

                                                            }
                                                            else
                                                            {
                                                                //finally increment in count in booking table
                                                                MyApplication.db.increaseInCount(
                                                                        DESIGNATION,
                                                                        SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                                                        CONTRACTOR_CODE,
                                                                        cellId,
                                                                        systemDate);
                                                                //insert in entry in attendance table
                                                                MyApplication.db.insertInEntry(
                                                                        rfidNumber,
                                                                        barcodeNumber,
                                                                        read_date_time,
                                                                        "0",
                                                                        SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                                                        "0",
                                                                        systemDate);

                                                                manageTimesAndShifts();
                                                                setScreenData();
                                                                displayDialogMessageSuccess(R.drawable.ic_success, "Your IN attendance is registered.", getResources().getString(R.string.in_hindi));
                                                            }

                                                        }
                                                        else
                                                        {
                                                            manageTimesAndShifts();
                                                            setScreenData();
                                                            displayDialogMessageSuccess(R.drawable.ic_fail, "" + DESIGNATION + " Type employee attendance is full (Tolerence target) Contractor : "+CONTRACTOR_CODE, getResources().getString(R.string.noentry_hindi));
                                                            //T.tET(HomeActivity.this,""+returnBarcodeEmpDesignationType + "\n Type employee attendance is full\n\n"+getResources().getString(R.string.noentry_hindi));
                                                        }
                                                    }
                                                    else
                                                    {
                                                        manageTimesAndShifts();
                                                        setScreenData();
                                                        displayDialogMessageSuccess(R.drawable.ic_fail, "" + DESIGNATION + " Type employee attendance is full (Main target) Contractor : "+CONTRACTOR_CODE, getResources().getString(R.string.noentry_hindi));
                                                    }
                                                }

                                            }

                                        }

                                    }
                                    else if (desStatus.equals("0"))
                                    {
                                        manageTimesAndShifts();
                                        setScreenData();
                                        //displayDialogMessageSuccess(R.drawable.ic_fail, "" + DESIGNATION + " This designation type is not allocated for todays shift or contractor.", getResources().getString(R.string.noentry_hindi));
                                        displayDialogMessageSuccess(R.drawable.ic_fail, "No " + DESIGNATION + " deployment found for contractor : "+CONTRACTOR_CODE, getResources().getString(R.string.noentry_hindi));

                                    }
                                }
                                else
                                {
                                    String enterTime = T.getSystemTimeTewntyFour();
                                    String inTimeSeconds = convertHrsTosecond(outTime);
                                    String enterTimeSeconds = convertHrsTosecond(enterTime);

                                    int compareTime = Integer.valueOf(inTimeSeconds) + 600;//10 min
                                    if (compareTime < Integer.valueOf(enterTimeSeconds))
                                    {
                                        //get target  count  (old code here without tollerence)
                                        //(with tollerence)
                                        //get target  count
                                        Integer targetCountOld = Integer.valueOf(
                                                S.getTargetCount(
                                                        DESIGNATION,
                                                        SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                                        cellId,
                                                        systemDate));

                                        Integer inCountOld = Integer.valueOf(
                                                S.getInCount(
                                                        DESIGNATION,
                                                        SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                                        cellId,
                                                        systemDate));//incount

                                        if (inCountOld < targetCountOld)
                                        {
                                            //(with tollerence)
                                            //get target  count
                                            Integer targetCount = Integer.valueOf(
                                                    S.getTargetCountTollerence(
                                                            DESIGNATION,
                                                            CONTRACTOR_CODE,
                                                            SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                                            cellId,
                                                            systemDate));

                                            //get in count
                                            Integer inCount = Integer.valueOf(
                                                    S.getInCountTollerence(
                                                            DESIGNATION,
                                                            CONTRACTOR_CODE,
                                                            SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                                            cellId,
                                                            systemDate));//incount


                                            if (inCount < targetCount)
                                            {
                                                if (T.checkConnection(HomeActivity.this))
                                                {
                                                    //check interlock here
                                                    checkInterLockHere(
                                                            DESIGNATION,
                                                            rfidNumber,
                                                            barcodeNumber,
                                                            read_date_time,
                                                            CONTRACTOR_CODE,
                                                            cellId,
                                                            systemDate);

                                                }
                                                else
                                                {
                                                    //finally increment in count in booking table
                                                    MyApplication.db.increaseInCount(
                                                            DESIGNATION,
                                                            SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                                            CONTRACTOR_CODE,
                                                            cellId,
                                                            systemDate);

                                                    //insert in entry in attendance table
                                                    MyApplication.db.insertInEntry(
                                                            rfidNumber,
                                                            barcodeNumber,
                                                            read_date_time,
                                                            "0",
                                                            SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                                            "0",
                                                            systemDate);

                                                    manageTimesAndShifts();
                                                    setScreenData();

                                                    displayDialogMessageSuccess(R.drawable.ic_success, "Your IN attendance is registered.", getResources().getString(R.string.in_hindi));
                                                    // T.tSS(HomeActivity.this,"Success... Your IN attendance is registered.\n\n"+getResources().getString(R.string.in_hindi));

                                                }

                                            }
                                            else
                                            {
                                                manageTimesAndShifts();
                                                setScreenData();
                                                displayDialogMessageSuccess(R.drawable.ic_fail, "" + DESIGNATION + " Type employee attendance is full (Tolerence target) Contractor : "+CONTRACTOR_CODE, getResources().getString(R.string.noentry_hindi));
                                                //T.tET(HomeActivity.this,""+returnBarcodeEmpDesignationType + "\n Type employee attendance is full\n\n"+getResources().getString(R.string.noentry_hindi));
                                            }
                                        }
                                        else
                                        {

                                            manageTimesAndShifts();
                                            setScreenData();
                                            displayDialogMessageSuccess(R.drawable.ic_fail, "" + DESIGNATION + " Type employee attendance is full (Main target) Contractor : "+CONTRACTOR_CODE, getResources().getString(R.string.noentry_hindi));
                                        }
                                    }
                                    else
                                    {
                                        manageTimesAndShifts();
                                        setScreenData();
                                        displayDialogMessageSuccess(R.drawable.ic_success, "In attendance is not possible when done out punch within 10 min.", getResources().getString(R.string.noentry_hindi));
                                    }
                                }
                            }
                            else
                            {
                                manageTimesAndShifts();
                                setScreenData();
                                displayDialogMessageSuccess(R.drawable.ic_success, "Employee type allocation not found", getResources().getString(R.string.noentry_hindi));
                            }

                        }
                    }
                    else if(sstaus.equals("0"))
                    {
                        manageTimesAndShifts();
                        setScreenData();
                        displayDialogMessageSuccess(R.drawable.ic_fail, "You do not belongs to this cell.", getResources().getString(R.string.noentry_hindi));//T.tET(HomeActivity.this,""+"You are not belongs to this cell\n"+getResources().getString(R.string.noentry_hindi));
                    }
                }

            }


        }
        catch (Exception e)
        {
            T.e("" + e);

        }

    }
    private void checkInterLockHere(final String DESIGNATION,
                                    final String rfidNumber,
                                    final String barcodeNumber,
                                    final String read_date_time,
                                    final String CONTRACTOR_CODE,
                                    final String cellId,
                                    final String systemDate) {

        final String inputJsonArray = T.arrayListToJsonArray(rfidNumber);


        String[] parameters =
                {
                        "emp_data" + "#" + inputJsonArray

                };
        VolleyResponseClass.checkAuthentication(
                new VolleyCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {

                       // Log.e("RESULTINT",""+result);
                        /*BeplLog.storeJSON(
                                " Webservice Name : "+getResources().getString(R.string.empInterlock)+
                                        " emp_data : "+inputJsonArray+
                                        " checkInterLockHere() : "+result+
                                        " DateTime : "+T.getSystemDateTime());*/

                        parseResponsesInterLock(result,
                                DESIGNATION,
                                barcodeNumber,
                                read_date_time,
                                rfidNumber,
                                CONTRACTOR_CODE,
                                cellId,
                                systemDate);
                    }
                },
                new VolleyErrorCallback()
                {
                    @Override
                    public void onError(VolleyError result)
                    {
                       /* BeplLog.storeerror(
                                " Webservice Name : "+getResources().getString(R.string.empInterlock)+
                                        " emp_data : "+inputJsonArray+
                                        " checkInterLockHere() : "+result+
                                        " DateTime : "+T.getSystemDateTime());*/
                        T.tE(HomeActivity.this, "Connection time out try again"+result);

                    }
                },
                getResources().getString(R.string.webUrl) + "" + getResources().getString(R.string.empInterlock),
                parameters,
                "Checking Gate...",HomeActivity.this);


    }

    private void parseResponsesInterLock(String result,
                                         String designation,
                                         String barcodeNumber,
                                         String read_date_time,
                                         String rfidNumber,
                                         String CONTRACTOR_CODE,
                                         String cellId,
                                         String systemDate) {

        try
        {

            if (result.length() > 0 || result != null)
            {
                JSONObject jsonObject = new JSONObject(result);

                String status = jsonObject.getString("status");

                if (status.equals("1"))
                {
                    //finally increment in count in booking table
                    //insert in entry in attendance table
                    MyApplication.db.insertInEntry(rfidNumber, barcodeNumber, read_date_time, "0", SHIFT_NAMES.get(SHIFT_NAMES.size() - 1), "1", systemDate);

                    String inCount = S.getInCountHere(cellId,SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),designation,CONTRACTOR_CODE,systemDate);

                    if(!inCount.equals("NA"))
                    {
                        //getIncount
                        MyApplication.db.updateInCount(designation, SHIFT_NAMES.get(SHIFT_NAMES.size() - 1), CONTRACTOR_CODE, cellId, systemDate,inCount);
                        manageTimesAndShifts();
                        setScreenData();
                        displayDialogMessageSuccess(R.drawable.ic_success, "Your IN attendance is registered.", getResources().getString(R.string.in_hindi));

                        sendInAttendanceHere(
                                "Your IN attendance is registered.",
                                getResources().getString(R.string.in_hindi),
                                R.color.colorGreen,
                                "1",
                                "in",
                                systemDate,
                                rfidNumber,
                                designation,
                                CONTRACTOR_CODE,
                                cellId,
                                SHIFT_NAMES.get(SHIFT_NAMES.size() - 1),
                                "today",
                                barcodeNumber
                        );
                    }


                }
                //already out
                else if(status.equals("3"))
                {
                    displayDialogMessageSuccess(R.drawable.ic_fail, "Out attendance is already registerd.", getResources().getString(R.string.noentry_hindi));
                }
                else if (status.equals("0"))
                {
                    displayDialogMessageSuccess(R.drawable.ic_fail, "Attendance mandatory on first gate.", getResources().getString(R.string.firstGate));
                }

            }
            else
            {
                T.tE(HomeActivity.this, "0 or null json found");
            }

        }
        catch (Exception e)
        {
            T.tE(HomeActivity.this, "Exeption:" + e);
            e.printStackTrace();
        }
    }
    private void sendInAttendanceHere(final String messageEnglish,
                                      final String messageHindi,
                                      final int color,
                                      final String synchStatus,
                                      final String inOutStatus,
                                      final String systemDate,
                                      final String rfidNumber,
                                      final String DESIGNATION,
                                      final String CONTRACTOR_CODE,
                                      final String cellId,
                                      final String shiftName,
                                      final String todayYesterdayStatus,
                                      final String barcodeNumber)
    {




        ArrayList<AttendanceInfoaa> ATTENDACE_INFO = S.getAttendanceDetailsOffline(rfidNumber,synchStatus,systemDate);

        if(!ATTENDACE_INFO.isEmpty())
        {

            if(T.checkConnection(HomeActivity.this))
            {
                for(int i = 0; i < ATTENDACE_INFO.size(); i++)
                {

                    AttendanceInfoaa attendanceInfo = ATTENDACE_INFO.get(i);

                    try
                    {
                        JSONArray inAttendJsonArray  = new JSONArray();

                        JSONObject inAttendJsonObject = new JSONObject();

                        inAttendJsonObject.put("id",attendanceInfo.getId());
                        inAttendJsonObject.put("shift_name",attendanceInfo.getShift_name());
                        inAttendJsonObject.put("out_time",attendanceInfo.getOut_time());
                        inAttendJsonObject.put("in_time",attendanceInfo.getIn_time());
                        inAttendJsonObject.put("barcode_number",attendanceInfo.getBarcode_number());
                        inAttendJsonObject.put("rfid_number",attendanceInfo.getRfid_number());

                        inAttendJsonArray.put(inAttendJsonObject);

                        sendInAttendanceNew(
                                inAttendJsonArray.toString(),
                                inOutStatus,
                                systemDate,
                                messageEnglish,
                                messageHindi,
                                rfidNumber,
                                DESIGNATION,
                                CONTRACTOR_CODE,
                                cellId,
                                shiftName,
                                todayYesterdayStatus,
                                barcodeNumber);

                    }
                    catch (Exception e)
                    {

                    }



                }
            }

        }

    }

    private void sendInAttendanceNew(String inAttendJsonArray,
                                     final String inOutStatus,
                                     final String systemDate,
                                     final String messageEnglish,
                                     final String messageHindi,
                                     final String rfidNumber,
                                     final String designation,
                                     final String contractor_code,
                                     final String cellId,
                                     final String shiftName,
                                     final String todayYesterdayStatus,
                                     final String barcodeNumber)
    {


        String[] parameters =
                {
                        "attendance" + "#" + inAttendJsonArray.toString()
                        //"cell_id"+"#"+"1109C00016"
                };


        VolleyResponseClass.sendInAttendance(
                new VolleyCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {
                        Log.e("DATAAAAAA","Result : "+result);
                        //parseResponsessRegister(result);
                        parseResponsessSendAttendance(
                                result,
                                inOutStatus,
                                systemDate,
                                messageEnglish,
                                messageHindi,
                                rfidNumber,
                                designation,
                                contractor_code,
                                cellId,
                                shiftName,
                                todayYesterdayStatus,
                                barcodeNumber);
                    }
                },
                new VolleyErrorCallback()
                {
                    @Override
                    public void onError(VolleyError result)
                    {
                        handleError(result);

                        //followLogin();
                    }
                },
                getResources().getString(R.string.webUrl) + "" +getResources().getString(R.string.synch_attendance),
                parameters,
                HomeActivity.this);

    }


    private void displayDialogMessageSuccess(
            int iconImage,
            String englishMessage,
            String hindiMessage
    )
    {
        Runnable mRunnable;
        Handler mHandler = new Handler();

        hideRelativeLayout.setVisibility(View.VISIBLE);

        successFailImageView.setImageResource(iconImage);
        progressTextViewEnglish.setText(englishMessage);
        progressTextViewHindi.setText(hindiMessage);

        mRunnable = new Runnable()
        {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                hideRelativeLayout.setVisibility(View.GONE); // when the task active then close the dialog
                progressTextViewEnglish.setText("");
                progressTextViewHindi.setText("");
            }
        };
        mHandler.postDelayed(mRunnable, 1500);
    }



    private void parseResponsessSendAttendance(
            String result,
            String inOutStatus,
            String systemDate,
            String messageEnglish,
            String messageHindi,
            String rfidNumber,
            String DESIGNATION,
            String CONTRACTOR_CODE,
            String cellId,
            String shiftName,
            String todayYesterdayStatus,
            String barcodeNumber)
    {
        try
        {

            //Log.e("sdfsdfgsdf",""+result);

            if (result != null || result.length() > 0)
            {
                Object json = new JSONTokener(result).nextValue();
                if (json instanceof JSONObject)
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result);
                        String successStatus = jsonObject.getString("status");

                        if (successStatus.equals("1"))
                        {
                            if (inOutStatus.equals("in"))
                            {



                            }
                            else if (inOutStatus.equals("out"))
                            {
                                JSONArray outCountJsonArray = jsonObject.getJSONArray("out_count");
                                String outCount = outCountJsonArray.get(0).toString();

                                if(outCount != null || outCount.length() > 0)
                                {
                                    if(todayYesterdayStatus.equals("today"))
                                    {
                                        //increse out count
                                        MyApplication.db.updateOutCount(DESIGNATION, shiftName, CONTRACTOR_CODE, cellId, systemDate,outCount);
                                        MyApplication.db.deleteAttendanceEntry(rfidNumber,systemDate);
                                    }
                                    else if(todayYesterdayStatus.equals("yesterday"))
                                    {
                                        MyApplication.db.updateOutCount(DESIGNATION, shiftName, CONTRACTOR_CODE, cellId, T.displayYesterdasDate(-1),outCount);
                                        MyApplication.db.deleteAttendanceEntry(rfidNumber,T.displayYesterdasDate(-1));
                                    }
                                    manageTimesAndShifts();
                                    setScreenData();
                                    displayDialogMessageSuccess(R.drawable.ic_success, messageEnglish, messageHindi);
                                }


                            }

                        }
                        else if (successStatus.equals("2"))
                        {
                            /*if (inOutStatus.equals("in"))
                            {

                                JSONArray arr = jsonObject.getJSONArray("data");
                                System.out.println(arr.length());
                                for (int i = 0; i < arr.length(); i++)
                                {
                                    MyApplication.db.deleteAttendanceEntry(arr.get(i).toString());
                                }
                                manageTimesAndShifts();
                                setScreenData();
                                displayDialogMessageSuccess(R.drawable.ic_success, messageEnglish, messageHindi);

                            }
                            else if (inOutStatus.equals("out"))
                            {
                                if(todayYesterdayStatus.equals("today"))
                                {
                                    MyApplication.db.deleteAttendanceEntry(rfidNumber,systemDate);
                                }
                                else if(todayYesterdayStatus.equals("yesterday"))
                                {
                                    MyApplication.db.deleteAttendanceEntry(rfidNumber,T.displayYesterdasDate(-1));
                                }

                                manageTimesAndShifts();
                                setScreenData();
                                displayDialogMessageSuccess(R.drawable.ic_success, messageEnglish, messageHindi);
                            }*/

                        }
                        else if (successStatus.equals("0"))
                        {
                            T.tE(HomeActivity.this, "Already attendance registered");
                        }
                    }
                    catch (JSONException e)
                    {

                        // TODO Auto-generated catch block
                        T.t(HomeActivity.this, "Result : " + result);
                       //T.tE(HomeActivity.this, "Invalid JSON" + e);
                        e.printStackTrace();
                    }
                }
                else
                {
                    T.t(HomeActivity.this, "incorect json");
                    //String sBody = "\r\n todayYesterdayStatus : "+todayYesterdayStatus+"\r\n rfidNumber : "+rfidNumber+"\r\n DESIGNATION : "+DESIGNATION+"\r\n CONTRACTOR_CODE : "+CONTRACTOR_CODE+"\r\n shiftName : "+shiftName+"\r\n rfidNumber : "+rfidNumber+"\r\nDate Time : "+T.getSystemDateTime()+"\r\nLocation : HomeActivity.java parseResponsessSendAttendance() incorrect json \r\nJSON : "+result;
                    //BeplLog.saveResponse(sBody);

                }
            }
            else
            {
                T.t(HomeActivity.this, "0 or null json");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }

    protected void savePreference()
    {
        if (true == bConfiged)
        {
            prefsHardware.edit().putString("configed", "TRUE").commit();
            prefsHardware.edit().putInt("baudRate", baudRate).commit();
            prefsHardware.edit().putInt("stopBit", stopBit).commit();
            prefsHardware.edit().putInt("dataBit", dataBit).commit();
            prefsHardware.edit().putInt("parity", parity).commit();
            prefsHardware.edit().putInt("flowControl", flowControl).commit();
        }
        else
        {
            prefsHardware.edit().putString("configed", "FALSE").commit();
        }
    }

    @Override
    protected void onDestroy()
    {
        uartInterface.DestroyAccessory(bConfiged);
        super.onDestroy();
    }

    public void backActivity(View view)
    {
        showChangeLangDialog("back");
    }

    private void startConnection()
    {

        try
        {
            int chkNumBytes;
            byte[] chkStrSendBuffer = new byte[1];
            chkStrSendBuffer[0] = 37;

            chkNumBytes = chkStrSendBuffer.length;
            uartInterface.SendPacket(chkNumBytes, chkStrSendBuffer);

        }
        catch (Exception e)
        {
            T.t(HomeActivity.this, "connection : " + e);
        }
    }

    public void openWifi(View view)
    {
        showChangeLangDialog("wifi");
    }
    protected void restorePreference()
    {
        String key_name = prefsHardware.getString("configed", "");
        if (true == key_name.contains("TRUE")) {
            bConfiged = true;
        } else {
            bConfiged = false;
        }

        baudRate = prefsHardware.getInt("baudRate", 9600);
        stopBit = (byte) prefsHardware.getInt("stopBit", 1);
        dataBit = (byte) prefsHardware.getInt("dataBit", 8);
        parity = (byte) prefsHardware.getInt("parity", 0);
        flowControl = (byte) prefsHardware.getInt("flowControl", 0);
    }

    protected void cleanPreference()
    {
        SharedPreferences.Editor editor = prefsHardware.edit();
        editor.remove("configed");
        editor.remove("baudRate");
        editor.remove("stopBit");
        editor.remove("dataBit");
        editor.remove("parity");
        editor.remove("flowControl");
        editor.commit();
    }



    public void showChangeLangDialog(final String status)
    {
        //get password from shared preferences

        MyApplication.editor.commit();
        final String oldPasword = MyApplication.prefs.getString(Constants.OLD_PASSWORD,"0");
        final String newPasword =  MyApplication.prefs.getString(Constants.NEW_PASSWORD,"0");
        MyApplication.editor.commit();


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.editText);

        dialogBuilder.setTitle("Password");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String password = edt.getText().toString();

                if(newPasword.equals("0"))
                {
                    if (password.equals("bepl123"))
                    {
                        if (status.equals("cell"))
                        {
                            Intent i = new Intent(HomeActivity.this, AddCellActivity.class);
                            i.putExtra(Constants.OLD_PASSWORD,"bepl123");
                            i.putExtra(Constants.PASSWORD_STATUS,"second");
                            startActivity(i);
                          //  finish();
                        }
                        else if (status.equals("back"))
                        {
                            finish();
                        }
                        else if (status.equals("iot"))
                        {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_APP_BROWSER);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                        }
                        else if (status.equals("wifi"))
                        {
                            Intent i = new Intent(HomeActivity.this, WifiConfigurationActivity.class);
                            i.putExtra("is_first_time", true);
                            i.putExtra("FIRST", "second");
                            startActivity(i);
                        }

                    }
                    else
                    {
                        T.tE(HomeActivity.this, "Incorrect Password.");
                    }
                }
                else
                {
                    if(password.equals(newPasword))
                    {

                        if (status.equals("cell"))
                        {
                            Intent i = new Intent(HomeActivity.this, AddCellActivity.class);
                            i.putExtra(Constants.OLD_PASSWORD,""+oldPasword);
                            i.putExtra(Constants.PASSWORD_STATUS,"second");
                            startActivity(i);
                        }
                        else if (status.equals("back")) {
                            finish();
                        }
                        else if (status.equals("iot"))
                        {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_APP_BROWSER);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                        }
                        else if (status.equals("wifi"))
                        {
                            Intent i = new Intent(HomeActivity.this, WifiConfigurationActivity.class);
                            i.putExtra("is_first_time", true);
                            i.putExtra("FIRST", "second");
                            startActivity(i);
                        }

                    }
                    else
                    {
                        T.tE(HomeActivity.this, "Incorrect Password.");
                    }
                }



                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    public void showChangeLangDialogDevelopere(final String status)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.editText);

        dialogBuilder.setTitle("Password");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String password = edt.getText().toString();

                if (password.equals("sns321"))
                {

                    if(status.equals("delete"))
                    {
                        Intent i = new Intent(HomeActivity.this, MenuActivity.class);
                        startActivity(i);

                    }
                    else if(status.equals("loadData"))
                    {
                        if(T.checkConnection(HomeActivity.this))
                        {
                            getBooking("first");
                        }
                        else
                        {
                            T.t(HomeActivity.this,"Network connection off");
                        }
                    }

                }
                else
                {
                    T.tE(HomeActivity.this, "Incorrect Password.");
                }
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private class MyReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            String[] datapassedStatus = arg1.getStringExtra("DATAPASSED").split("#");

            if (datapassedStatus[0].equals("ok"))
            {
               // SHIFT_NAMES.clear();
                manageTimesAndShifts();
                setScreenData();
                String[] dateData = T.getSystemDateTime().split(" ");
                SharedPreferences preferences = getSharedPreferences(Constants.NAVIGATION, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.SYSTEM_DATE, dateData[0]);
                editor.commit();

                if (T.checkConnection(HomeActivity.this))
                {
                    changeStatus(datapassedStatus[1]);
                }

            }
            else if (datapassedStatus[0].equals("resetscreens"))
            {

                txt_shift_name.setText(datapassedStatus[1]);

                SharedPreferences preferences = getSharedPreferences(Constants.NAVIGATION, 0);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("status", "next");
                editor.commit();

                String dateSystem = preferences.getString(Constants.SYSTEM_DATE, "0");

                String[] dateData = T.getSystemDateTime().split(" ");


                if (dateData[0].equals(dateSystem))
                {
                    checkAvailableBooking("second");
                    //SHIFT_NAMES.clear();
                    manageTimesAndShifts();
                    setScreenData();
                }
                else
                {
                    checkAvailableBooking("first");
                    //SHIFT_NAMES.clear();
                    manageTimesAndShifts();
                    setScreenData();
                }


            }
            else if (datapassedStatus[0].equals("shiftend"))
            {
                //T.t(HomeActivity.this, "Shift End");
                txt_shift_name.setText("Shift End");

            }
            else if (datapassedStatus[0].equals("floatingbutton"))
            {
                Intent i = new Intent(HomeActivity.this, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                // finish();
            }
            else if (datapassedStatus[0].equals("cellAdd"))
            {
                //SHIFT_NAMES.clear();
                manageTimesAndShifts();
                setScreenData();
                // finish();
            }
            else if (datapassedStatus[0].equals("wifichanged"))
            {
                //SHIFT_NAMES.clear();
                manageTimesAndShifts();
                setScreenData();
                // finish();
            }

        }

    }

    private void changeStatus(String cellIdJsonArray) {
        String[] parameters =
                {
                        "cell_data" + "#" + cellIdJsonArray,
                        "data_flag" + "#" + "cell"
                        //"cell_id"+"#"+"1109C00016"
                };

        VolleyResponseClass.chekDataUpdtate(
                new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                    }
                },
                new VolleyErrorCallback() {
                    @Override
                    public void onError(VolleyError result) {

                        T.t(HomeActivity.this, "" + result);

                        //loadServerDataThreeMin();
                    }
                },
                getResources().getString(R.string.webUrl) + "" + getResources().getString(R.string.changeStatus),
                parameters);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }






    private static String returnCellNames()
    {
        ArrayList<CellInfo> CELL_NAMES = S.getAllCellIDForJsonArray();
        StringBuffer stringBuffer = new StringBuffer();

        for(int i = 0; i < CELL_NAMES.size(); i++)
        {
            CellInfo cellInfo = CELL_NAMES.get(i);
            stringBuffer.append("- "+cellInfo.getCell_name()+"\n");
        }

        return  stringBuffer.toString();
    }

    private static String convertHrsTosecond(String timeFormatString)
    {
        String timeSplit[] = timeFormatString.split(":");
        int seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 + Integer.parseInt(timeSplit[1]) * 60 + Integer.parseInt(timeSplit[2]);

        return String.valueOf(seconds);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {

            int chkNumBytes;
            byte[] chkStrSendBuffer = new byte[1];
            chkStrSendBuffer[0] = 'N';

            chkNumBytes = chkStrSendBuffer.length;
            uartInterface.SendPacket(chkNumBytes, chkStrSendBuffer);


            readBuffer = new byte[4096];

            // cap_image = "";
        }
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
       // SHIFT_NAMES.clear();
        manageTimesAndShifts();
        setScreenData();
        checkAvailableBooking("first");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // new getAllStudentsAsync().execute();

        startConnection();
        if (2 == uartInterface.ResumeAccessory()) {
            cleanPreference();
            restorePreference();
        }
    }

}


