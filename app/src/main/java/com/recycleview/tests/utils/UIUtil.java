package com.recycleview.tests.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.johnpersano.supertoasts.SuperToast;
import com.recycleview.tests.activity.MainActivity;
import com.recycleview.tests.activity.R;
import com.snappydb.SnappydbException;

/**
 * Created by varsovski on 29-Apr-15.
 */
public class UIUtil {

    public static int containerHeight(MainActivity ba) {
        DisplayMetrics dm = new DisplayMetrics();
        ba.getWindowManager().getDefaultDisplay().getMetrics(dm);
        LogUtil.dLog(Static.HEIGHT_TAG, "Screen Height of " + Build.MANUFACTURER + " " + Build.DEVICE + " "
                + Build.MODEL + " is " + Integer.toString(dm.heightPixels));

        double ratio = Static.PIC_RATIO_VALUE;

        try {
            if (ba.getDB() != null && ba.getDB().isOpen() && ba.getDB().exists(Static.PIC_RATIO))
                ratio = ba.getDB().getDouble(Static.PIC_RATIO);
        } catch (SnappydbException e) {
            e.printStackTrace();
            LogUtil.dLog(Static.DB_TAG, "SnappydbException | couldn't get Static.PIC_RATIO");
        }

        return (int) (dm.heightPixels / ratio);
    }

    public static MaterialDialog.Builder getLoadingDialog(Context c) {
        MaterialDialog.Builder md = new MaterialDialog.Builder(c)
                .title(R.string.pleaseWait)
                .content(R.string.loading)
                .cancelable(false)
                .backgroundColor(c.getResources().getColor(R.color.Chocolate))
                .itemColor(c.getResources().getColor(R.color.White))
                .contentColor(c.getResources().getColor(R.color.White))
                .titleColor(c.getResources().getColor(R.color.White))
                .dividerColor(c.getResources().getColor(R.color.White))
                .progress(true, 0);
        return md;
    }


    public static void showSuperToast(Context c, int id, int duration) {

        SuperToast superToast = new SuperToast(c);
        superToast.setDuration(duration);
        superToast.setText(c.getResources().getString(id));

        superToast.setBackground(R.color.Chocolate);
        superToast.setTextColor(c.getResources().getColor(R.color.White));
        superToast.setAnimations(SuperToast.Animations.FLYIN);
        superToast.show();
    }


    public static void showSuperToast(Context c, String text, int duration) {

        SuperToast superToast = new SuperToast(c);
        superToast.setDuration(duration);
        superToast.setText(text);

        superToast.setBackground(R.color.Chocolate);
        superToast.setTextColor(c.getResources().getColor(R.color.White));
        superToast.setAnimations(SuperToast.Animations.FLYIN);
        superToast.show();
    }


    public static void hideKeyboard(MainActivity a) {
        View view = a.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
