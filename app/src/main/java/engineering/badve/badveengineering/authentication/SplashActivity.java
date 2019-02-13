package engineering.badve.badveengineering.authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.dashboard.HomeActivity;
import engineering.badve.badveengineering.interfaces.Constants;
import engineering.badve.badveengineering.wifisetting.WifiConfigurationActivity;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        manageNavigation();
    }

    private void manageNavigation()
    {

        try
        {
            MyApplication.editor.commit();
            String plantCode = MyApplication.prefs.getString(Constants.PLANT_CODE,"0");
            MyApplication.editor.commit();

            if(plantCode.equals("0"))
            {

                Intent i = new Intent(SplashActivity.this,WifiConfigurationActivity.class);
                i.putExtra("FIRST","first");
                startActivity(i);
                finish();

            }
            else
            {
                Intent i = new Intent(SplashActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
