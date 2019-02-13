package engineering.badve.badveengineering.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import engineering.badve.badveengineering.authentication.LoginActivity;
import engineering.badve.badveengineering.dashboard.HomeActivity;


/**
 * Created by snsystem_amol on 3/11/2017.
 */
public class VolleyResponseClass
{

    public static void chekDataUpdtate(final VolleyCallback callback,
                                       final VolleyErrorCallback errorLog,
                                       final String url,
                                       final String[] parameters)
    {

        try
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {

                            callback.onSuccess(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            //T.t(context,""+error);


                            errorLog.onError(error);

                        }
                    }){
                @Override
                protected Map<String,String> getParams()
                {

                    Map<String,String> params = new HashMap<String, String>();

                    for(int i = 0; i < parameters.length; i++)
                    {
                        String[] dataParams = parameters[i].split("#");
                        params.put(dataParams[0],dataParams[1]);
                    }

                    return params;
                }
            };


            MyApplication.requestQueue.getCache().clear();
            stringRequest.setRetryPolicy(MyApplication.retryPolicy);
            MyApplication.requestQueue.add(stringRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void getResponseProgressDialogThreeMin(final VolleyCallback callback,
                                                 final VolleyErrorCallback errorLog,
                                                 final String url,
                                                 final String[] parameters)
    {

        try
        {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {

                            callback.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            errorLog.onError(error);
                        }
                    })
            {
                @Override
                protected Map<String,String> getParams()
                {

                    Map<String,String> params = new HashMap<String, String>();

                    for(int i = 0; i < parameters.length; i++)
                    {
                        String[] dataParams = parameters[i].split("#");
                        params.put(dataParams[0],dataParams[1]);
                    }

                    return params;
                }
            };

            MyApplication.requestQueue.getCache().clear();
            stringRequest.setRetryPolicy(MyApplication.retryPolicy);
            MyApplication.requestQueue.add(stringRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static void checkAuthentication(final VolleyCallback callback,
                                           final VolleyErrorCallback errorLog,
                                           final String url,
                                           final String[] parameters,
                                           final String progressMessage,
                                           final Context context)
    {

        try
        {

            final SweetAlertDialog progressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            progressDialog.getProgressHelper().setBarColor(Color.parseColor("#3F51B5"));
            progressDialog.setTitleText(progressMessage);
            progressDialog.setCancelable(false);
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            progressDialog.dismiss();
                            callback.onSuccess(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            progressDialog.dismiss();
                            errorLog.onError(error);
                        }
                    }){
                @Override
                protected Map<String,String> getParams()
                {

                    Map<String,String> params = new HashMap<String, String>();

                    for(int i = 0; i < parameters.length; i++)
                    {
                        String[] dataParams = parameters[i].split("#");
                        params.put(dataParams[0],dataParams[1]);
                    }

                    return params;
                }
            };

            MyApplication.requestQueue.getCache().clear();
            stringRequest.setRetryPolicy(MyApplication.retryPolicy);
            MyApplication.requestQueue.add(stringRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static void sendInAttendance(final VolleyCallback callback,
                                           final VolleyErrorCallback errorLog,
                                           final String url,
                                           final String[] parameters,
                                        Context context)
    {

        try
        {
            final SweetAlertDialog progressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            progressDialog.getProgressHelper().setBarColor(Color.parseColor("#3F51B5"));
            progressDialog.setTitleText("Sending attendance...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            progressDialog.dismiss();
                            callback.onSuccess(response);

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {

                            progressDialog.dismiss();
                            errorLog.onError(error);

                        }
                    }){
                @Override
                protected Map<String,String> getParams()
                {

                    Map<String,String> params = new HashMap<String, String>();

                    for(int i = 0; i < parameters.length; i++)
                    {
                        String[] dataParams = parameters[i].split("#");
                        params.put(dataParams[0],dataParams[1]);
                    }

                    return params;
                }
            };

            MyApplication.requestQueue.getCache().clear();
            stringRequest.setRetryPolicy(MyApplication.retryPolicy);
            MyApplication.requestQueue.add(stringRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static void synchOffline(final VolleyCallback callback,
                                        final VolleyErrorCallback errorLog,
                                        final Context context,
                                        final String url,
                                        final String[] parameters)
    {

        try
        {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            callback.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            errorLog.onError(error);
                        }
                    })
            {
                @Override
                protected Map<String,String> getParams()
                {
                    Map<String,String> params = new HashMap<String, String>();
                    for(int i = 0; i < parameters.length; i++)
                    {
                        String[] dataParams = parameters[i].split("#");
                        params.put(dataParams[0],dataParams[1]);
                    }

                    return params;
                }
            };
            MyApplication.requestQueue.getCache().clear();
            stringRequest.setRetryPolicy(MyApplication.retryPolicy);
            MyApplication.requestQueue.add(stringRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static void checkAuthenticationProgressBar(final VolleyCallback callback,
                                           final VolleyErrorCallback errorLog,
                                           final String url,
                                           final String[] parameters,
                                           final String progressMessage,
                                           Context context)
    {
        try
        {
            final SweetAlertDialog progressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            progressDialog.getProgressHelper().setBarColor(Color.parseColor("#3F51B5"));
            progressDialog.setTitleText(progressMessage);
            progressDialog.setCancelable(false);
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            progressDialog.dismiss();
                            callback.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            progressDialog.dismiss();
                            errorLog.onError(error);
                        }
                    })
            {
                @Override
                protected Map<String,String> getParams()
                {

                    Map<String,String> params = new HashMap<String, String>();

                    for(int i = 0; i < parameters.length; i++)
                    {
                        String[] dataParams = parameters[i].split("#");
                        params.put(dataParams[0],dataParams[1]);
                    }

                    return params;
                }
            };
            MyApplication.requestQueue.getCache().clear();
            stringRequest.setRetryPolicy(MyApplication.retryPolicy);
            MyApplication.requestQueue.add(stringRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void selectCellId(final VolleyCallback callback,
                                           final VolleyErrorCallback errorLog,
                                           final String url)
    {
        try
        {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {

                            callback.onSuccess(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {

                            errorLog.onError(error);

                        }
                    });
            MyApplication.requestQueue.getCache().clear();
            stringRequest.setRetryPolicy(MyApplication.retryPolicy);
            MyApplication.requestQueue.add(stringRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void getBookingData(final VolleyCallback callback,
                                           final VolleyErrorCallback errorLog,
                                           final Context context,
                                           final String url,
                                           final String[] parameters,
                                           final String progressMessage)
    {

        try
        {
            final SweetAlertDialog progressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            progressDialog.getProgressHelper().setBarColor(Color.parseColor("#3F51B5"));
            progressDialog.setTitleText(progressMessage);
            progressDialog.setCancelable(false);
            progressDialog.show();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            progressDialog.dismiss();
                            callback.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                           // T.t(context,""+error);
                            progressDialog.dismiss();
                            errorLog.onError(error);
                        }
                    })
            {
                @Override
                protected Map<String,String> getParams()
                {
                    Map<String,String> params = new HashMap<String, String>();
                    for(int i = 0; i < parameters.length; i++)
                    {
                        String[] dataParams = parameters[i].split("#");
                        params.put(dataParams[0],dataParams[1]);
                    }

                    return params;
                }
            };

            MyApplication.requestQueue.getCache().clear();
            stringRequest.setRetryPolicy(MyApplication.retryPolicy);
            MyApplication.requestQueue.add(stringRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void getBookingData(final VolleyCallback callback,
                                      final VolleyErrorCallback errorLog,
                                      final Context context,
                                      final String url,
                                      final String[] parameters,
                                      final String progressMessage,
                                      final String status)
    {

        try
        {
            final SweetAlertDialog progressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);


           // final ProgressDialog progressDialog = new ProgressDialog(context);
            if(status.equals("first"))
            {

                progressDialog.getProgressHelper().setBarColor(Color.parseColor("#3F51B5"));
                progressDialog.setTitleText(progressMessage);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            if(status.equals("first"))
                            {
                                progressDialog.dismiss();
                            }
                            callback.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(status.equals("first"))
                            {
                                progressDialog.dismiss();
                            }
                            errorLog.onError(error);
                        }
                    })
            {
                @Override
                protected Map<String,String> getParams()
                {

                    Map<String,String> params = new HashMap<String, String>();

                    for(int i = 0; i < parameters.length; i++)
                    {
                        String[] dataParams = parameters[i].split("#");
                        params.put(dataParams[0],dataParams[1]);
                    }

                    return params;
                }
            };

            MyApplication.requestQueue.getCache().clear();
            stringRequest.setRetryPolicy(MyApplication.retryPolicy);
            MyApplication.requestQueue.add(stringRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static void getBookingData(final VolleyCallback callback,
                                      final VolleyErrorCallback errorLog,
                                      final String url,
                                      final String[] parameters)
    {
        try
        {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {


                            callback.onSuccess(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {


                            errorLog.onError(error);

                        }
                    })
            {
                @Override
                protected Map<String,String> getParams()
                {

                    Map<String,String> params = new HashMap<String, String>();

                    for(int i = 0; i < parameters.length; i++)
                    {
                        String[] dataParams = parameters[i].split("#");
                        params.put(dataParams[0],dataParams[1]);
                    }

                    return params;
                }
            };

            MyApplication.requestQueue.getCache().clear();
            stringRequest.setRetryPolicy(MyApplication.retryPolicy);
            MyApplication.requestQueue.add(stringRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
