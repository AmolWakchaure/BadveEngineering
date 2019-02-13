package engineering.badve.badveengineering.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import engineering.badve.badveengineering.interfaces.Constants;

/**
 * Created by snsystem_amol on 03/07/2017.
 */

public class V
{
    public static boolean validateOldPassword(String oldPassword, String inputPassword)
    {
        if (oldPassword.equals(inputPassword))
        {

            return true;
        }
        else
        {
            T.tE(MyApplication.context,"Warning ! incorrect old password.");
            return false;
        }


    }
    public static boolean validateConfNewPassword(String newPassword, String confNewPassword)
    {
        if (newPassword.equals(confNewPassword))
        {
            return true;
        }
        else
        {
            T.tE(MyApplication.context,"Warning ! new password and confirm password should be same.");
            return false;
        }


    }
    public static boolean validateEmptyField(Context context,EditText editText, String message)
    {
        if (editText.getText().toString().trim().isEmpty())
        {
            T.tE(context,message);
            return false;
        }
        else
        {
            return true;
        }


    }
    public static boolean validateSpinner(SearchableSpinner editText, String message)
    {
        boolean status = false;
        try
        {
            if (editText.getSelectedItem().toString().trim().isEmpty())
            {
                T.tE(MyApplication.context,message);
                status = false;
            }
            else
            {
                status = true;
            }
        }
        catch (Exception e)
        {
            T.tE(MyApplication.context,message);
            status = false;
        }

        return status;

    }
    public static boolean validateEmptyButton(AutoCompleteTextView editText, String data, String message)
    {
        if (editText.getText().toString().equals(data))
        {
            T.tE(MyApplication.context,message);
            return false;
        }
        else
        {
            return true;
        }


    }

    public static boolean valiNull(String data)
    {
        if (data != null || data.length() > 0)
        {

            return true;
        }
        else
        {
            return false;
        }


    }
    public static boolean validateCellId(Context context,String cellIdd,String message)
    {
        //get device id
        final SharedPreferences[] preferences = {context.getSharedPreferences(Constants.NAVIGATION, 0)};
        SharedPreferences.Editor editor = preferences[0].edit();
        editor.commit();

        String cellId = preferences[0].getString("cell_id","0");

        if (cellId.equals(cellIdd))
        {
            T.tE(context,message);
            return false;
        }
        else
        {
            return true;
        }


    }
}
