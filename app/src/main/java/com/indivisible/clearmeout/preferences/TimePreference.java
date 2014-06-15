package com.indivisible.clearmeout.preferences;

/**
 * This class borrowed from CommonsWare's
 * (http://stackoverflow.com/users/115145/commonsware) solution to
 * "http://stackoverflow.com/questions/5533078/timepicker-in-preferencescreen"
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

public class TimePreference
        extends DialogPreference
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private int lastHour = 0;
    private int lastMinute = 0;
    private String formatSummary;
    private TimePicker picker = null;

    private static final String DEFAULT_TIME = "02:00";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public TimePreference(Context ctxt)
    {
        this(ctxt, null);
    }

    public TimePreference(Context ctxt, AttributeSet attrs)
    {
        this(ctxt, attrs, android.R.attr.preferenceStyle);
    }

    public TimePreference(Context ctxt, AttributeSet attrs, int defStyle)
    {
        super(ctxt, attrs, defStyle);
        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
        setDefaultValue(DEFAULT_TIME);
    }


    ///////////////////////////////////////////////////////
    ////    gets & sets
    ///////////////////////////////////////////////////////

    public static int getHour(String time)
    {
        String[] pieces = time.split(":");
        return (Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time)
    {
        String[] pieces = time.split(":");
        return (Integer.parseInt(pieces[1]));
    }

    private void setFormatSummary(String summary)
    {
        formatSummary = summary;
    }

    @Override
    public void setSummary(CharSequence summary)
    {
        String summaryString = summary.toString();
        if (summaryString.indexOf("%$") != -1)
        {
            setFormatSummary(summaryString);
        }
        super.setSummary(summary);
    }

    @Override
    public CharSequence getSummary()
    {
        if (formatSummary != null)
        {
            return String.format(formatSummary, this.toString());
        }
        return super.getSummary();
    }

    @Override
    public String toString()
    {
        return String.format("%02d", lastHour) + ":" + String.format("%02d", lastMinute);
    }


    ///////////////////////////////////////////////////////
    ////    dialog
    ///////////////////////////////////////////////////////

    @Override
    protected View onCreateDialogView()
    {
        picker = new TimePicker(getContext());
        picker.setIs24HourView(true);
        picker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        return (picker);
    }

    @Override
    protected void onBindDialogView(View v)
    {
        super.onBindDialogView(v);

        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult)
    {
        super.onDialogClosed(positiveResult);

        if (positiveResult)
        {
            lastHour = picker.getCurrentHour();
            lastMinute = picker.getCurrentMinute();

            String time = String.valueOf(lastHour) + ":" + String.valueOf(lastMinute);

            if (callChangeListener(time))
            {
                persistString(time);
            }
        }
    }


    ///////////////////////////////////////////////////////
    ////    persistence
    ///////////////////////////////////////////////////////

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index)
    {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue)
    {
        String time = null;

        if (restoreValue)
        {
            if (defaultValue == null)
            {
                time = getPersistedString("00:00");
            }
            else
            {
                time = getPersistedString(defaultValue.toString());
            }
        }
        else
        {
            time = defaultValue.toString();
        }

        lastHour = getHour(time);
        lastMinute = getMinute(time);
    }


}
