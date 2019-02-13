package engineering.badve.badveengineering.download;

import android.content.Context;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.classes.T;

public class DownloadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
    }

    public void donloadFile(View view) {

        Date new_date1 = new Date();
        String datetime = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new_date1);
        final String path = Environment.getExternalStorageDirectory() + "/Download/";

        String mUrl= "http://192.168.0.11:81/possibill/release/1hpgwBtaSnadai1/V1.7/Bepl_v1_7.apk";
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, mUrl,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        // TODO handle the response
                        try {
                            if (response!=null)
                            {
                                try {
     /* file_byte is yous json string*/

                                    byte[] decodestring = Base64.decode(response, Base64.DEFAULT);
                                    File file = Environment.getExternalStorageDirectory();
                                    File dir = new File(path);
                                    if (!dir.exists()) {
                                        dir.mkdirs();
                                    }
                                    File document = new File(dir, "amol.apk");

                                    if (document.exists()) {
                                        document.delete();
                                    }

                                    FileOutputStream fos = new FileOutputStream(document.getPath());
                                    fos.write(decodestring);
                                    fos.close();

                                    Toast.makeText(DownloadActivity.this, "Download complete.", Toast.LENGTH_LONG).show();
                                }
                                catch (Exception e)
                                {
                                    T.e(""+e);
                                    //Toast.makeText(DownloadActivity.this, "failed to download file.", Toast.LENGTH_LONG).show();
                                }

                                /*FileOutputStream outputStream;

                                //String name=<FILE_NAME_WITH_EXTENSION e.g reference.txt>;
                                outputStream = openFileOutput("amol.apk", Context.MODE_PRIVATE);

                                outputStream.write(response);
                                outputStream.close();
                                Toast.makeText(DownloadActivity.this, "Download complete.", Toast.LENGTH_LONG).show();*/
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                            e.printStackTrace();
                        }
                    }
                } ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle the error
                error.printStackTrace();
            }
        }, null);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
        mRequestQueue.add(request);
    }
}
