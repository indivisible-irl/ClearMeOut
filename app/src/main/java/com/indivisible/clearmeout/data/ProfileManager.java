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
        //this.context = context.getApplicationContext();
        this.context = context;
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
        if (profileId < 0)
        {
            return makeEmptyProfile();
        }
        try
        {
            Profile profile;
            profileSource.openReadable();
            profile = profileSource.getProfile(profileId);
            loadAll(profile);
            return profile;
        }
        catch (SQLException e)
        {
            Log.e(TAG, "(getProfile) Error reading database");
            e.printStackTrace();
            return makeEmptyProfile();
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
                loadAll(profile);
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

    public void loadAll(Profile profile)
    {
        if (profile.getId() < 0)
        {
            Log.e(TAG,
                    "(loadAll) Invalid profile id: " + profile.getId() + " / "
                            + profile.getName());
        }
        loadTarget(profile);
        loadFilters(profile);
        loadIntervals(profile);
    }

    public void loadTarget(Profile profile)
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
    }

    public void loadFilters(Profile profile)
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
    }

    public void loadIntervals(Profile profile)
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
    }


    ///////////////////////////////////////////////////////
    ////    util
    ///////////////////////////////////////////////////////

    public Profile makeEmptyProfile()
    {
        Profile emptyProfile = new Profile();
        emptyProfile.setName("INVALID PROFILE");
        Target emptyTarget = new Target();
        emptyProfile.setTarget(emptyTarget);
        return emptyProfile;
    }


    ///////////////////////////////////////////////////////
    ////    testing
    ///////////////////////////////////////////////////////

    public void loadTestData()
    {
        // profiles
        Profile p1 = new Profile("First", false);
        Profile p2 = new Profile("Second", true);
        Profile p3 = new Profile("Third", false);
        try
        {
            profileSource.openWriteable();
            p1 = profileSource.createProfile(p1);
            p2 = profileSource.createProfile(p2);
            p3 = profileSource.createProfile(p3);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            profileSource.close();
        }

        // targets
        Target t1 = new Target(p1.getId(), "/some/dir", true, false);
        Target t2 = new Target(p2.getId(), "/another/dir", false, true);
        try
        {
            targetSource.openWriteable();
            t1 = targetSource.createTarget(t1);
            t2 = targetSource.createTarget(t2);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            targetSource.close();
        }
    }
}
