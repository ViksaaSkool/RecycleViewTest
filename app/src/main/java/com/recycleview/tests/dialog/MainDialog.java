package com.recycleview.tests.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.github.johnpersano.supertoasts.SuperToast;
import com.recycleview.tests.activity.MainActivity;
import com.recycleview.tests.activity.R;
import com.recycleview.tests.helpers.FragmentHelper;
import com.recycleview.tests.utils.LogUtil;
import com.recycleview.tests.utils.Static;
import com.recycleview.tests.utils.UIUtil;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by varsovski on 30-Apr-15.
 */
public class MainDialog extends DialogFragment implements View.OnClickListener {

    @InjectView(R.id.etSubreddit)
    EditText mEtSubreddit;
    @InjectView(R.id.etRatio)
    EditText mEtRatio;
    @InjectView(R.id.bCancel)
    Button mBCancel;
    @InjectView(R.id.bSave)
    Button mBSave;

    public MainDialog() {
        // Empty constructor required for DialogFragment
    }

    public static MainDialog newInstance(String title) {

        MainDialog frag = new MainDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_main, container, false);
        ButterKnife.inject(this, v);
        initDialog();

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bCancel:
                dismiss();
                UIUtil.hideKeyboard((MainActivity) getActivity());
                break;

            case R.id.bSave:
                if (validate()) {
                    saveInCache();
                    dismiss();
                    UIUtil.hideKeyboard((MainActivity) getActivity());
                    FragmentHelper.setMainFragment((MainActivity)getActivity());
                } else {
                    UIUtil.showSuperToast(getActivity(), R.string.notValid, SuperToast.Duration.MEDIUM);
                    mEtRatio.setText("");
                }

                break;

            default:
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        UIUtil.hideKeyboard((MainActivity) getActivity());
    }


    public void initDialog() {
        //set listeners
        mBCancel.setOnClickListener(this);
        mBSave.setOnClickListener(this);

        //dialog settings
        setCancelable(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }


    public boolean validate() {
        boolean r = true;
        if (!mEtRatio.getText().toString().isEmpty()) {
            double ratio = Double.parseDouble(mEtRatio.getText().toString());
            if (ratio > 10.0 || ratio <= 0.0)
                r = false;
        } else
            r = false;

        return r;
    }


    public void saveInCache() {
        DB db = ((MainActivity) getActivity()).getDB();
        try {
            if (db == null || !db.isOpen())
                db = DBFactory.open(getActivity());
            if (mEtSubreddit.getText() != null && !mEtSubreddit.getText().toString().isEmpty())
                db.put(Static.SUBREDDIT, mEtSubreddit.getText().toString());
            if (mEtRatio.getText() != null && !mEtRatio.getText().toString().isEmpty())
                db.putDouble(Static.PIC_RATIO, Double.parseDouble(mEtRatio.getText().toString()));
        } catch (SnappydbException e) {
            e.printStackTrace();
            LogUtil.dLog(Static.DB_TAG, "SnappydbException | saveInCache()");
        }

    }
}
