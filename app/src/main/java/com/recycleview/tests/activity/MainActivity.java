package com.recycleview.tests.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.recycleview.tests.dialog.MainDialog;
import com.recycleview.tests.helpers.FragmentHelper;
import com.recycleview.tests.utils.LogUtil;
import com.recycleview.tests.utils.Static;
import com.recycleview.tests.utils.UIUtil;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    //ButterKnife injection
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    //cache
    private DB mDB;
    //loading dialog
    private MaterialDialog mLoading;
    //settings dialog
    private MainDialog mSettingsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (this != null)
            UIUtil.hideKeyboard(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentHelper.showEditDialog(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void init() {

        //open DB
        try {
            if (mDB == null || !mDB.isOpen()) {
                mDB = DBFactory.open(this);
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
            LogUtil.dLog(Static.DB_TAG, "SnappydbException | DB not opened");
        }

        //set toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title);

        //initalize materialDialog
        mLoading = UIUtil.getLoadingDialog(this).build();

        //set fragment
        FragmentHelper.setMainFragment(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //close DB
        try {
            if (mDB != null && mDB.isOpen())
                mDB.close();
        } catch (SnappydbException e) {
            e.printStackTrace();
            LogUtil.dLog(Static.DB_TAG, "SnappydbException | DB not closed");
        }
    }


    //getters and setters

    public DB getDB() {
        return mDB;
    }

    public void setDB(DB mDB) {
        this.mDB = mDB;
    }


    public MaterialDialog getLoading() {
        return mLoading;
    }

    public void setLoading(MaterialDialog mLoading) {
        this.mLoading = mLoading;
    }


}
