package org.delta.nittfest;

/**
 * Created by HP on 06-02-2016.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter{
    private final Context context;


    public ListAdapter(Context context) {



        this.context = context;

    }

    @Override
    public int getCount() {
       //return 0;
        return Utilities.departments.length;
    }

    @Override
    public Department getItem(int i) {
        return Utilities.departments[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView score = (TextView) rowView.findViewById(R.id.score);
        TextView departmentName = (TextView) rowView.findViewById(R.id.departmentName);
        score.setText(String.valueOf(Utilities.departments[position].score));
        departmentName.setText(Utilities.departments[position].name);


        //Log.e("adapter",Utilities.departments[position].name);

        return rowView;
    }
}
