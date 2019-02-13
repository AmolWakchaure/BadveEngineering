package engineering.badve.badveengineering.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.adapter.CellIdAdapter;
import engineering.badve.badveengineering.authentication.LoginActivity;
import engineering.badve.badveengineering.classes.BeplLog;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.classes.V;
import engineering.badve.badveengineering.classes.VolleyCallback;
import engineering.badve.badveengineering.classes.VolleyErrorCallback;
import engineering.badve.badveengineering.classes.VolleyErrors;
import engineering.badve.badveengineering.classes.VolleyResponseClass;
import engineering.badve.badveengineering.dashboard.HomeActivity;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.interfaces.Constants;
import engineering.badve.badveengineering.pojo.CellIdInformation;
import engineering.badve.badveengineering.pojo.CellInfo;
import engineering.badve.badveengineering.widgets.MyButton;

public class AddCellActivity extends AppCompatActivity {

    String[] cellId = {

            "C1234561",
            "C1234562",
            "C1234563",
            "C1234564"

    };

    String jsonResponse;

    @BindView(R.id.cellIdsRecyclerView)
    RecyclerView cellIdsRecyclerView;

    @BindView(R.id.updateAppButtonAnother)
    Button updateAppButtonAnother;

    public final static String MY_ACTION = "MY_ACTION";

    @BindView(R.id.cellIdSearchableSpinner)
    SearchableSpinner cellIdSearchableSpinner;

    @BindView(R.id.submitCellIdButton)
    Button submitCellIdButton;

    @BindView(R.id.updateAppButton)
    MyButton updateAppButton;

    private CellIdAdapter cellIdAdapter;



    public static boolean isChangeMenu = false;

