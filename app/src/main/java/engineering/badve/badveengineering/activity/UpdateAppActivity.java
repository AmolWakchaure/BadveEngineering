package engineering.badve.badveengineering.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;


import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.adapter.CellIdAdapter;
import engineering.badve.badveengineering.authentication.LoginActivity;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.classes.V;
import engineering.badve.badveengineering.classes.VolleyCallback;
import engineering.badve.badveengineering.classes.VolleyErrorCallback;
import engineering.badve.badveengineering.classes.VolleyResponseClass;
import engineering.badve.badveengineering.dashboard.HomeActivity;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.interfaces.Constants;
import engineering.badve.badveengineering.widgets.MyButton;
import engineering.badve.badveengineering.widgets.MyEditext;

public class UpdateAppActivity extends AppCompatActivity {

    @BindView(R.id.tabIdButton)
    MyButton tabIdButton;

    @BindView(R.id.licenseKeyEditext)
    MyEditext licenseKeyEditext;

    @BindView(R.id.updateButton)
    MyButton updateButton;

    String tabID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_app);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initalise();

        tabID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID + "");
        tabIdButton.setText("Tab ID: "+tabID);

        checkLicenseKey();

        //generate license key
        if(T.checkConnection(UpdateAppActivity.this))
        {
            sendTabIdAnApplicationType();
        }
        else
        {
            T.t(UpdateAppActivity.this,"Network connection off");
        }
        updateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(!T.validateEmptyField(licenseKeyEditext,"Warning ! enter license key",UpdateAppActivity.this))
                {
                    return;
                }
                //generate license key
                if(T.checkConnection(UpdateAppActivity.this))
                {
                    followLogin();
                }
                else
                {
                    T.t(UpdateAppActivity.this,"Network connection off");
                }

            }
        });
    }
    private void initalise() {

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Update Application");



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_cell,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkLicenseKey()
    {
        MyApplication.editor.commit();
        String license_key = MyApplication.prefs.getString(Constants.LICENSE_KEY,"0");

        if(!license_key.equals("0"))
        {
            licenseKeyEditext.setText(license_key);
            licenseKeyEditext.setEnabled(false);
        }

    }

    private void followLogin() {

        String[] parameters =
                {
                        "deviceid"+"#"+tabID,
                        //"license_key"+"#"+"05313bN541T99e6512e8744839c",
                        "license_key"+"#"+licenseKeyEditext.getText().toString(),
                        // "deviceid"+"#"+"1f48aeda0335d7ca",
                        // "license_key"+"#"+"7ada403d51fcea38S18167z20617N",
                         "type"+"#"+"attendance"
                };
        VolleyResponseClass.checkAuthenticationProgressBar(
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
                "Authenticating...",
                UpdateAppActivity.this);


    }
    private void parseResponsessRegister(String response) {


        try {

            if (response != null || response.length() > 0)
            {

                JSONObject jsonResponse = new JSONObject(response);
                String successString = jsonResponse.getString("status");
                if (successString.equals("1"))
                {
                    MyApplication.editor.commit();
                    MyApplication.editor.putString(Constants.LICENSE_KEY,""+licenseKeyEditext.getText().toString());
                    MyApplication.editor.commit();

                    //check update available or not
                    checkUpdateAvailable();

                    /*final SharedPreferences preferences = getSharedPreferences(Constants.NAVIGATION, 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("status","true");
                    editor.putString("cell_id",cellIdEditext.getText().toString());
                    editor.commit();*/

                }
                else
                {

                    T.tE(UpdateAppActivity.this,"Warning ! incorrect input credentials");

                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void checkUpdateAvailable()
    {

        String[] parameters =
                {
                        "deviceid"+"#"+tabID,
                        "licensekey"+"#"+licenseKeyEditext.getText().toString(),
                        "version"+"#"+T.returnVersionName()
                };
        VolleyResponseClass.checkAuthenticationProgressBar(
                new VolleyCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {
                        parseVersionUpdateResponses(result);
                    }
                },
                new VolleyErrorCallback()
                {
                    @Override
                    public void onError(VolleyError result)
                    {

                        handleError(result,"third");

                    }
                },
                getResources().getString(R.string.checkUpdate),
                parameters,
                "Checking update...",
                UpdateAppActivity.this);

    }

    private void parseVersionUpdateResponses(String response)
    {

        try
        {

            if (response != null || response.length() > 0)
            {

                JSONObject jsonResponse = new JSONObject(response);
                String successString = jsonResponse.getString("result");
                T.e("successString : "+successString);
                if (successString.equals("0"))
                {
                    T.tI(UpdateAppActivity.this,"Fail ! License key mismatch.");
                    //BeplLog.d(">>", "resif>>>" + successString);
                }
                else
                {
                    //ready to download apk
                    if(T.checkConnection(UpdateAppActivity.this))
                    {
                        String apkDownloadUrl = getResources().getString(R.string.downloadUrl)+""+successString;
                        T.e("apkDownloadUrl : "+apkDownloadUrl);
                        //without progress
                        new DownloadUpdatedApkAsyncTask().execute(apkDownloadUrl);
                        //new DownloadUpdatedApkAsyncTask().execute("");
                    }
                    else
                    {
                        T.t(UpdateAppActivity.this,"Network connection off");
                    }


                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public class DownloadUpdatedApkAsyncTask extends AsyncTask<String, String, String>
    {

        String str_token="";
        SweetAlertDialog progressDialog;
        @Override
        protected void onPreExecute()
        {
            progressDialog = new SweetAlertDialog(UpdateAppActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            progressDialog.getProgressHelper().setBarColor(Color.parseColor("#3F51B5"));
            progressDialog.setTitleText("Downloading apk...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... urls) {

            Date new_date1 = new Date();
            String datetime = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new_date1);
            String path = Environment.getExternalStorageDirectory() + "/Download/" + datetime + ".apk";
            String    responseStr="";
            String downloadString = urls[0];
            //download the apk from your server and save to sdk card here
            try
            {

               // Log.e("downloadString",""+downloadString);

                //  path =  makeHttpRequest();
                //Log.d("sendUpdateApk","url-> "+Constants.url_download_file+""+MyApplication.prefs.getString(MyApplication.SESSION_TOKEN,""));
                URL url = new URL(downloadString);
                URLConnection connection = url.openConnection();
                connection.connect();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(path);

                byte data[] = new byte[1024];
                int count;
                while ((count = input.read(data)) != -1)
                {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            }catch(Exception e)
            {
                e.printStackTrace();
            }

            return path;
        }


        @Override
        protected void onPostExecute(String path) {
            // call to superuser command, pipe install updated APK without writing over files/DB
            try {
                progressDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public class DownloadFileFromURL extends AsyncTask<String, String, String>
    {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(UpdateAppActivity.this);
            progressDialog.setMessage("Downloading App. Please wait...");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {

            Date new_date1 = new Date();
            String datetime = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new_date1);
            String path = Environment.getExternalStorageDirectory() + "/Download/" + datetime + ".apk";
            try
            {
                T.e("URL : "+f_url[0]);

                URL url = new URL(f_url[0]);
                //URLConnection conection = url.openConnection();

                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                conection.connect();

                // getting file length
                //int lenghtOfFile = conection.getContentLength();
                int lenghtOfFile = 1024;

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(path);

                byte data[] = new byte[1024];

                long total = 0;
                int count;
                while ((count = input.read(data)) != -1)
                {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            }
            catch (Exception e)
            {
               // T.t(UpdateAppActivity.this,""+e);
                T.e("Exception"+e.getMessage());
            }

            return path;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }
        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url)
        {
            // dismiss the dialog after the file was downloaded
            progressDialog.dismiss();

            // T.e("path : "+path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(file_url)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


            /*// Reading image path from sdcard
            String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // setting downloaded into image view
            my_image.setImageDrawable(Drawable.createFromPath(imagePath));*/
        }

    }


    private void sendTabIdAnApplicationType()
    {

        String[] parameters =
                {
                        "deviceid"+"#"+tabID,
                        "type"+"#"+"attendance"
                };
        VolleyResponseClass.checkAuthenticationProgressBar(
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
                "Registering tab...",
                UpdateAppActivity.this);


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
                    T.tI(UpdateAppActivity.this,"Success ! Tab Id Successfully save.");
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

        new SweetAlertDialog(UpdateAppActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setConfirmText("Try again")
                .setContentText(error)
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener()
                {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog)
                    {

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
                        else if(callType.equals("third"))
                        {
                            checkUpdateAvailable();
                        }
                        else if(callType.equals("four"))
                        {
                            checkUpdateAvailable();
                        }

                    }
                })
                .show();

    }


}
