package com.recycleview.tests.helpers;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.recycleview.tests.activity.MainActivity;
import com.recycleview.tests.activity.R;
import com.recycleview.tests.dialog.MainDialog;
import com.recycleview.tests.fragments.MainFragment;

/**
 * Created by varsovski on 30-Apr-15.
 */
public class FragmentHelper {

    private static final String MAIN = "mainFragment";
    private static final String MAIN_DIALOG = "mainDialog";

    public static void setMainFragment(MainActivity c){
        FragmentTransaction ft = c.getSupportFragmentManager().beginTransaction();
        MainFragment fragment = MainFragment.newInstance();
        ft.replace(R.id.mainFragment, fragment, MAIN);
        ft.commit();
    }


    public static void showEditDialog(MainActivity c) {
        FragmentManager fm = c.getSupportFragmentManager();
        MainDialog settingsDialog = new MainDialog();
        settingsDialog.show(fm, MAIN_DIALOG);
    }

}
