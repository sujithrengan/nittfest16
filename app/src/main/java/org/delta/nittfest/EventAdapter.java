package org.delta.nittfest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 19-02-2016.
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int mode;
    int _Visibility;
    static final int TYPE_HEADER=1;
    static final int TYPE_FOOTER=3;
    static final int TYPE_DATA=2;

    //List<Events> eventList=null;

    int vis_pos=-1;
    int vis_count=0;
    int picked_eventid;
    private final ViewGroup.LayoutParams footerparams;

    Typeface t;
    Context context;




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class DataViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView cluster;
        public TextView credit;
        public CardView rootLayout;
        RelativeLayout layout;

        public DataViewHolder(View v) {
            super(v);
            title=(TextView)v.findViewById(R.id.title);
            credit=(TextView)v.findViewById(R.id.credit);
            cluster=(TextView)v.findViewById(R.id.cluster);
            layout=(RelativeLayout)v.findViewById(R.id.expandedLayout);
            rootLayout=(CardView)v.findViewById(R.id.rootlayout);


        }


    }


    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Header;
        public CardView goback;
        public HeaderViewHolder(View v) {
            super(v);
            Header = (TextView)v.findViewById(R.id.header_text);
            goback=(CardView)v.findViewById(R.id.goback);

            //share=(CardView)v.findViewById(R.id.share);

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

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventAdapter(Context context, int mode,int Visibility) {

        //this.eventList=eventList;
        this._Visibility = Visibility;
        this.mode=mode;
        this.context=context;
        this.t=Typeface.createFromAsset(context.getAssets(),"fonts/hn.otf");
        this.footerparams=new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //for(int i=0;i<eventList.size();i++)
          //  Log.e("evelist",eventList.get(i)._name);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        if (viewType == TYPE_DATA) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_item, parent, false);
            // set the view's size, margins, paddings and layout parameters

            DataViewHolder vh = new DataViewHolder(v);
            return vh;
        }
        else if(viewType==TYPE_HEADER)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_layout_event, parent, false);
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

        if (position==0)
            return TYPE_HEADER;

        else if(position<21)
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

            final int position=posit-1;
            //Log.e("Recycle",String.valueOf(position));
            final DataViewHolder holder = (DataViewHolder)mholder;
            holder.layout.setVisibility(View.GONE);
            if(vis_pos==posit)
                holder.layout.setVisibility(View.GONE);
            //if(_Visibility==1)holder.layout.setVisibility(View.VISIBLE);
            //else
            //    holder.layout.setVisibility(View.GONE);
            holder.title.setTypeface(t);
            holder.cluster.setTypeface(t);
            holder.credit.setTypeface(t);

            holder.title.setText(Utilities.events[position]._name);
            if(Utilities.events[position]._status==-1)
            holder.credit.setText("Place your bets");
            else
                holder.credit.setText("Bets placed");
            //Log.e("notif", eventList.get(position).get(KEY_TITLE));
            holder.cluster.setText(Utilities.events[position]._cluster);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.rootLayout.getLayoutParams();
            params.bottomMargin = 0;
            params.topMargin = 0;
            //_Visibility=1;
            holder.rootLayout.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            //TODO:Check bet_status and decide
                            if (Utilities.events[position]._status!=-1) {
                                vis_pos=posit;

                                if (vis_count%2==0) {
                                    holder.layout.setVisibility(View.VISIBLE);
                                    //Log.e("in visible", "visibility 1->2");
                                    //_Visibility = 2;

                                } else {
                                    holder.layout.setVisibility(View.GONE);
                                    //Log.e("in invisible", "visibility 2->1");
                                    //_Visibility = 1;
                                }
                                vis_count++;
                            }

                            else
                            {
                                holder.layout.setVisibility(View.GONE);
                                picked_eventid=Utilities.events[position]._id;
                                //call distribution.
                                new StatisticsTask().execute();
                                //Intent i=new Intent(context,DeptBetting.class);
                                //context.startActivity(i);
                            }
                        }

                    }
            );
            //if (position == 11)
            //  params.bottomMargin = 50;

            //if (position == 0)
            //  params.topMargin = 25;


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
            //TODO:Set the credit amount here
            holder.Header.setText("Credits:"+String.valueOf(Utilities.credits_available));
            holder.goback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "bet", Toast.LENGTH_SHORT).show();
                    ((Activity) context).finish();

                }
            });

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mode==0)
            return 0;
        else
            return 22;
    }



    class StatisticsTask extends AsyncTask<String, Void, String> {
        ProgressDialog myPd_ring = null;
        @Override
        protected void onPreExecute() {

            myPd_ring  = new ProgressDialog (context);
            myPd_ring.setMessage("Loading Statistics...");
            myPd_ring.setCancelable(false);
            myPd_ring.setCanceledOnTouchOutside(false);
            myPd_ring.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String stat = null;

            HttpClient httpclient = new DefaultHttpClient();

            HttpEntity httpEntity = null;
            HttpPost httppost = new HttpPost(Utilities.url_getdistribution);
            JSONObject jsonObject;

            try {
                List nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("event_id",String.valueOf(picked_eventid)));
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
                    for(int i=0;i<12;i++)
                    {
                        Utilities.departments[i].votes=jsonObject.getInt(Utilities.departments[i].name);
                    }

                }




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


            if(stat!=null) {
                switch (stat) {

                    case "2":
                        Intent intent = new Intent(context, DeptBetting.class);
                        intent.putExtra("event_id",picked_eventid);
                        context.startActivity(intent);
                        break;

                }
            }
            else{
                Toast.makeText(context, "Internet?", Toast.LENGTH_SHORT).show();
            }

        }
    }



}
