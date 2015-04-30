package com.recycleview.tests.rest;

import android.content.Context;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.magnet.android.mms.async.StateChangedHandler;
import com.magnet.android.mms.exception.SchemaException;
import com.recycleview.tests.models.magnet.SubRedditItem;
import com.recycleview.tests.models.magnet.api.RedditApi;
import com.recycleview.tests.models.magnet.api.RedditApiFactory;
import com.recycleview.tests.models.magnet.beans.SubRedditResult;

import java.util.ArrayList;

/**
 * Created by varsovski on 30-Apr-15.
 */
public class RestAPI {


   public static void fetchSubRedditItems(Context c, ArrayList<SubRedditItem> subredditItems, final String subreddit, String limit, String after) {


       MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(c);
       RedditApiFactory controllerFactory = new RedditApiFactory(magnetClient);
       RedditApi redditMagnetAPI = null;
       try {
           redditMagnetAPI = controllerFactory.obtainInstance();
       } catch (SchemaException e) {
           e.printStackTrace();
       }

       /*Call<SubRedditResult> callObject =*/
       final SubRedditResult subReddit = null;
       assert redditMagnetAPI != null;
       redditMagnetAPI.getSubReddit(subreddit, limit, after, new StateChangedHandler(c) {
           @Override
           public void onSuccess(Call<?> call) throws Throwable {
               super.onSuccess(call);
               SubRedditResult _subReddit = ((Call<SubRedditResult>)call).get();
              // if (_subReddit.getData().getChildren() != null &&

           }

           @Override
           public void onError(Call<?> call, Throwable cause) {
               super.onError(call, cause);
           }
       });
   }


    public static RedditApi getRedditAPI(Context c){
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(c);
        RedditApiFactory controllerFactory = new RedditApiFactory(magnetClient);
        RedditApi redditMagnetAPI = null;
        try {
            redditMagnetAPI = controllerFactory.obtainInstance();
        } catch (SchemaException e) {
            e.printStackTrace();
        }

        return redditMagnetAPI;
    }


}

