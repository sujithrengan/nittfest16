package org.delta.nittfest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class Notify extends Activity {

    DBController controller = new DBController(this);
    List<Map<String, String>> sampleList;
    ArrayList<HashMap<String, String>> notifList;
    String notifs[],titles[];
    private String[] time;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private NotifyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        final TextView em=(TextView) findViewById(R.id.empty);

        notifList = controller.getAllNotifs();
        if(notifList.size()>0)
        {
            em.setVisibility(View.INVISIBLE);
        }
        else
            em.setVisibility(View.VISIBLE);


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
                notifList = controller.getAllNotifs();
                if(notifList.size()>0)
                {
                    em.setVisibility(View.INVISIBLE);
                }
                else
                    em.setVisibility(View.VISIBLE);

                notifs=new String[notifList.size()];
                time=new String[notifList.size()];
                titles=new String[notifList.size()];
                for(int i=0;i<notifList.size();i++)
                {
                    notifs[i]="null";
                    time[i]="null";
                    titles[i] = "null";
                }
                for(int i=0;i<notifList.size();i++)
                {
                    notifs[i] = notifList.get(i).get("notifText");
                    time[i]=notifList.get(i).get("time");
                    titles[i] = notifList.get(i).get("title");
                    Log.e("time", time[i]);

                }

                sampleList = new ArrayList<Map<String, String>>();

                for (int i = notifList.size()-1; i >=0 ; i--) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(NotifyAdapter.KEY_TEXT, notifs[i]);
                    map.put(NotifyAdapter.KEY_TIME,time[i]);
                    map.put(NotifyAdapter.KEY_TITLE,titles[i]);
                    Log.e("notif", titles[i]);
                    sampleList.add(map);
                }


                mAdapter=new NotifyAdapter(Notify.this,1,sampleList);
                mRecyclerView.setItemAnimator(new FadeInAnimator());
                AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
                ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
                //        scaleAdapter.setFirstOnly(false);
                //        scaleAdapter.setInterpolator(new OvershootInterpolator());
                mRecyclerView.setAdapter(scaleAdapter);

                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

        notifs=new String[notifList.size()];
        time=new String[notifList.size()];
        titles=new String[notifList.size()];
        for(int i=0;i<notifList.size();i++)
        {
            notifs[i]="null";
            time[i]="null";
            titles[i] = "null";
        }
        for(int i=0;i<notifList.size();i++)
        {
            notifs[i] = notifList.get(i).get("notifText");
            time[i]=notifList.get(i).get("time");
            titles[i] = notifList.get(i).get("title");
            Log.e("time", time[i]);

        }

        sampleList = new ArrayList<Map<String, String>>();

        for (int i = notifList.size()-1; i >=0 ; i--) {
            Map<String, String> map = new HashMap<String, String>();
            map.put(NotifyAdapter.KEY_TEXT, notifs[i]);
            map.put(NotifyAdapter.KEY_TIME,time[i]);
            map.put(NotifyAdapter.KEY_TITLE,titles[i]);
            Log.e("notif", titles[i]);
            sampleList.add(map);
        }


        mAdapter=new NotifyAdapter(Notify.this,1,sampleList);
        mRecyclerView.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        //        scaleAdapter.setFirstOnly(false);
        //        scaleAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(scaleAdapter);

        mSwipeRefreshLayout.setRefreshing(false);

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


}
