package com.indivisible.clearmeout.data;

/**
 * Created by indiv on 15/06/14.
 */
public class Filter
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private int id;
    private int fk_profile;
    private FilterType filterType;
    private boolean active;
    private boolean whitelist;
    private String data;        //ASK: parse into List<String>?


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public Filter(int id, int fk_profile, FilterType filterType, boolean isActive,
            boolean isWhitelist, String data)
    {
        this.id = id;
        this.fk_profile = fk_profile;
        this.filterType = filterType;
        this.active = isActive;
        this.whitelist = isWhitelist;
        this.data = data;
    }


    ///////////////////////////////////////////////////////
    ////    get & set
    ///////////////////////////////////////////////////////

    public int getId()
    {
        return id;
    }

    public int getParentProfileId()
    {
        return fk_profile;
    }

    public FilterType getFilterType()
    {
        return filterType;
    }

    public void setFilterType(FilterType filterType)
    {
        this.filterType = filterType;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean isActive)
    {
        this.active = isActive;
    }

    public boolean isWhitelist()
    {
        return whitelist;
    }

    public void setWhitelist(boolean isWhitelist)
    {
        this.whitelist = isWhitelist;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }


}
