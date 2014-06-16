package com.indivisible.clearmeout.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.indivisible.clearmeout.R;

/**
 * Created by indiv on 16/06/14.
 */
public class BreadcrumbFragment
        extends Fragment
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private List<String> trailParts;

    private TextView textViewTrail;

    private static final String START = "|> ";
    private static final String SEP = " > ";
    private static final String KEY_TRAIL_ARRAY = "breadcrumb_trail_array";
    private static final String TAG = "bCrumbFrag";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public static final BreadcrumbFragment newInstance(String[] trailArray)
    {
        Log.v(TAG, "newInstance...");
        BreadcrumbFragment frag = new BreadcrumbFragment();
        if (trailArray != null)
        {
            Bundle args = new Bundle();
            args.putStringArray(KEY_TRAIL_ARRAY, trailArray);
            frag.setArguments(args);
        }
        Log.v(TAG, "...returning instance");
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate (after super)...");
        trailParts = new ArrayList<String>();
        Bundle args = getArguments();
        Log.v(TAG, "...parsing args...");
        if (args != null)
        {
            String[] trailArray = args.getStringArray(KEY_TRAIL_ARRAY);
            if (trailArray != null)
            {
                for (String crumb : trailArray)
                {
                    trailParts.add(crumb);
                }
            }
        }
        Log.v(TAG, "...args parsed, end of onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.v(TAG, "creating view...");
        View view = inflater.inflate(R.layout.frag_breadcrumbs, container, false);
        textViewTrail = (TextView) view.findViewById(R.id.bcrumb_text_trail);
        setTrailText();
        Log.v(TAG, "...returning view");
        return view;
    }


    ///////////////////////////////////////////////////////
    ////    communication
    ///////////////////////////////////////////////////////

    public boolean goUp()
    {
        int size = trailParts.size();
        if (size < 1)
        {
            Log.e(TAG, "(goUp) Can't. Not enough crumbs: " + size);
            return false;
        }
        else
        {
            trailParts.remove(trailParts.size() - 1);
            setTrailText();
            return true;
        }
    }

    public void next(String nextLevelTitle)
    {
        trailParts.add(nextLevelTitle);
        setTrailText();
    }

    public int size()
    {
        return trailParts.size();
    }


    ///////////////////////////////////////////////////////
    ////    trail handling
    ///////////////////////////////////////////////////////

    public void setTrailText()
    {
        Log.v(TAG, "setting text...");
        if (trailParts.isEmpty())
        {
            Log.e(TAG, "(setText) No trailParts to use");
            textViewTrail.setText("No trail...");
        }
        else
        {
            StringBuilder sb = new StringBuilder(trailParts.get(0));
            for (int i = 1; i < trailParts.size(); i++)
            {
                sb.append(SEP).append(trailParts.get(i));
            }
            textViewTrail.setText(sb.toString());
        }
        Log.v(TAG, "...text set");
    }
}
