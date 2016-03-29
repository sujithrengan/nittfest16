package org.delta.nittfest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class BettingScreen extends Activity {

    DBController controller = new DBController(this);
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private EventAdapter mAdapter;
    List<Map<String, String>> sampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting_events);



        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        //mSwipeRefreshLayout.setColorScheme(R.color.color_scheme_1_1, R.color.color_scheme_1_2,
        //      R.color.color_scheme_1_3, R.color.color_scheme_1_4);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                //new getnotifsfromDB().execute();


                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

        sampleList = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put(EventAdapter.KEY_TITLE, "Dance");
        map.put(EventAdapter.KEY_CLUSTER, "dance");
        map.put(EventAdapter.KEY_CREDIT, "Place your bets");
        sampleList.add(map);
        mAdapter=new EventAdapter(BettingScreen.this,1,sampleList,0);
        mRecyclerView.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        //        scaleAdapter.setFirstOnly(false);
        //        scaleAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(scaleAdapter);




        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Utilities.strcolorsEvents[notifList.size()%Utilities.strcolorsEvents.length])));
        //getSupportActionBar().setTitle("Notifications");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_notify, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class AuthTask extends AsyncTask<String, Void, String> {
        ProgressDialog myPd_ring = null;
        @Override
        protected void onPreExecute() {

            myPd_ring  = new ProgressDialog (BettingScreen.this);
            myPd_ring.setMessage("Loading...");
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

                case 2:


                case 3:
                    Toast.makeText(BettingScreen.this, "Your account is not on the system. Please contact NITTFEST OC", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    }




}
