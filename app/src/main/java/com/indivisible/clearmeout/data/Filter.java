package com.indivisible.clearmeout.data;

/**
 * Created by indiv on 15/06/14.
 */
public class Filter
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private long id;
    private long fk_profile;
    private FilterType filterType;
    private boolean active;
    private boolean whitelist;
    private String data;        //ASK: parse into List<String>?


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public Filter()
    {
        this(-1L, FilterType.INVALID, false, false, "NO DATA");
    }
    public Filter(long parentProfileId, FilterType filterType, boolean isActive,
            boolean isWhitelist, String data)
    {
        this(-1L, parentProfileId, filterType, isActive, isWhitelist, data);
    }

    public Filter(long id, long parentProfileId, FilterType filterType, boolean isActive,
            boolean isWhitelist, String data)
    {
        this.id = id;
        this.fk_profile = parentProfileId;
        this.filterType = filterType;
        this.active = isActive;
        this.whitelist = isWhitelist;
        this.data = data;
    }


    ///////////////////////////////////////////////////////
    ////    get & set
    ///////////////////////////////////////////////////////

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getParentProfileId()
    {
        return fk_profile;
    }

    public void setParentProfileId(long parentProfileId)
    {
        this.fk_profile = parentProfileId;
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
