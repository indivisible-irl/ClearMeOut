package com.indivisible.clearmeout.data;

/**
 * Created by indiv on 15/06/14.
 */
public class Interval
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private long id;
    private long fk_profile;
    private IntervalType intervalType;
    private boolean strictAlarm;
    private boolean active;
    private long lastRun;
    private String[] data;

    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public Interval()
    {
        this(-1L, IntervalType.INVALID, false, false, 0L, new String[0]);
    }

    public Interval(long parentProfileId, IntervalType intervalType, boolean isStrictAlarm,
            boolean isActive, long lastRunMillis, String[] data)
    {
        this(-1L, parentProfileId, intervalType, isStrictAlarm, isActive, lastRunMillis, data);
    }

    public Interval(long id, long parentProfileId, IntervalType intervalType,
            boolean isStrictAlarm, boolean isActive, long lastRunMillis, String[] data)
    {
        this.id = id;
        this.fk_profile = parentProfileId;
        this.intervalType = intervalType;
        this.strictAlarm = isStrictAlarm;
        this.active = isActive;
        this.lastRun = lastRunMillis;
        this.data = data;
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

    public long getParentProfileId()
    {
        return fk_profile;
    }

    public void setParentProfileId(long parentProfileId)
    {
        this.fk_profile = parentProfileId;
    }

    public IntervalType getIntervalType()
    {
        return intervalType;
    }

    public void setIntervalType(IntervalType intervalType)
    {
        this.intervalType = intervalType;
    }

    public boolean isStrictAlarm()
    {
        return strictAlarm;
    }

    public void setStrictAlarm(boolean isStrictAlarm)
    {
        this.strictAlarm = isStrictAlarm;
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


    ///////////////////////////////////////////////////////
    ////    util
    ///////////////////////////////////////////////////////

    @Override
    public String toString()
    {
        return id + ": " + intervalType.name();
    }

    public String debugContent()
    {
        return "p_" + fk_profile + " / s_" + strictAlarm + " / a_" + active + "\nr_" + lastRun
                + " / d_" + data.length;
    }
}
