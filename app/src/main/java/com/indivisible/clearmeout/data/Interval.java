package com.indivisible.clearmeout.data;

/**
 * Created by indiv on 15/06/14.
 */
public class Interval
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private int id;
    private int fk_profile;
    private IntervalType intervalType;
    private boolean strictAlarm;
    private boolean active;
    private long lastRun;
    private String[] data;

    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////


    public Interval(int id, int fk_profile, IntervalType intervalType, boolean strictAlarm,
            boolean active, long lastRun, String[] data)
    {
        this.id = id;
        this.fk_profile = fk_profile;
        this.intervalType = intervalType;
        this.strictAlarm = strictAlarm;
        this.active = active;
        this.lastRun = lastRun;
        this.data = data;
    }


    ///////////////////////////////////////////////////////
    ////    gets & sets
    ///////////////////////////////////////////////////////

    public int getId()
    {
        return id;
    }

    public int getParentProfileId()
    {
        return fk_profile;
    }

    public IntervalType getIntervalType()
    {
        return intervalType;
    }

    public void setIntervalType(IntervalType intervalType)
    {
        this.intervalType = intervalType;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean isActive)
    {
        this.active = isActive;
    }

    public long getLastRun()
    {
        return lastRun;
    }

    public void setLastRun(long lastRunMillis)
    {
        this.lastRun = lastRunMillis;
    }

    public String[] getData()
    {
        return this.data;
    }

    public void setData(String[] data)
    {
        this.data = data;
    }

}
