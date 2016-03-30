package org.delta.nittfest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by HP on 19-02-2016.
 */
public class HelpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int mode;
    static final int TYPE_HEADER=1;
    static final int TYPE_FOOTER=5;
    static final int TYPE_DATA_SCORE=2;
    static final int TYPE_DATA_EVENTS=3;
    static final int TYPE_DATA_BET=4;
    static final int TYPE_HEADING=6;



    private final ViewGroup.LayoutParams footerparams;

    Typeface t;
    Context context;




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ScoreDataViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Dept;
        public TextView Score;
        public CardView rootLayout;

        public ImageView unread;
        public ScoreDataViewHolder(View v) {
            super(v);
            Dept = (TextView)v.findViewById(R.id.Dept);
            Score = (TextView)v.findViewById(R.id.Score);
            unread=(ImageView)v.findViewById(R.id.unreadview);
            rootLayout=(CardView)v.findViewById(R.id.rootlayout);


        }
    }
    public static class EventDataViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView cluster;
        public TextView credit;
        public CardView rootLayout;
        RelativeLayout layout;
        public TextView betin;
        public TextView betwon;
        public TextView betdesc;
        public ImageView imgbut;
        TextView help,help2;


        public EventDataViewHolder(View v) {
            super(v);
            title=(TextView)v.findViewById(R.id.title);
            credit=(TextView)v.findViewById(R.id.credit);
            cluster=(TextView)v.findViewById(R.id.cluster);
            layout=(RelativeLayout)v.findViewById(R.id.expandedLayout);
            rootLayout=(CardView)v.findViewById(R.id.rootlayout);
            betdesc=(TextView)v.findViewById(R.id.bet_desc);
            betwon=(TextView)v.findViewById(R.id.bet_won);
            betin=(TextView)v.findViewById(R.id.bet_in);
            imgbut = (ImageView)v.findViewById(R.id.stat_but);
            help = (TextView)v.findViewById(R.id.help);
            help2 = (TextView)v.findViewById(R.id.help2);



        }


    }
    public static class BetDataViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView position;
        public SeekBar seekBar;
        public TextView textView;
        TextView seektext;
        public CardView rootLayout;
        RelativeLayout layout,sub_layout;
        Button bet;

        public BetDataViewHolder(View v) {
            super(v);
            title=(TextView)v.findViewById(R.id.title);
            position=(TextView)v.findViewById(R.id.position);
            seekBar=(SeekBar)v.findViewById(R.id.seekbar);
            layout=(RelativeLayout)v.findViewById(R.id.expandedLayout);
            rootLayout=(CardView)v.findViewById(R.id.rootlayout);
            seektext = (TextView)v.findViewById(R.id.seektext);
            bet=(Button)v.findViewById(R.id.bet_button);
            textView = (TextView)v.findViewById(R.id.textview);
            sub_layout = (RelativeLayout)v.findViewById(R.id.sub_layout);

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
    public static class HeadingViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Footer;
        public HeadingViewHolder(View v) {
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
    public HelpAdapter(Context context, int mode) {


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

        if (viewType == TYPE_DATA_SCORE) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ScoreDataViewHolder vh = new ScoreDataViewHolder(v);
            return vh;
        }
        else if (viewType == TYPE_DATA_EVENTS) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_item, parent, false);
            // set the view's size, margins, paddings and layout parameters

            EventDataViewHolder vh = new EventDataViewHolder(v);
            return vh;
        }
       else if (viewType == TYPE_DATA_BET) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dept_betting, parent, false);
            // set the view's size, margins, paddings and layout parameters

            BetDataViewHolder vh = new BetDataViewHolder(v);
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
        else if (viewType == TYPE_HEADING){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            // set the view's size, margins, paddings and layout parameters

            HeadingViewHolder vh = new HeadingViewHolder(v);
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
        switch(position){
            case 0:return TYPE_HEADER;
            case 1:return TYPE_DATA_SCORE;
            case 3: return TYPE_DATA_EVENTS;
            case 5:return  TYPE_DATA_BET;
            case 6: return TYPE_FOOTER;
            case 2:
            case 4:return TYPE_HEADING;
        }

        return 0;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mholder, final int posit) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(mholder instanceof ScoreDataViewHolder) {

            int position=posit-1;
            //Log.e("Recycle",String.valueOf(position));
            ScoreDataViewHolder holder = (ScoreDataViewHolder)mholder;

            holder.Score.setTypeface(t);
            holder.Dept.setTypeface(t);
            holder.Score.setText(String.valueOf("Score"));
            holder.Dept.setText("Department");
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.rootLayout.getLayoutParams();
            params.bottomMargin = 0;
            params.topMargin = 0;

        }

        else if(mholder instanceof EventDataViewHolder){
            final EventDataViewHolder holder = (EventDataViewHolder)mholder;


                holder.layout.setVisibility(View.VISIBLE);
            //if(_Visibility==1)holder.layout.setVisibility(View.VISIBLE);
            //else
            //    holder.layout.setVisibility(View.GONE);
            holder.title.setTypeface(t);
            holder.cluster.setTypeface(t);
            holder.credit.setTypeface(t);

            holder.title.setText("Event Name");
            holder.credit.setText("Bet placed or not?");
           holder.help.setVisibility(View.VISIBLE);
            holder.help2.setVisibility(View.VISIBLE);
            holder.imgbut.setImageResource(R.drawable.circle);


            //Log.e("notif", eventList.get(position).get(KEY_TITLE));
            holder.cluster.setText("Event Cluster");
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.rootLayout.getLayoutParams();
            params.bottomMargin = 0;
            params.topMargin = 0;
            //_Visibility=1;
            holder.layout.setVisibility(View.VISIBLE);
            holder.betin.setText(String.valueOf("Bet you placed"));
            holder.betdesc.setText(String.valueOf("Dept you bet on "));
            holder.betwon.setText(String.valueOf("Credits won"));
            //Log.e("in visible", "visibility 1->2");
            //_Visibility = 2;


            //if (position == 11)
            //  params.bottomMargin = 50;
        }
        else if(mholder instanceof BetDataViewHolder) {

            //Log.e("Recycle",String.valueOf(position));
            final BetDataViewHolder holder = (BetDataViewHolder) mholder;



            holder.layout.setVisibility(View.VISIBLE);
            holder.title.setTypeface(t);
            holder.position.setTypeface(t);


            holder.title.setText("Dept");
            holder.position.setText("% of votes");


            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.rootLayout.getLayoutParams();
            params.bottomMargin = 0;
            params.topMargin = 0;

            int temp = Utilities.credits_available - 10;
            holder.seekBar.setMax(temp);

                holder.seekBar.setEnabled(true);
                holder.seekBar.setMax(1000);
                holder.textView.setText(String.valueOf("Slide to choose bet"));
                holder.sub_layout.setVisibility(View.VISIBLE);
                holder.bet.setEnabled(true);

            holder.seekBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            holder.seektext.setText(String.valueOf(progress + 10));

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    }
            );
            holder.layout.setVisibility(View.VISIBLE);
            holder.bet.setText("Click to set Bet");

        }
        else if(mholder instanceof FooterViewHolder) {
            FooterViewHolder holder =(FooterViewHolder)mholder;

            holder.Footer.setText(R.string.made_with);
            holder.Footer.setTypeface(t);
            //ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.Footer.getLayoutParams();
            //params.bottomMargin=0;
            holder.Footer.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            holder.Footer.setPadding(0,0,0,10);
            holder.Footer.setTextSize(15f);


        }
        else if(mholder instanceof HeadingViewHolder) {
            HeadingViewHolder holder =(HeadingViewHolder)mholder;
            holder.Footer.setTextSize(23f);
            if(posit==2)holder.Footer.setText("Event Select Card");
            else holder.Footer.setText("Dept Select Card");
            holder.Footer.setTypeface(t);
            //ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.Footer.getLayoutParams();
            //params.bottomMargin=0;
            holder.Footer.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
            holder.Footer.setPadding(0, 0, 0, 10);



        }
        else if(mholder instanceof HeaderViewHolder) {
            HeaderViewHolder holder =(HeaderViewHolder)mholder;

            holder.Header.setTypeface(t);
            holder.Header.setText("LeaderBoard Card");
            holder.Header.setTextSize(23f);
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
        return 7;
    }
}