    private SweetAlertDialog progressDialog;
   private String PASSWORD_STATUS,OLD_PASSWORDD ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cell);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressDialog = new SweetAlertDialog(AddCellActivity.this, SweetAlertDialog.PROGRESS_TYPE);


        initalise();
        setCellData();
        Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
            PASSWORD_STATUS = bundle.getString(Constants.PASSWORD_STATUS);
            OLD_PASSWORDD = bundle.getString(Constants.OLD_PASSWORD);
        }

        if(T.checkConnection(AddCellActivity.this))
        {
            getCellId();

        }
        else
        {
            T.tW(AddCellActivity.this,getString(R.string.network));
        }

        //setData();
    }

    private void getCellId()
    {


        MyApplication.editor.commit();
        String plant_code = MyApplication.prefs.getString(Constants.PLANT_CODE,"0");
        MyApplication.editor.commit();


        final String plant_codeJsonArray = T.returnPlantCode(plant_code);

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
                AddCellActivity.this,
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


                    setCellId(CELL_ID);

                }
                else
                {
                    T.tE(AddCellActivity.this,"Error ! cell id not found.");

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setCellId(ArrayList<String> cellId)
    {

        ArrayAdapter<String> pallet_a_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview,R.id.spinnerTxtView, cellId);
        pallet_a_adapter.setDropDownViewResource(R.layout.spinner_textview);
        cellIdSearchableSpinner.setAdapter(pallet_a_adapter);
        cellIdSearchableSpinner.setTitle("Select cell ID");


    }

    private void setCellData() {





        ArrayList<CellInfo> CELL_INFO = S.getAllCellIDForJsonArray();

        if (CELL_INFO.isEmpty())
        {
            CellInfo cellIdInformation = new CellInfo();
            cellIdInformation.setCell_id("Not Found");
            CELL_INFO.add(cellIdInformation);

            cellIdAdapter.setCellId(CELL_INFO);
            cellIdAdapter.notifyDataSetChanged();
        }
        else
        {


            cellIdAdapter.setCellId(CELL_INFO);
            cellIdAdapter.notifyDataSetChanged();

        }


    }

    private void initalise()
    {

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Add Cell");

            cellIdsRecyclerView.setLayoutManager(new LinearLayoutManager(AddCellActivity.this));
            cellIdAdapter = new CellIdAdapter(AddCellActivity.this);
            cellIdsRecyclerView.setAdapter(cellIdAdapter);


        updateAppButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent i = new Intent(AddCellActivity.this, ChangePasswordActivity.class);
                i.putExtra(Constants.PASSWORD_STATUS,PASSWORD_STATUS);
                i.putExtra(Constants.OLD_PASSWORD,OLD_PASSWORDD);
                startActivity(i);
                finish();


            }
        });

        updateAppButtonAnother.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent i = new Intent(AddCellActivity.this,UpdateAppActivity.class);
                startActivity(i);


            }
        });


        submitCellIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (T.checkConnection(AddCellActivity.this))
                {
                    if (!V.validateSpinner(cellIdSearchableSpinner, "Warning ! select cell Id"))
                    {
                        return;
                    }
                    //check countcvcb dfgdf
                    String cellCount = S.getCellCount();

                    if (cellCount.equals("3")) {

                        displayErrorMessage();
                    }
                    else
                        {

                        String [] cellData =  cellIdSearchableSpinner.getSelectedItem().toString().split("\n");

                        //check cell already
                        if (!S.checkCellAlready(cellData[0], "Duplicate cell id found.", AddCellActivity.this)) {
                            return;
                            //T.tE(AddCellActivity.this,"Duplicate cell id found.");
                        }
                        MyApplication.db.insertCellIds(cellData[0],cellData[1]);
                        setCellData();
                        displaySuccessMessage(cellData[0]);

                    }
                }
                else {
                    T.displayErrorMessage("Sorry...", "Ok", "No internet connection,please check your internet connection");
                }

            }
        });


    }

    public void displaySuccessMessage(final String cellId)
    {
        new SweetAlertDialog(AddCellActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setConfirmText("Ok")
                .setContentText("Cell Id successfully added.")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();


                        if (T.checkConnection(AddCellActivity.this)) {
                            checkTargetAvailable(cellId);
                        } else {
                            T.tE(AddCellActivity.this, "Network connection off");
                        }


                    }
                })
                .show();
    }

    private void checkTargetAvailable(final String cellId) {

        ArrayList<CellInfo> CELL_ID = new ArrayList<>();

        CellInfo cellInfo = new CellInfo();
        cellInfo.setCell_id(cellId);
        CELL_ID.add(cellInfo);

        final String cellIdJsonArray = T.arrayListToJsonArrayCellId(CELL_ID);

        String[] parameters =
                {
                        "cell_id" + "#" + cellIdJsonArray,
                        "date" + "#" + T.getSystemDate()
                };
        VolleyResponseClass.getBookingData(
                new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                        jsonResponse = result;
                        new ParseResponses().execute();
                        //parseResponsess(result);
                    }
                },
                new VolleyErrorCallback() {
                    @Override
                    public void onError(VolleyError result)
                    {
                        //T.tE(AddCellActivity.this,""+result);

                        VolleyErrors.handleError(result,AddCellActivity.this);
                        /*BeplLog.storeerror(
                                " Webservice Name : "+getResources().getString(R.string.load_data)+
                                        " cell_id : "+cellIdJsonArray+
                                        " checkTargetAvailable() : "+result+
                                        " DateTime : "+T.getSystemDateTime());*/
                    }
                },
                AddCellActivity.this,
                getResources().getString(R.string.webUrl) + "" + getResources().getString(R.string.load_data),
                parameters,
                "Checking targets...");


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




                String[] systemDateArray = T.getSystemDateTime().split(" ");
                String systemDate = systemDateArray[0];


                if (jsonResponse != null || jsonResponse.length() > 0) {
                    Object json = new JSONTokener(jsonResponse).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject dataJsonObject = new JSONObject(jsonResponse);
                        String success = dataJsonObject.getString("success");

                        if (success.equals("1")) {

                            JSONArray cellJsonArray = dataJsonObject.getJSONArray("cell");
                            JSONArray employeeJsonArray = dataJsonObject.getJSONArray("employee");
                            JSONArray bookingJsonArray = dataJsonObject.getJSONArray("booking");
                            JSONArray shiftJsonArray = dataJsonObject.getJSONArray("shift");


                        /*if (!checkEmptyArray(cellJsonArray, "Oops", "OK", "Cell details not found.")) {
                            return;
                        }
                        if (!checkEmptyArray(employeeJsonArray, "Oops", "OK", "Employee details not found.")) {
                            return;
                        }
                        if (!checkEmptyArray(bookingJsonArray, "Oops", "OK", "Booking details not found.")) {
                            return;
                        }
                        if (!checkEmptyArray(shiftJsonArray, "Oops", "OK", "Shift details not found.")) {
                            return;
                        }
*/

                            //employeeJsonArray
//                        for(int i = 0; i < employeeJsonArray.length(); i++)
//                        {
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
                                        // MyApplication.db.insertEmployeeDetails(RFID_no.trim(),employee_code,contractual_type_id,contractor_id,cell_id,systemDate,Constants.SYNCH_STATUS_N);
                                        String alreadyEmployee = S.checkEmployeeAlreadyEntryf(RFID_no.trim(),employee_code,systemDate);

                                        if(alreadyEmployee != null)
                                        {
                                            if(alreadyEmployee.equals("1"))
                                            {
                                                // Log.e("UPDATE>>","if");
                                                MyApplication.db.updateEmployeeDetails(RFID_no.trim(),employee_code,contractual_type_id,contractor_id,cell_id,systemDate);
                                            }
                                            else if(alreadyEmployee.equals("0"))
                                            {
                                                // Log.e("UPDATE>>","else");
                                                MyApplication.db.insertEmployeeDetails(RFID_no.trim(),employee_code,contractual_type_id,contractor_id,cell_id,systemDate,Constants.SYNCH_STATUS_N);

                                            }
                                        }

                                    /*if (!S.checkEmployeeAlreadyEntry(RFID_no.trim(), employee_code, contractual_type_id, contractor_id, cell_id, systemDate, Constants.SYNCH_STATUS_N)) {
                                        MyApplication.db.insertEmployeeDetails(RFID_no.trim(), employee_code, contractual_type_id, contractor_id, cell_id, systemDate, Constants.SYNCH_STATUS_N);
                                    }*/
                                    }
                                }

                            }



                            //MyApplication.db.updateTarget("0");
                            for (int e = 0; e < cellJsonArray.length(); e++) {
                                JSONArray eJsonArray = bookingJsonArray.getJSONArray(e);

                                if (eJsonArray.length() != 0) {
                                    //MyApplication.db.updateTarget("0");

                                    //

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

                                        // MyApplication.db.insertBookingDetails(contractual_type,target,"0","0",shift,contractor_code,cell_id,id);

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

                                String shiftAlready = S.checkShiftAlreadyEntry(shift_name, shift_from, shift_to, systemDate, Constants.SYNCH_STATUS_N);

                                if(shiftAlready != null)
                                {
                                    if (shiftAlready.equals("0"))
                                    {
                                        MyApplication.db.insertShiftDetails(shift_name, shift_from, shift_to, systemDate, Constants.SYNCH_STATUS_N);
                                    }
                                }


                                //MyApplication.db.insertShiftDetails(shift_name,shift_from,shift_to,systemDate,Constants.SYNCH_STATUS_N);
                            }



                        } else {
                            // T.t(AddCellActivity.this, "No data found");

                            //T.displayErrorMessage(context,"Oops","OK","No attendance details found");
                        }
                    } else {
                        // T.t(AddCellActivity.this, "incorect json");
                    }
                } else {
                    //  T.t(AddCellActivity.this, "0 or null json");
                }
            } catch (Exception e) {
                //T.t(HomeActivity.this, "Exception :"+e);
                //testError.setText(""+e);
                // T.displayErrorMessage("Exception","ok",""+e);
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {



            //send broad cast when data successfully inserted
            Intent intent = new Intent();
            intent.setAction(MY_ACTION);

            intent.putExtra("DATAPASSED", "cellAdd#NA");

            sendBroadcast(intent);

            finish();

        }
    }



    public boolean checkEmptyArray(JSONArray jsonArray, String title, String confirmText, String message) {
        if (jsonArray.length() == 0) {
            T.t(AddCellActivity.this, message);
            //requestFocus(editText);
            return false;
        } else {
            return true;
        }


    }

    private void handleError(VolleyError error, String cellId) {
        try {

            if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                displayError("TimeoutError/NoConnectionError", "Server not responding or no connection.", cellId);


            } else if (error instanceof AuthFailureError) {

                displayError("AuthFailureError", "Remote server returns (401) Unauthorized?.", cellId);

            } else if (error instanceof ServerError) {


                displayError("ServerError", "Wrong webservice call or wrong webservice url.", cellId);

            } else if (error instanceof NetworkError) {
                displayError("NetworkError", "you doesn't have a data connection and wi-fi Connection.", cellId);

            } else if (error instanceof ParseError) {

                displayError("NetworkError", "Incorrect json response.", cellId);
            }


        } catch (Exception e) {

        }

    }

    private void displayError(String title, String error, final String cellId) {

        new SweetAlertDialog(AddCellActivity.this, SweetAlertDialog.ERROR_TYPE)
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
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        checkTargetAvailable(cellId);

                    }
                })
                .show();

    }

    public void displayErrorMessage() {
        new SweetAlertDialog(AddCellActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Fail")
                .setConfirmText("Ok")
                .setContentText("You can add maximum 3 cell id here.")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();


                    }
                })
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_cell, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteCellId(String cellId)
    {
        if (isChangeMenu)
        {
            try
            {
                MyApplication.db.deleteCellId(cellId);
                MyApplication.db.deleteBookingAndEmployeeCellWise(cellId);
                displayMessage();
            }
            catch (Exception e)
            {
                T.t(AddCellActivity.this, "" + e);
            }

        } else {

            T.t(AddCellActivity.this, "Heloo");
        }


    }

    public void displayMessage() {
        new SweetAlertDialog(AddCellActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setConfirmText("Ok")
                .setContentText("Cell Id successfully deleted.")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        setCellData();


                    }
                })
                .show();
    }

    public void updateApplication(View view) {

    }
}
