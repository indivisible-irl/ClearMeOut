package com.indivisible.clearmeout.interval;

import android.content.Context;
import android.preference.Preference;

import com.indivisible.clearmeout.data.IntervalType;


public interface Interval
{

    public IntervalType getType();

    public Preference[] getPreferences(Context context);

}
