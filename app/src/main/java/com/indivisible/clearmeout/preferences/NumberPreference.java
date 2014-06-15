package com.indivisible.clearmeout.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;


public class NumberPreference
        extends DialogPreference
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private int lastValue = -1;
    private String formatSummary = null;
    private NumberPicker picker = null;


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public NumberPreference(Context context)
    {
        this(context, null);
    }

    public NumberPreference(Context context, AttributeSet attrs)
    {
        this(context, attrs, android.R.attr.preferenceStyle);
    }

    public NumberPreference(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }


    ///////////////////////////////////////////////////////
    ////    gets & sets
    ///////////////////////////////////////////////////////

    private void setFormatSummary(String summary)
    {
        formatSummary = summary;
    }

    public void refreshSummary()
    {
        setSummary(getSummary());
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
        return String.valueOf(lastValue);
    }


    ///////////////////////////////////////////////////////
    ////    dialog
    ///////////////////////////////////////////////////////

    @Override
    protected View onCreateDialogView()
    {
        picker = new NumberPicker(getContext());
        //picker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        return (picker);
    }

    @Override
    protected void onBindDialogView(View v)
    {
        super.onBindDialogView(v);
        picker.setValue(lastValue);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult)
    {
        super.onDialogClosed(positiveResult);
        if (positiveResult)
        {
            lastValue = picker.getValue();
            if (callChangeListener(lastValue))
            {
                persistInt(lastValue);
            }
        }
    }

}
