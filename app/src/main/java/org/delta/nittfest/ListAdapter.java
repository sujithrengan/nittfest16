package org.delta.nittfest;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

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
                    Toast.makeText(context, "bet", Toast.LENGTH_SHORT).show();
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
}
