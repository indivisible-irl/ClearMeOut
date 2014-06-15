package com.indivisible.clearmeout.data;

/**
 * Created by indiv on 15/06/14.
 */
public class Profile
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private long id = -1;
    private String name;
    private boolean active;

    //private Target target;
    //private List<Interval> intervals;
    //private List<Filter> filters;


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public Profile()
    {
        this("NO NAME", false);
    }

    public Profile(String profileName, boolean isActive)
    {
        this(-1, profileName, isActive);
    }

    public Profile(long id, String profileName, boolean isActive)
    {
        this.id = id;
        this.name = profileName;
        this.active = isActive;

        //TODO: just grab as required instead of always
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


    ///////////////////////////////////////////////////////
    ////    gets & sets
    ///////////////////////////////////////////////////////

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean isActive)
    {
        this.active = active;
    }

    //TODO: target, filters, intervals
}
