package com.indivisible.clearmeout.activity;

import android.app.Activity;
import android.os.Bundle;

import com.indivisible.clearmeout.fragment.BreadcrumbFragment;
import com.indivisible.clearmeout.fragment.SaveCancelFragment;

/**
 * Created by indiv on 19/06/14.
 */
public class ChildListActivity
        extends Activity
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private String[] crumbs;

    BreadcrumbFragment breadcrumbFrag;
    SaveCancelFragment saveCancelFrag;

    private static final String THIS_CRUMB = "Child";
    private static final String TAG = "ChildListAct";

    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void extractIntentExtras()
    {
        Bundle extras = getIntent().getExtras();
    }
}
