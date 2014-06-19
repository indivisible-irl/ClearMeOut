package com.indivisible.clearmeout.interval;

//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import com.indivisible.clearmeout.R;
//import com.indivisible.clearmeout.data.IntervalType;
//
//
//public class IntervalUtil
//{
//
//    private static final IntervalType DEFAULT_TYPE = IntervalType.EveryXHours;
//    private static final String TAG = "IntervalUtil";
//
//    public static IntervalType calcIntervalType(Context context, CharSequence interval)
//    {
//        String intervalString = interval.toString();
//        Log.d(TAG, "IntervalString: " + intervalString);
//        if (intervalString.equals(context.getString(R.string.pref_interval_everyXMinutes_key)))
//        {
//            return IntervalType.EveryXMinutes;
//        }
//        else if (intervalString.equals(context
//                .getString(R.string.pref_interval_everyXHours_key)))
//        {
//            return IntervalType.EveryXHours;
//        }
//        else if (intervalString.equals(context
//                .getString(R.string.pref_interval_everyXDays_days_key)))
//        {
//            return IntervalType.EveryXDays;
//        }
//        else if (intervalString.equals(context.getString(R.string.pref_interval_daily_key)))
//        {
//            return IntervalType.Daily;
//        }
//        else if (intervalString.equals(context
//                .getString(R.string.pref_interval_weekly_day_key)))
//        {
//            return IntervalType.Weekly;
//        }
//        else if (intervalString.equals(context
//                .getString(R.string.pref_interval_onTheseWeekdays_days_key)))
//        {
//            return IntervalType.OnTheseWeekdays;
//        }
//        else if (intervalString.equals(context
//                .getString(R.string.pref_interval_onTheseDates_dates_key)))
//        {
//            return IntervalType.OnTheseDates;
//        }
//        else
//        {
//            Log.e(TAG, "calcType: Invalid/Unhandled String: " + intervalString);
//            return IntervalType.INVALID;
//        }
//    }
//
//    public static IntervalType getCurentIntervalType(Context context)
//    {
//        SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(context);
//        String intervalString = pm.getString(context
//                .getString(R.string.pref_interval_choice_key), null);
//        if (intervalString != null)
//        {
//            return calcIntervalType(context, intervalString);
//        }
//        else
//        {
//            Log.w(TAG, "No IntervalChoice saved, using default: " + DEFAULT_TYPE.name());
//            return DEFAULT_TYPE;
//        }
//    }
//}
