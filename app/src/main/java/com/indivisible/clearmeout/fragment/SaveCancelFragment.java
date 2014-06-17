package com.indivisible.clearmeout.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.indivisible.clearmeout.R;

/**
 * Created by indiv on 16/06/14.
 */
public class SaveCancelFragment
        extends Fragment
        implements View.OnClickListener
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private Button btnSave;
    private Button btnCancel;

    private SaveCancelInterface saveCancelInterface;

    private static final String TAG = "SaveCancelFrag";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public static final SaveCancelFragment newInstance()
    {
        SaveCancelFragment frag = new SaveCancelFragment();
        return frag;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        verifyParentActivityInterface();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        container.removeAllViews();
        View view = inflater.inflate(R.layout.frag_savecancel, container);
        btnSave = (Button) view.findViewById(R.id.savecancel_btn_save);
        btnCancel = (Button) view.findViewById(R.id.savecancel_btn_cancel);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        return view;
    }


    ///////////////////////////////////////////////////////
    ////    communication
    ///////////////////////////////////////////////////////

    public interface SaveCancelInterface
    {

        public void onSave();

        public void onCancel();
    }

    private void verifyParentActivityInterface()
    {
        try
        {
            saveCancelInterface = (SaveCancelInterface) getActivity();
        }
        catch (ClassCastException e)
        {
            Log.e(TAG, "Parent Activity must implement 'SaveCancelInterface'");
            e.printStackTrace();
            throw e;
        }
    }

    public void setSaveEnabled(boolean isEnabled)
    {
        btnSave.setEnabled(isEnabled);
    }

    public void setCancelEnabled(boolean isEnabled)
    {
        btnCancel.setEnabled(isEnabled);
    }

    public void setBothEnabled(boolean isEnabled)
    {
        setSaveEnabled(isEnabled);
        setCancelEnabled(isEnabled);
    }


    ///////////////////////////////////////////////////////
    ////    click handling
    ///////////////////////////////////////////////////////

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.savecancel_btn_save:
                saveCancelInterface.onSave();
                break;
            case R.id.savecancel_btn_cancel:
                saveCancelInterface.onCancel();
                break;
            default:
                Log.e(TAG, "Unhandled button press!");
                break;
        }
    }
}
