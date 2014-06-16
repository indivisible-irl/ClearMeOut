package com.indivisible.clearmeout.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.Log;

/**
 * Created by indiv on 16/06/14.
 */
public class ProfileManager
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private List<Profile> profiles;
    private Context context;

    private ProfileSource profileSource;
    private TargetSource targetSource;
    private FilterSource filterSource;
    private IntervalSource intervalSource;

    private static final String TAG = "ProfileMgr";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public ProfileManager(Context context)
    {
        this.context = context.getApplicationContext();
        profileSource = new ProfileSource(context);
        targetSource = new TargetSource(context);
        filterSource = new FilterSource(context);
        intervalSource = new IntervalSource(context);
    }


    ///////////////////////////////////////////////////////
    ////    grab
    ///////////////////////////////////////////////////////

    public Profile getProfile(long profileId)
    {
        Profile profile;
        try
        {
            profileSource.openReadable();
            profile = profileSource.getProfile(profileId);
            return loadAll(profile);
        }
        catch (SQLException e)
        {
            Log.e(TAG, "(getProfile) Error reading database");
            e.printStackTrace();
            return new Profile();
        }
        finally
        {
            profileSource.close();
        }
    }

    public List<Profile> getAllProfiles()
    {
        List<Profile> profiles = new ArrayList<Profile>();
        try
        {
            profileSource.openReadable();
            profiles = profileSource.getAllProfiles();
            for (Profile profile : profiles)
            {
                profile = loadAll(profile);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            profileSource.close();
        }


        return profiles;
    }


    ///////////////////////////////////////////////////////
    ////    load
    ///////////////////////////////////////////////////////

    public Profile loadAll(Profile profile)
    {
        if (profile.getId() < 0)
        {
            Log.e(TAG,
                    "(loadAll) Invalid profile id: " + profile.getId() + " / "
                            + profile.getName());
        }
        profile = loadTarget(profile);
        profile = loadFilters(profile);
        profile = loadIntervals(profile);
        return profile;
    }

    public Profile loadTarget(Profile profile)
    {
        Target target;
        try
        {
            targetSource.openReadable();
            target = targetSource.getProfileTarget(profile.getId());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            target = new Target();
        }
        finally
        {
            targetSource.close();
        }
        profile.setTarget(target);
        return profile;
    }

    public Profile loadFilters(Profile profile)
    {
        List<Filter> filters;
        try
        {
            filterSource.openReadable();
            filters = filterSource.getProfileFilters(profile.getId());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            filters = new ArrayList<Filter>();
        }
        finally
        {
            filterSource.close();
        }
        profile.setFilters(filters);
        return profile;
    }

    public Profile loadIntervals(Profile profile)
    {
        List<Interval> intervals;
        try
        {
            intervalSource.openReadable();
            intervals = intervalSource.getProfileIntervals(profile.getId());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            intervals = new ArrayList<Interval>();
        }
        finally
        {
            intervalSource.close();
        }
        profile.setIntervals(intervals);
        return profile;
    }
}
