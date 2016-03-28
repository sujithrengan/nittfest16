package org.delta.nittfest;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GCMRegisterService extends IntentService {
    static Context context;
    String token;
    static String msg;
    public static void register(Context cont) {
        context = cont;
        Intent intent = new Intent(cont, GCMRegisterService.class);
        cont.startService(intent);
    }

    public GCMRegisterService() {
        super("GCMRegisterService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.e("gcm_status",token.toString());
            String serverUrl = Utilities.url_gcm;
            Log.e("gcm_status", "Attempt to register");


/*

            StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    msg = response;
                    try {

                        JSONObject js = new JSONObject(response);
                        int gcm = js.getInt("status_code");
                        Utilities.gcm_registered = 1;
                        SharedPreferences.Editor editor = Utilities.sp.edit();
                        editor.putInt("gcm_registered", Utilities.gcm_registered);
                        editor.apply();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.e("gcm_status", "Failed to register :" + error);

                }
            }){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<>();

                    params.put("gcm_id", token);
                    return params;
                }
            };

            int socketTimeout = 10000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            Volley.newRequestQueue(context).add(stringRequest);
*/


            String res="bleh";
            HttpClient httpClient=new DefaultHttpClient();
            HttpEntity httpEntity=null;
            HttpPost httpPost=new HttpPost(Utilities.url_gcm);


            try {
                List nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("p_id", "1111"));
                nameValuePairs.add(new BasicNameValuePair("gcm_id", token));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                HttpResponse response=httpClient.execute(httpPost);
                httpEntity=response.getEntity();
                res= EntityUtils.toString(httpEntity);

                Log.e("Async", res);

            } catch (IOException e) {
                Log.e("Async", "catched");
                e.printStackTrace();
            }

            try {

                JSONObject js = new JSONObject(res);
                int gcm = js.getInt("status_code");
                Utilities.gcm_registered = 1;
                SharedPreferences.Editor editor = Utilities.sp.edit();
                editor.putInt("gcm_registered", Utilities.gcm_registered);
                editor.apply();
            }catch (Exception e){

                Log.e("gcm_status", "Failed to register :");
                e.printStackTrace();
            }



        }catch(Exception e){
            e.printStackTrace();
        }

    }


}


