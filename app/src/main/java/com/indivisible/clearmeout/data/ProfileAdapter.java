package com.indivisible.clearmeout.data;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by indiv on 16/06/14.
 */
public class ProfileAdapter
        extends ArrayAdapter<Profile>
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private Context context;
    private List<Profile> profiles;

    private static final String TAG = "ProfileAdapter";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////


    public ProfileAdapter(Context context, List<Profile> profiles)
    {
        super(context, android.R.layout.two_line_list_item, profiles);
        this.context = context;
        this.profiles = profiles;
    }


    ///////////////////////////////////////////////////////
    ////    inflation
    ///////////////////////////////////////////////////////

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(android.R.layout.two_line_list_item, parent, false);

        TextView majorText = (TextView) rowView.findViewById(android.R.id.text1);
        TextView minorText = (TextView) rowView.findViewById(android.R.id.text2);
        majorText.setText(profiles.get(position).toString());
        minorText.setText(profiles.get(position).debugContent());
        return rowView;
    }

}
