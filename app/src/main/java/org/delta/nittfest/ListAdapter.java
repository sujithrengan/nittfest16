package org.delta.nittfest;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HP on 19-02-2016.
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int mode;
    static final int TYPE_HEADER=1;
    static final int TYPE_FOOTER=3;
    static final int TYPE_DATA=2;
    private final ViewGroup.LayoutParams footerparams;

    Typeface t;
    Context context;




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class DataViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Dept;
        public TextView Score;
        public CardView rootLayout;

        public ImageView unread;
        public DataViewHolder(View v) {
            super(v);
            Dept = (TextView)v.findViewById(R.id.Dept);
            Score = (TextView)v.findViewById(R.id.Score);
            unread=(ImageView)v.findViewById(R.id.unreadview);
            rootLayout=(CardView)v.findViewById(R.id.rootlayout);


        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Footer;
        public FooterViewHolder(View v) {
            super(v);
            Footer = (TextView)v.findViewById(android.R.id.text1);

        }
    }
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Header;
        public CardView notif;
        public CardView bet;
        public CardView share;
        public HeaderViewHolder(View v) {
            super(v);
            Header = (TextView)v.findViewById(R.id.header_text);
            notif=(CardView)v.findViewById(R.id.notif);
            bet=(CardView)v.findViewById(R.id.bet);
            //share=(CardView)v.findViewById(R.id.share);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapter(Context context,int mode) {

        this.mode=mode;
        this.context=context;
        this.t=Typeface.createFromAsset(context.getAssets(),"fonts/hn.otf");
        this.footerparams=new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        if (viewType == TYPE_DATA) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            // set the view's size, margins, paddings and layout parameters

            DataViewHolder vh = new DataViewHolder(v);
            return vh;
        }
        else if(viewType==TYPE_HEADER)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_layout_score, parent, false);
            // set the view's size, margins, paddings and layout parameters

            HeaderViewHolder vh = new HeaderViewHolder(v);
            return vh;
        }
        else if(viewType==TYPE_FOOTER)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            // set the view's size, margins, paddings and layout parameters

            FooterViewHolder vh = new FooterViewHolder(v);
            return vh;

        }
        throw new RuntimeException("nomatch");
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return  TYPE_HEADER;
        else if(position<13)
           return  TYPE_DATA;

        else
            return TYPE_FOOTER;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mholder, final int posit) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(mholder instanceof DataViewHolder) {

            int position=posit-1;
            //Log.e("Recycle",String.valueOf(position));
            DataViewHolder holder = (DataViewHolder)mholder;
            holder.Score.setTypeface(t);
            holder.Dept.setTypeface(t);
            holder.Score.setText(String.valueOf(Utilities.departments[position].score));
            holder.Dept.setText(Utilities.departments[position].name);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.rootLayout.getLayoutParams();
            params.bottomMargin = 0;
            params.topMargin = 0;

            //if (position == 11)
              //  params.bottomMargin = 50;

            //if (position == 0)
                //params.topMargin = 25;


            holder.Dept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(context, "Dept", Toast.LENGTH_SHORT).show();
                }
            });

            holder.Score.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(context, "Score", Toast.LENGTH_SHORT).show();

                }
            });

        }

        else if(mholder instanceof FooterViewHolder) {
            FooterViewHolder holder =(FooterViewHolder)mholder;
            holder.Footer.setText(R.string.made_with);
            holder.Footer.setTypeface(t);
            //ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.Footer.getLayoutParams();
            //params.bottomMargin=0;
            holder.Footer.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
            holder.Footer.setPadding(0,0,0,10);
            holder.Footer.setTextSize(15f);


        }

        else if(mholder instanceof HeaderViewHolder) {
            HeaderViewHolder holder =(HeaderViewHolder)mholder;

            holder.Header.setTypeface(t);
            holder.bet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "bet", Toast.LENGTH_SHORT).show();
                    Utilities.events=new Events[20];
                    Utilities.eventMap=new HashMap<Integer, Integer>();
                    if(Utilities.status==0) {
                        Intent i = new Intent(context, LoginActivity.class);
                        context.startActivity(i);
                    }
                    else
                    {
                        //Toast.makeText(context,"BettingScreen",Toast.LENGTH_SHORT).show();
                        new EventsTask().execute();
                        //TODO:Take to Betting Screen
                    }

                }
            });
            holder.notif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,"notif",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(context,Notify.class);
                    context.startActivity(i);
                }
            });
            //ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.Footer.getLayoutParams();
            //params.bottomMargin=0;



        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mode==0)
            return 0;
        else
        return 14;
    }


    class EventsTask extends AsyncTask<String, Void, String> {
        ProgressDialog myPd_ring = null;
        @Override
        protected void onPreExecute() {

            myPd_ring  = new ProgressDialog (context);
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
    Toast.makeText(context,"Internet?",Toast.LENGTH_SHORT).show();
            }


        }
    }




    class ProfileTask extends AsyncTask<String, Void, String> {
        ProgressDialog myPd_ring = null;
        @Override
        protected void onPreExecute() {

            myPd_ring  = new ProgressDialog (context);
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

                    JSONArray jsonArray=jsonObject.getJSONArray("bet");
                    JSONObject j;
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        j=jsonArray.getJSONObject(i);
                        int index=Utilities.eventMap.get(j.getInt("event_id"));
                        Utilities.events[index]._desc=j.getString("bet_desc");
                        Utilities.events[index]._status=j.getInt("bet_status");
                        Utilities.events[index]._won=j.getInt("bet_won");
                        Utilities.events[index]._credits=j.getInt("bet_credits");
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
                    Intent intent = new Intent(context, BettingScreen.class);
                    context.startActivity(intent);
                    break;

            }
        }
    }


}
