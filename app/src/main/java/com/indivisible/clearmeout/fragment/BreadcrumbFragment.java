package com.indivisible.clearmeout.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.util.Utils;

/**
 * Created by indiv on 16/06/14.
 */
public class BreadcrumbFragment
        extends Fragment
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private String[] trailParts;
    private TextView textViewTrail;

    private static final String SEP = " > ";
    public static final String KEY_CRUMB_ARRAY = "breadcrumb_trail_array";
    private static final String TAG = "bCrumbFrag";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public static final BreadcrumbFragment newInstance(String singleCrumb)
    {
        return newInstance(new String[] {
            singleCrumb
        });
    }

    public static final BreadcrumbFragment newInstance(String[] parentCrumbs, String newCrumb)
    {
        String[] newCrumbs = Utils.appendToArray(parentCrumbs, newCrumb);
        return newInstance(newCrumbs);
    }

    public static final BreadcrumbFragment newInstance(String[] crumbArray)
    {
        BreadcrumbFragment frag = new BreadcrumbFragment();
        if (crumbArray != null)
        {
            Bundle args = new Bundle();
            args.putStringArray(KEY_CRUMB_ARRAY, crumbArray);
            frag.setArguments(args);
        }
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null)
        {
            String[] trailArray = args.getStringArray(KEY_CRUMB_ARRAY);
            if (trailArray != null)
            {
                trailParts = trailArray;
            }
        }
        if (trailParts == null)
        {
            Log.e(TAG, "Failed to extract array from bundle args");
            trailParts = new String[0];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_breadcrumbs, container, false);
        textViewTrail = (TextView) view.findViewById(R.id.bcrumb_text_trail);
        setTrailText();
        return view;
    }


    ///////////////////////////////////////////////////////
    ////    communication
    ///////////////////////////////////////////////////////

    //    public boolean goUp()
    //    {
    //        int size = trailParts.size();
    //        if (size < 1)
    //        {
    //            Log.e(TAG, "(goUp) Can't. Not enough crumbs: " + size);
    //            return false;
    //        }
    //        else
    //        {
    //            trailParts.remove(trailParts.size() - 1);
    //            setTrailText();
    //            return true;
    //        }
    //    }
    //
    //    public void next(String nextLevelTitle)
    //    {
    //        trailParts.add(nextLevelTitle);
    //        setTrailText();
    //    }
    //
    //    public int size()
    //    {
    //        return trailParts.size();
    //    }


    ///////////////////////////////////////////////////////
    ////    trail handling
    ///////////////////////////////////////////////////////

    public void setTrailText()
    {
        if (trailParts.length < 1)
        {
            Log.e(TAG, "(setText) No trailParts to use");
            textViewTrail.setText("No trail...");
        }
        else
        {
            StringBuilder sb = new StringBuilder(trailParts[0]);
            for (int i = 1; i < trailParts.length; i++)
            {
                sb.append(SEP).append(trailParts[i]);
            }
            textViewTrail.setText(sb.toString());
        }
    }
}
