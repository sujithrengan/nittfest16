package org.delta.nittfest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HP on 19-02-2016.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final int mode;
    Typeface t;
    Context context;


    public static Date EpochConvert(String date)
    {
        return new Date(Long.parseLong(date));
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Dept;
        public TextView Score;

        public ImageView unread;
        public ViewHolder(View v) {
            super(v);
            Dept = (TextView)v.findViewById(R.id.Dept);
            Score = (TextView)v.findViewById(R.id.Score);
            unread=(ImageView)v.findViewById(R.id.unreadview);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapter(Context context,int mode) {

        this.mode=mode;
        this.context=context;
        this.t=Typeface.createFromAsset(context.getAssets(),"fonts/hn.otf");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.Score.setTypeface(t);
        holder.Dept.setTypeface(t);
        holder.Score.setText(String.valueOf(Utilities.departments[position].score));
        holder.Dept.setText(Utilities.departments[position].name);

        holder.Dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        holder.Score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                            }
        });



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mode==0)
            return 0;
        else
        return 12;
    }
}
