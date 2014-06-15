package com.indivisible.clearmeout.data;

import java.util.List;

/**
 * Created by indiv on 15/06/14.
 */
public class Profile
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private int id;
    private String name;
    private boolean active;

    private Target target;
    private List<Interval> intervals;
    private List<Filter> filters;


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public Profile(int id, String name, boolean isActive)
    {
        this.id = id;
        this.name = name;
        this.active = isActive;

        refreshTarget();
        refreshIntervals();
        refreshFilters();
    }

    public void refreshTarget()
    {
        //TODO: grab target from db
    }

    public void refreshIntervals()
    {
        //TODO: grab intervals from db
    }

    public void refreshFilters()
    {
        //TODO: grab filters from db
    }
}
