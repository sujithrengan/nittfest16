package org.delta.nittfest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by bharath on 30/3/16.
 */
public class Help_Activity extends Activity{
    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;
    private HelpAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);



        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);






        //sampleList = new ArrayList<Events>();
//TODO: set Utilities.events from DB(To account for network issues)
        mAdapter=new HelpAdapter(Help_Activity.this,1);

        mRecyclerView.setAdapter(mAdapter);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Utilities.strcolorsEvents[notifList.size()%Utilities.strcolorsEvents.length])));
        //getSupportActionBar().setTitle("Notifications");

    }



}
