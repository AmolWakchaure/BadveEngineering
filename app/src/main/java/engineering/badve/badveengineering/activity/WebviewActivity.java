package engineering.badve.badveengineering.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import engineering.badve.badveengineering.R;


public class WebviewActivity extends Activity {


    private WebView webView;
    private LinearLayout backButton;

    private String url="http://demomnx.azurewebsites.net/";
    //private String url="http://1.22.124.222:81/obd/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initialize();
        checkNetworkConnection();
    }
    private void checkNetworkConnection() {

        try
        {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo datac = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected()))
            {
                startWebView(url);
                //webView.loadUrl(url);
            }
            else
            {
                Toast.makeText(WebviewActivity.this,"Network conection off", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void startWebView(String url) {

        webView.setWebViewClient(new WebViewClient()
        {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(WebviewActivity.this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    //progressWheel.setVisibility(View.VISIBLE);
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        // progressWheel.setVisibility(View.GONE);
                        progressDialog = null;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);


    }
    @Override
    // Detect when the back button is pressed
    public void onBackPressed()
    {
        goBack();
    }

    private void goBack()
    {
        if(webView.canGoBack())
        {
            webView.goBack();
        }
        else
        {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

    private void initialize()
    {
        webView = (WebView) findViewById(R.id.webView1);

        backButton = (LinearLayout) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goBack();
            }
        });
    }
}
