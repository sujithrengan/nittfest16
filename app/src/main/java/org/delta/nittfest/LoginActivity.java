package org.delta.nittfest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {

    String rollNumber;
    String password;
    EditText rollNumberText, passwordText;
    Button button;
    Typeface t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        this.t=Typeface.createFromAsset(this.getAssets(),"fonts/hn.otf");

        //Intent intent = new Intent(this, BettingScreen.class);
        //startActivity(intent);
        //finish();
        handleButtonClick();
    }


    private void handleButtonClick() {

        rollNumberText = (EditText) findViewById(R.id.rollNumber);
        passwordText = (EditText) findViewById(R.id.password);
        rollNumberText.setTypeface(t);
        passwordText.setTypeface(t);
        button = (Button) findViewById(R.id.signInButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollNumber = rollNumberText.getText().toString();
                if (rollNumber.length() != 9)
                    rollNumberText.setError("Invalid roll number");
                else {
                    password = passwordText.getText().toString();
                    //Pass rollNumber and password to the server


                    new AuthTask().execute();

                    //sslcheck();


                    //loginvolley();
                    //Testing
                    //    Intent i = new Intent(LoginActivity.this, Coupon.class);
                    //    LoginActivity.this.startActivity(i);

                    button.setClickable(false);
                }
            }
        });
        CheckBox checkBox = (CheckBox) findViewById(R.id.showPasswordCheckBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                if (!isChecked) {
                    passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }



    class AuthTask extends AsyncTask<String, Void, String> {
        ProgressDialog myPd_ring = null;
        @Override
        protected void onPreExecute() {

            myPd_ring  = new ProgressDialog (LoginActivity.this);
            myPd_ring.setMessage("Logging In...");
            myPd_ring.setCancelable(false);
            myPd_ring.setCanceledOnTouchOutside(false);
            myPd_ring.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String error = null;

            HttpClient httpclient = new DefaultHttpClient();

            HttpEntity httpEntity = null;
            HttpPost httppost = new HttpPost(Utilities.url_auth);
            JSONObject jsonObject;

            try {
                List nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("user_roll", rollNumber));
                nameValuePairs.add(new BasicNameValuePair("user_pass", password));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                HttpResponse response = null;

                response = httpclient.execute(httppost);
                httpEntity = response.getEntity();
                String s = null;
                s = EntityUtils.toString(httpEntity);

                Log.e("ll", s);

                jsonObject = new JSONObject(s);
                Log.e("response", s);
                Utilities.status = jsonObject.getInt("status");
                error = jsonObject.getString("error");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ll",String.valueOf(e));


            }



            return error;
        }

        @Override
        protected void onPostExecute(String error) {
            super.onPostExecute(error);
            System.out.println("Error: " + error);
            myPd_ring.dismiss();


            switch (Utilities.status) {
                case 0:
                    Toast.makeText(LoginActivity.this, "There was a problem connecting to the server. Please check your username and password and try again.", Toast.LENGTH_LONG).show();
                    rollNumberText.setText("");
                    passwordText.setText("");
                    button.setClickable(true);
                    break;
                case 1:
                    //Intent intent = new Intent(getBaseContext(), BettingScreen.class);
                    SharedPreferences.Editor editor = Utilities.sp.edit();
                    editor.putInt("status", Utilities.status);
                    editor.putString("user_name", rollNumber);
                    Utilities.username = rollNumber;
                    editor.putString("user_pass", password);
                    Utilities.password = password;
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"LoggedIn",Toast.LENGTH_SHORT).show();
                    new EventsTask().execute();
                    //startActivity(intent);
                    //finish();
                    break;

                case 3:
                    Toast.makeText(LoginActivity.this, "Your account is not on the system. Please contact NITTFEST OC", Toast.LENGTH_SHORT).show();
                    rollNumberText.setText("");
                    passwordText.setText("");
                    button.setClickable(true);
                    break;
            }
        }
    }



    class ProfileTask extends AsyncTask<String, Void, String> {
        ProgressDialog myPd_ring = null;
        @Override
        protected void onPreExecute() {

            myPd_ring  = new ProgressDialog (LoginActivity.this);
            myPd_ring.setMessage("Loading Profile...");
            myPd_ring.setCancelable(false);
            myPd_ring.setCanceledOnTouchOutside(false);
            myPd_ring.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String stat = null;

            HttpClient httpclient = new DefaultHttpClient();

            HttpEntity httpEntity = null;
            HttpPost httppost = new HttpPost(Utilities.url_getprofile);
            JSONObject jsonObject;

            try {
                List nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("user_roll", Utilities.username));
                nameValuePairs.add(new BasicNameValuePair("user_pass", Utilities.password));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                HttpResponse response = null;

                response = httpclient.execute(httppost);
                httpEntity = response.getEntity();
                String s = null;
                s = EntityUtils.toString(httpEntity);

                Log.e("ll", s);
                jsonObject = new JSONObject(s);
                if(jsonObject.getInt("status")==2)
                {
                    stat=String.valueOf(jsonObject.getInt("status"));
                    jsonObject=jsonObject.getJSONObject("data");
                    Utilities.credits_available=jsonObject.getInt("credits_available");
                    SharedPreferences.Editor editor = Utilities.sp.edit();
                    editor.putInt("credits_available", Utilities.credits_available);
                    editor.commit();

                    JSONArray jsonArray=jsonObject.getJSONArray("bets");
                    JSONObject j;
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        j=jsonArray.getJSONObject(i);
                        Log.e("******",String.valueOf(j));
                        int index=Utilities.eventMap.get(j.getInt("event_id"));
                        Utilities.events[index]._desc=j.getString("bet_desc");
                        Utilities.events[index]._status=j.getInt("bet_status");
                        Utilities.events[index]._credits=j.getInt("credits_bet");
                        try {
                            Utilities.events[index]._won = j.getInt("bet_won");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }


                    //TODO:Update the eventarray in the DB.
                }


                stat=String.valueOf(jsonObject.getInt("status"));

                Log.e("response", s);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ll",String.valueOf(e));


            }



            return stat;
        }

        @Override
        protected void onPostExecute(String stat) {
            super.onPostExecute(stat);
            myPd_ring.dismiss();
           // System.out.println("Error: " + );
            //myPd_ring.setMessage("Loading Profile");


            switch (stat) {

                case "2":
                    Intent intent = new Intent(getBaseContext(), BettingScreen.class);
                    startActivity(intent);
                    finish();
                    break;

            }
        }
    }


    class EventsTask extends AsyncTask<String, Void, String> {
        ProgressDialog myPd_ring = null;
        @Override
        protected void onPreExecute() {

            myPd_ring  = new ProgressDialog (LoginActivity.this);
            myPd_ring.setMessage("Loading Events...");
            myPd_ring.setCancelable(false);
            myPd_ring.setCanceledOnTouchOutside(false);
            myPd_ring.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String stat = null;

            HttpClient httpclient = new DefaultHttpClient();

            HttpEntity httpEntity = null;
            HttpPost httppost = new HttpPost(Utilities.url_getevents);
            JSONObject jsonObject;

            try {

                HttpResponse response = null;

                response = httpclient.execute(httppost);
                httpEntity = response.getEntity();
                String s = null;
                s = EntityUtils.toString(httpEntity);

                Log.e("ll", s);

                jsonObject = new JSONObject(s);
                Log.e("response", s);
                if(jsonObject.getInt("status")==2)
                {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    JSONObject j;
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        j=jsonArray.getJSONObject(i);
                        Utilities.events[i]=new Events(j.getInt("event_id"),j.getString("event_name"),"cluster",-1,0,"dept",0,j.getInt("event_status"));
                        Utilities.eventMap.put(j.getInt("event_id"),i);
                    }
                }
                stat = String.valueOf(jsonObject.getInt("status"));
                //error = jsonObject.getString("error");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ll",String.valueOf(e));


            }



            return stat;
        }

        @Override
        protected void onPostExecute(String status) {
            super.onPostExecute(status);
            myPd_ring.dismiss();
           // System.out.println("Error: " + error);
            //myPd_ring.setMessage("Loading Profile");


            if(status!=null) {
                switch (status) {

                    case "2":


                    default:

                        new ProfileTask().execute();

                }
            }
            else{
                Toast.makeText(LoginActivity.this,"Internet?",Toast.LENGTH_SHORT).show();
            }


        }
    }





}
