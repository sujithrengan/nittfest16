package org.delta.nittfest;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Notify extends ActionBarActivity {

    DBController controller = new DBController(this);
    List<Map<String, String>> sampleList;
    SampleAdapter sa;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;
    ArrayList<HashMap<String, String>> notifList;
    String notifs[];
    private String[] time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
       // Utilities.initcolours(this);

        final TextView em=(TextView) findViewById(R.id.empty);

        notifList = controller.getAllNotifs();
        if(notifList.size()>0)
        {
            em.setVisibility(View.INVISIBLE);
        }
        else
            em.setVisibility(View.VISIBLE);
        notifs=new String[notifList.size()];
        time=new String[notifList.size()];
        for(int i=0;i<notifList.size();i++)
        {
            notifs[i]="null";
            time[i]="null";
        }
        for(int i=0;i<notifList.size();i++)
        {
            notifs[i] = notifList.get(i).get("notifText");
            time[i]=notifList.get(i).get("time");

            Log.e("time",time[i]);

        }

			/*
			for(int i=0;i<notifList.size();i++)
			{
			    TableRow row=new TableRow(this);
			    TextView n=new TextView(this);
			    n.setText(""+notifs[i]);
			    row.addView(n);


			    table.addView(row);
			}*/


        Map<String, String> map;
        sampleList = new ArrayList<Map<String, String>>();
        sa=new SampleAdapter(this, R.layout.notif_item, sampleList);

        for (int i = notifList.size()-1; i >=0 ; i--) {
            map = new HashMap<String, String>();
            map.put(SampleAdapter.KEY_DEPT, notifs[i]);
            map.put(SampleAdapter.KEY_SCORE,time[i]);

            sampleList.add(map);
        }
        final ListAdapter listad=(ListAdapter) new SampleAdapter(this, R.layout.notif_item, sampleList);
        ListView listView = (ListView) findViewById(R.id.list_notif);
        listView.setAdapter(listad);


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

    class SampleAdapter extends ArrayAdapter<Map<String, String>> {

        public static final String KEY_ICON = "icon";
        public static final String KEY_COLOR = "color";
        public static final String KEY_DEPT = "dept";
        public static final String KEY_SCORE = "score";

        private final LayoutInflater mInflater;
        private final List<Map<String, String>> mData;

        public SampleAdapter(Context context, int layoutResourceId, List<Map<String, String>> data) {
            super(context, layoutResourceId, data);
            mData = data;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.notif_item, parent, false);
                //viewHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.image_view_icon);
                viewHolder.dept=(TextView)convertView.findViewById(R.id.notifText);
                viewHolder.score=(TextView)convertView.findViewById(R.id.notiftime);
                //viewHolder.score=(TextView)convertView.findViewById(R.id.score);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //viewHolder.imageViewIcon.setImageResource(mData.get(position).get(KEY_ICON));
            viewHolder.dept.setText(mData.get(position).get(KEY_DEPT));
            viewHolder.score.setText(mData.get(position).get(KEY_SCORE));
            //viewHolder.score.setText(String.valueOf(mData.get(position).get(KEY_SCORE)));
           /*TextView t= (TextView)convertView.findViewById(R.id.departmentName);
           TextView t2= (TextView)convertView.findViewById(R.id.score);

           t.setText(mData.get(position).get(KEY_DEPT));
           t2.setText(mData.get(position).get(KEY_SCORE));*/

           /* int[] colors = {
                    getApplicationContext().getResources().getColor(R.color.ColorSchedule),
                    getApplicationContext(). getResources().getColor(R.color.ColorProfile),
                    getApplicationContext().getResources().getColor(R.color.ColorEvents),

            };
            */
            //convertView.setBackgroundColor(Utilities.colours.get(position % 5));


            return convertView;
        }

        class ViewHolder {
            ImageView imageViewIcon;
            TextView dept;
            TextView score;
        }
    }



}
