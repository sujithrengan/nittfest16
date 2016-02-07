package org.delta.nittfest;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

    DBController db;
    String regid = new String();
    ListAdapter listAdapter;
    public GoogleCloudMessaging gcm;
    AsyncTask<Void, Void, Void> mRegisterTask;
    ListView scoreList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreList=(ListView)findViewById(R.id.scoreList);

        Utilities.sp=this.getSharedPreferences("pop",0);
        Utilities.locked=Utilities.sp.getInt("locked",0);
        Utilities.gcm_regid=Utilities.sp.getString("gcm_regid","null");

        db=new DBController(this);
        if(Utilities.gcm_regid.equals("null"))
        {
                gcmreg();
        }





        new getScoresfromServer().execute();
        //ChartDisplay();

    }
    public void gcmreg()
    {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register("835229264934");
                    msg = regid;
                    pregister(MainActivity.this, regid);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.e("gcm_status", msg);
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    void pregister(final Context context, final String regId) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                Log.e("gcm_status", "registering device (regId = " + regId + ")");
                String serverUrl = Utilities.GCM_URL;
                Map<String, String> paramss = new HashMap<String, String>();
                paramss.put("gcm_id", regId);
                for (int i = 1; i <= 1; i++) {
                    Log.e("gcm_status", "Attempt #" + i + " to register");
                    try {
                        post(serverUrl, paramss);
                        msg="registered";
                        return msg;
                    } catch (IOException e) {
                        Log.e("gcm_status", "Failed to register on attempt " + i + ":" + e);
                        msg="null";
                    }
                }
                return msg;
            }
            @Override
            protected void onPostExecute(String msg) {

                SharedPreferences.Editor editor = Utilities.sp.edit();
                editor.putString("gcm_regid", msg);
                editor.apply();
            }
        }.execute(null, null, null);
     }

    private void post(String endpoint, Map<String, String> params)
            throws IOException {

        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        byte[] bytes = body.getBytes();
        Log.e("gcm_status", "Posting '" + body + "' to " + url);
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    Log.e("check", line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            out.close();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    void setdepartments(String s)
    {

           Department dp[]=new Department[12];
        //t.setText(s);
        JSONArray jsonArray= null;
        JSONObject jsonObject=null;
        try {
            jsonArray = new JSONArray(s);
            for(int i=0;i<jsonArray.length();i++)
            {
                    jsonObject=jsonArray.getJSONObject(i);
                dp[i]=new Department(jsonObject.get("departmentName").toString(),Float.valueOf(jsonObject.get("score").toString()));
                if(Utilities.locked==0)
                db.insertDepartment(dp[i]);
                else
                    db.updateScores(dp[i]);
            }
            Utilities.departments=dp;
            Utilities.sortScores();

        } catch (JSONException e) {
            e.printStackTrace();
        }



        //if Utilities.locked==0
        Utilities.locked=1;
        SharedPreferences.Editor editor = Utilities.sp.edit();
        editor.putInt("locked", 1);
        editor.apply();

        Toast.makeText(MainActivity.this,"Updated :D",Toast.LENGTH_SHORT).show();
        showscore();
    }



    void showscore()
    {
        listAdapter=new ListAdapter(MainActivity.this);
        scoreList.setAdapter(listAdapter);

        scoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this,Utilities.departments[i].name,Toast.LENGTH_SHORT).show();
            }
        });

    }

    void oldscores()
    {
            Toast.makeText(MainActivity.this,"Failed to update :/",Toast.LENGTH_SHORT).show();
            Utilities.departments=db.getAllScores();
            Utilities.sortScores();
            showscore();
    }


    class getScoresfromServer extends AsyncTask<Void,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
            if(!s.equals("bleh"))
            setdepartments(s);
            else {
                if(Utilities.locked!=0)
                oldscores();
                else
                    Toast.makeText(MainActivity.this,"Internet?",Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected String doInBackground(Void... voids) {

            String res="bleh";
            HttpClient httpClient=new DefaultHttpClient();
            HttpEntity httpEntity=null;
            HttpPost httpPost=new HttpPost(Utilities.url_scores);


            try {
                HttpResponse response=httpClient.execute(httpPost);
                httpEntity=response.getEntity();
                res= EntityUtils.toString(httpEntity);

                Log.e("Async", "inside");

            } catch (IOException e) {
                Log.e("Async", "catched");
                e.printStackTrace();
            }
            return res;
        }
    }

    void ChartDisplay()
    {
        LineChart chart =null;//= (LineChart) findViewById(R.id.chart);
        ArrayList<Entry> v1=new ArrayList<Entry>();
        ArrayList<Entry> v2=new ArrayList<Entry>();
        v1.add(new Entry(100.000f,1));
        v1.add(new Entry(50.000f,0));
        v1.add(new Entry(155.300f,2));
        v1.add(new Entry(230.000f,3));

        v2.add(new Entry(160.000f,1));
        v2.add(new Entry(110.000f,0));

        v2.add(new Entry(155.300f,2));
        v2.add(new Entry(205.000f,3));
        LineDataSet s1=new LineDataSet(v1,"Department1");
        LineDataSet s2=new LineDataSet(v2,"Department2");

        s1.setColor(Color.parseColor("#ffffff"));
        s1.enableDashedLine(10.000f,7.000f,0);
        s1.setCircleColor(Color.parseColor("#000000"));
        s2.setCircleColor(Color.parseColor("#ffffff"));
        s2.setColor(Color.parseColor("#000000"));
        s2.setCircleColorHole(Color.parseColor("#000000"));
        s1.setAxisDependency(YAxis.AxisDependency.LEFT);

        s2.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dsets=new ArrayList<ILineDataSet>();
        dsets.add(s1);
        dsets.add(s2);
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Day0"); xVals.add("Day1");
        xVals.add("Day2");
        xVals.add("Day3");

        LineData ldata=new LineData(xVals,dsets);
        chart.setData(ldata);
        chart.invalidate();

        chart.animateY(1000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_refresh) {


            new getScoresfromServer().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
