package org.delta.nittfest;

import android.content.Intent;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;




public class MainActivity extends ActionBarActivity {

    DBController db;
    ListAdapter listAdapter;
    ListView scoreList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Utilities.sp=this.getSharedPreferences("pop",0);
        Utilities.locked=Utilities.sp.getInt("locked",0);
        db=new DBController(this);
        scoreList=(ListView)findViewById(R.id.scoreList);




        Utilities.init(db);
        new getScoresfromServer().execute();
        //ChartDisplay();

    }



    void setdepartments(String s)
    {


        //t.setText(s);
        JSONArray jsonArray= null;
        JSONObject jsonObject=null;
        try {
            jsonArray = new JSONArray(s);
            for(int i=0;i<jsonArray.length();i++)
            {
                    jsonObject=jsonArray.getJSONObject(i);
                Utilities.departments[i].SetDepartment(jsonObject.get("departmentName").toString(),Float.valueOf(jsonObject.get("score").toString()));

            }
            if(Utilities.locked==0)
                db.insertDepartment(Utilities.departments);
            else
                db.updateScores(Utilities.departments);

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
        Utilities.locked=1;

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
            if(Utilities.locked!=0)
            for(int i=0;i<Utilities.departments.length;i++)
            {
                Utilities.departments[i].old_position=i;
            }

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
