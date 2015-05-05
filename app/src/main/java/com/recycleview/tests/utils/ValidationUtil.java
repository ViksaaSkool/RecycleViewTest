package com.recycleview.tests.utils;

import com.recycleview.tests.models.magnet.beans.SubRedditResult;

/**
 * Created by varsovski on 05-May-15.
 */
public class ValidationUtil {

    public static boolean vResultObject(SubRedditResult result, int flag) {
        boolean r = false;
        switch (flag) {
            case Static.REFRESH:
                if (result != null && result.getData() != null && result.getData().getChildren() != null &&
                        !result.getData().getChildren().isEmpty() && result.getData().getChildren().get(0) != null)
                    r = true;
                break;
            case Static.LOAD_MORE:
                if (result != null && result.getData() != null && result.getData().getChildren() != null &&
                        !result.getData().getChildren().isEmpty())
                    r = true;
                break;

            case Static.LOAD:

                break;

            default:
                break;
        }


        return r;


    }

    public static boolean valResObj(SubRedditResult result) {
        boolean r = false;

        if (result != null && result.getData() != null && result.getData().getChildren() != null &&
                !result.getData().getChildren().isEmpty())
            r = true;
        return r;
    }

    public static boolean valResObjRef(SubRedditResult result) {
        boolean r = false;
        if (valResObj(result) && result.getData().getChildren().get(0) != null)
            r = true;
        return r;


    }
}
