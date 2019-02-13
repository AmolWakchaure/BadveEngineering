package engineering.badve.badveengineering.classes;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by sns003 on 25-Jan-18.
 */

public class VolleyErrors
{

    public static void handleError(VolleyError error, Context context)
    {
        try
        {

            if(error instanceof TimeoutError || error instanceof NoConnectionError)
            {
                T.t(context,"Warning ! Server not responding or no connection.");
            }
            else if(error instanceof AuthFailureError)
            {
                T.t(context,"Warning ! Remote server returns (401) Unauthorized?.");
            }
            else if(error instanceof ServerError)
            {
                T.t(context,"Warning ! Wrong webservice call or wrong webservice url.");
            }
            else if (error instanceof NetworkError)
            {
                T.t(context,"Warning ! You doesn't have a data connection or wi-fi Connection.");
            }
            else if(error instanceof ParseError)
            {
                T.t(context,"Warning ! Incorrect json response.");
            }

        }
        catch (Exception e)
        {

        }

    }

}
