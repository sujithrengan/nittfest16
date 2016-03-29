package org.delta.nittfest;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

/**
 * Created by bharath on 29/3/16.
 */
public class DeptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int mode;
    int _Visibility;
    static final int TYPE_HEADER=1;
    static final int TYPE_FOOTER=3;
    static final int TYPE_DATA=2;

    public static final String KEY_TITLE = "Title";
    public static final String KEY_RANK = "rank";
       List<Map<String, String>> deptlist=null;

    private final ViewGroup.LayoutParams footerparams;

    Typeface t;
    Context context;




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

        public DataViewHolder(View v) {
            super(v);
            title=(TextView)v.findViewById(R.id.title);
            position=(TextView)v.findViewById(R.id.position);
            seekBar=(SeekBar)v.findViewById(R.id.seekbar);
            layout=(RelativeLayout)v.findViewById(R.id.expandedLayout);
            rootLayout=(CardView)v.findViewById(R.id.rootlayout);
            seektext = (TextView)v.findViewById(R.id.seektext);

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
    public DeptAdapter(Context context, int mode, List<Map<String, String>> deptlist,int Visibility) {

        this.deptlist=deptlist;
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
        if(position<deptlist.size())
            return  TYPE_DATA;

        else
            return TYPE_FOOTER;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mholder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(mholder instanceof DataViewHolder) {

            //Log.e("Recycle",String.valueOf(position));
            final DataViewHolder holder = (DataViewHolder)mholder;

            holder.title.setTypeface(t);
            holder.position.setTypeface(t);


            holder.title.setText(deptlist.get(position).get(KEY_TITLE));
            holder.position.setText(deptlist.get(position).get(KEY_RANK));


            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.rootLayout.getLayoutParams();
            params.bottomMargin = 0;
            params.topMargin = 0;
            _Visibility=1;
            holder.seekBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            holder.seektext.setText(String.valueOf(progress));
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    }
            );
            holder.rootLayout.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(_Visibility==1){
                                holder.layout.setVisibility(View.VISIBLE);
                                Log.e("in visible","visibility 1->2");
                                _Visibility=2;

                            }
                            else if(_Visibility==2){
                                holder.layout.setVisibility(View.GONE);
                                Log.e("in invisible", "visibility 2->1");
                                _Visibility=1;
                            }
                            else  holder.layout.setVisibility(View.GONE);
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

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mode==0)
            return 0;
        else
            return deptlist.size()+1;
    }


}
