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

    private long id = -1;
    private String name;
    private boolean active;

    private Target target;
    private List<Interval> intervals;
    private List<Filter> filters;


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

    public Target getTarget()
    {
        return this.target;
    }

    public void setTarget(Target target)
    {
        this.target = target;
    }

    public List<Filter> getFilters()
    {
        return this.filters;
    }

    public void setFilters(List<Filter> filters)
    {
        this.filters = filters;
    }

    public List<Interval> getIntervals()
    {
        return this.intervals;
    }

    public void setIntervals(List<Interval> intervals)
    {
        this.intervals = intervals;
    }
}
