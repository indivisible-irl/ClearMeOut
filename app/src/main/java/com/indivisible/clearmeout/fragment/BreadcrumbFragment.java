package com.indivisible.clearmeout.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
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

    private static final String SEP = " > ";
    private static final String KEY_TRAIL_ARRAY = "breadcrumb_trail_array";
    private static final String TAG = "bCrumbFrag";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////


    public static final BreadcrumbFragment newInstance(String initialCrumb)
    {
        String[] trailArray = new String[] {
            initialCrumb
        };
        return newInstance(trailArray);
    }

    public static final BreadcrumbFragment newInstance(String[] trailArray)
    {
        BreadcrumbFragment frag = new BreadcrumbFragment();
        if (trailArray != null)
        {
            Bundle args = new Bundle();
            args.putStringArray(KEY_TRAIL_ARRAY, trailArray);
            frag.setArguments(args);
        }
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        trailParts = new ArrayList<String>();
        Bundle args = getArguments();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_breadcrumbs, container);
        textViewTrail = (TextView) view.findViewById(R.id.bcrumb_text_trail);
        setTrailText();
        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
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

    private void setTrailText()
    {
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
    }
}
