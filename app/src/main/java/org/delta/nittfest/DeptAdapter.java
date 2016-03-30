package org.delta.nittfest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

/**
 * Created by bharath on 29/3/16.
 */
public class DeptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int mode;
    final int event_id;
    int _Visibility;
    static final int TYPE_HEADER=1;
    static final int TYPE_FOOTER=3;
    static final int TYPE_DATA=2;

    public static final String KEY_TITLE = "Title";
    public static final String KEY_RANK = "rank";


    private final ViewGroup.LayoutParams footerparams;

    Typeface t;
    Context context;
    private String picked_dept;
    private String picked_credit;
    private int vis_pos=-1;
    private int vis_count=0;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class DataViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView position;
        public SeekBar seekBar;
        TextView seektext;
        public CardView rootLayout;
        RelativeLayout layout;
        Button bet;

        public DataViewHolder(View v) {
            super(v);
            title=(TextView)v.findViewById(R.id.title);
            position=(TextView)v.findViewById(R.id.position);
            seekBar=(SeekBar)v.findViewById(R.id.seekbar);
            layout=(RelativeLayout)v.findViewById(R.id.expandedLayout);
            rootLayout=(CardView)v.findViewById(R.id.rootlayout);
            seektext = (TextView)v.findViewById(R.id.seektext);
            bet=(Button)v.findViewById(R.id.bet_button);

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
        public CardView goback;
        public HeaderViewHolder(View v) {
            super(v);
            Header = (TextView)v.findViewById(R.id.header_text);
            goback=(CardView)v.findViewById(R.id.goback);

            //share=(CardView)v.findViewById(R.id.share);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DeptAdapter(Context context,int event_id, int mode,int Visibility) {

        this.event_id=event_id;
        this._Visibility = Visibility;
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
                    .inflate(R.layout.dept_betting, parent, false);
            // set the view's size, margins, paddings and layout parameters

            DataViewHolder vh = new DataViewHolder(v);
            return vh;
        }
        else if(viewType==TYPE_HEADER)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_layout_notif, parent, false);
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

        final int position=posit-1;
        if(mholder instanceof DataViewHolder) {

            //Log.e("Recycle",String.valueOf(position));
            final DataViewHolder holder = (DataViewHolder)mholder;

            holder.layout.setVisibility(View.GONE);
            if(vis_pos==posit)
                holder.layout.setVisibility(View.VISIBLE);
            holder.title.setTypeface(t);
            holder.position.setTypeface(t);


            holder.title.setText(Utilities.departments[position].name);
            holder.position.setText(Utilities.departments[position].votes+"%");


            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.rootLayout.getLayoutParams();
            params.bottomMargin = 0;
            params.topMargin = 0;
            _Visibility=1;

            holder.seekBar.setMax(Utilities.credits_available-10);
            holder.seekBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            holder.seektext.setText(String.valueOf(progress+10));
                            picked_credit=String.valueOf(progress+10);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    }
            );

            holder.bet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new PlaceBetTask().execute();
                    //Log.e("SubmitBet","*"+Utilities.username+"."+Utilities.password+"."+(holder.seekBar.getProgress()+10)+"."+picked_dept+"."+event_id+"*");
                }
            });
            holder.rootLayout.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            vis_pos=posit;

                            picked_dept=Utilities.departments[position].name;
                            if (vis_count%2==0) {
                                holder.layout.setVisibility(View.VISIBLE);
                                //Log.e("in visible", "visibility 1->2");
                                //_Visibility = 2;

                            } else {
                                vis_pos=-1;
                                vis_count=-1;
                                holder.layout.setVisibility(View.GONE);
                                //Log.e("in invisible", "visibility 2->1");
                                //_Visibility = 1;
                            }
                            vis_count++;
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
            holder.Header.setText("pick a department");
            holder.goback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "bet", Toast.LENGTH_SHORT).show();
                    ((Activity)context).finish();

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
            return 14;
    }



    class PlaceBetTask extends AsyncTask<String, Void, String> {
        ProgressDialog myPd_ring = null;
        @Override
        protected void onPreExecute() {

            myPd_ring  = new ProgressDialog (context);
            myPd_ring.setMessage("Placing Bet...");
            myPd_ring.setCancelable(false);
            myPd_ring.setCanceledOnTouchOutside(false);
            myPd_ring.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String stat = null;

            HttpClient httpclient = new DefaultHttpClient();

            HttpEntity httpEntity = null;
            HttpPost httppost = new HttpPost(Utilities.url_placebet);
            JSONObject jsonObject;

            try {
                List nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("event_id",String.valueOf(event_id)));
                nameValuePairs.add(new BasicNameValuePair("user_roll", Utilities.username));
                nameValuePairs.add(new BasicNameValuePair("user_pass", Utilities.password));
                nameValuePairs.add(new BasicNameValuePair("credit", picked_credit));
                nameValuePairs.add(new BasicNameValuePair("dept", picked_dept));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                HttpResponse response = null;

                response = httpclient.execute(httppost);
                httpEntity = response.getEntity();
                String s = null;
                s = EntityUtils.toString(httpEntity);

                Log.e("Result", s);
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



            return "2";//TODO: change raw return from API side remove dept put for debug.
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

                        Toast.makeText(context, "Bet Placed xD", Toast.LENGTH_LONG).show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // do something
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
                        }, 1000);

                        break;

                    case "3":
                        Toast.makeText(context,"Invalid bet",Toast.LENGTH_LONG);
                        ((Activity)context).finish();

                }
            }
            else{
                //TODO:Temporary FIX for noresponse from server for this APIcall

                Toast.makeText(context, "Internet?", Toast.LENGTH_SHORT).show();
            }

        }
    }



}
