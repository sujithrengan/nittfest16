package org.delta.nittfest;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineChart chart = (LineChart) findViewById(R.id.chart);
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

        return super.onOptionsItemSelected(item);
    }
}
