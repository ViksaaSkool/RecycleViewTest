package com.recycleview.tests.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnpersano.supertoasts.SuperToast;
import com.magnet.android.mms.async.Call;
import com.magnet.android.mms.async.StateChangedHandler;
import com.recycleview.tests.activity.MainActivity;
import com.recycleview.tests.activity.R;
import com.recycleview.tests.adapters.ItemsRecyclerAdapter;
import com.recycleview.tests.listeners.RecyclerItemClickListener;
import com.recycleview.tests.models.magnet.SubRedditItem;
import com.recycleview.tests.models.magnet.beans.Child;
import com.recycleview.tests.models.magnet.beans.SubRedditResult;
import com.recycleview.tests.rest.RestAPI;
import com.recycleview.tests.utils.LogUtil;
import com.recycleview.tests.utils.Static;
import com.recycleview.tests.utils.UIUtil;
import com.recycleview.tests.utils.ValidationUtil;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by varsovski on 29-Apr-15.
 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.rvPosts)
    RecyclerView mRvPosts;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayoutManager mLinearLayoutManager;
    private ItemsRecyclerAdapter mItemsRecyclerAdapter;
    private ArrayList<SubRedditItem> mItems;

    private String after, subreddit;
    private int totalItemCount, lastVisivleItem;
    private MainActivity mActivity;
    private SubRedditResult mSubredditResult;


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    //way to transfer arguments instead of using constructor; not in use
    public static MainFragment newInstance(String someString) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("someString", someString);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items, container, false);
        ButterKnife.inject(this, v);

        if (mActivity == null && getActivity() != null) {

            mActivity = (MainActivity) getActivity();
            mLinearLayoutManager = new LinearLayoutManager(getActivity());
            mRvPosts.setLayoutManager(mLinearLayoutManager);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            lastVisivleItem = 0;
            totalItemCount = 0;
            after = "";
            //make sure it loads fresh news
            try {
                if (mActivity.getDB().exists(Static.FEED_AFTER))
                     mActivity.getDB().del(Static.FEED_AFTER);
            } catch (SnappydbException e) {
                e.printStackTrace();
            }
            setEndlessScroll();
        }


        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (mActivity == null && getActivity() != null) {
            mActivity = (MainActivity) getActivity();
        } else if (mSubredditResult == null){
            loadFeed(Static.LOAD);
        }

    }


    @Override
    public void onRefresh() {
        if (!mActivity.getLoading().isShowing() && mSwipeRefreshLayout != null){

            loadFeed(Static.REFRESH);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    public void setEndlessScroll() {
        if (mRvPosts != null)
            mRvPosts.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (mLinearLayoutManager != null) {
                        totalItemCount = mLinearLayoutManager.getItemCount();
                        lastVisivleItem = mLinearLayoutManager.findLastVisibleItemPosition();


                        if (!mActivity.getLoading().isShowing() && lastVisivleItem == totalItemCount - 1) {
                            loadFeed(Static.LOAD_MORE);
                        }

                    }

                }
            });
    }

    public void loadFeed(final int flag) {
        if (mActivity.getLoading() != null && !mActivity.getLoading().isShowing()) {

            if (flag != Static.REFRESH)
                mActivity.getLoading().show();
            setFeedParams(flag);
            LogUtil.dLog(Static.AFTER_TAG, "after value =  " + after);
            RestAPI.getRedditAPI(mActivity).getSubReddit(subreddit, Integer.toString(Static.ITEMS_VALUE), after, new StateChangedHandler(mActivity) {
                @Override
                public void onSuccess(Call<?> call) throws Throwable {
                    super.onSuccess(call);
                    SubRedditResult result = new SubRedditResult();

                    if (call != null) {
                        if (mSubredditResult == null)
                            mSubredditResult = new SubRedditResult();
                        result = ((Call<SubRedditResult>) call).get();

                        if (ValidationUtil.valResObj(result)) {

                            if (flag == Static.LOAD_MORE && ValidationUtil.valResObj(mSubredditResult)) {

                                if (mActivity.getDB() != null && mActivity.getDB().isOpen())
                                    mActivity.getDB().put(Static.FEED_AFTER, result.getData().getAfter());
                                mSubredditResult.getData().getChildren().addAll(result.getData().getChildren());
                                setAdapterAndData(mSubredditResult);
                            } else if ((flag == Static.REFRESH
                                    && ValidationUtil.valResObjRef(mSubredditResult)
                                    && ValidationUtil.valResObjRef(result)) || flag == Static.LOAD) {

                                LogUtil.dLog(Static.AFTER_TAG, "result.getData().getAfter() =  " + result.getData().getAfter());
                                if (mActivity.getDB() != null && mActivity.getDB().isOpen())
                                    mActivity.getDB().put(Static.FEED_AFTER, result.getData().getAfter());

                                mSubredditResult = result;

                                lastVisivleItem = 0;
                                mSubredditResult = result;
                            }


                            if (mSubredditResult != null && flag != Static.LOAD_MORE)
                                setAdapterAndData(mSubredditResult);
                            else if (mSubredditResult == null)
                                UIUtil.showSuperToast(mActivity, R.string.smtWWInt, SuperToast.Duration.MEDIUM);
                        } else
                            UIUtil.showSuperToast(mActivity, R.string.smtWWInt, SuperToast.Duration.MEDIUM);
                    }
                    if (mActivity.getLoading() != null && mActivity.getLoading().isShowing())
                        mActivity.getLoading().dismiss();

                    if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
                        mSwipeRefreshLayout.setRefreshing(false);

                }

                @Override
                public void onError(Call<?> call, Throwable cause) {
                    super.onError(call, cause);

                    LogUtil.dLog(Static.MAGNET_TAG, "loadFeed error because " + cause.toString());
                    UIUtil.showSuperToast(mActivity, R.string.smtWW, SuperToast.Duration.MEDIUM);

                    if (mActivity.getLoading() != null && mActivity.getLoading().isShowing())
                        mActivity.getLoading().dismiss();
                    if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
                        mSwipeRefreshLayout.setRefreshing(false);
                }
            });


        }

    }


    public void setFeedParams(int flag) {
        try {
            if (mActivity.getDB() != null && mActivity.getDB().isOpen()) {
                //check if new subreddit is entered
                if (mActivity.getDB().exists(Static.SUBREDDIT))
                    subreddit = mActivity.getDB().get(Static.SUBREDDIT);
                else
                    subreddit = Static.SUBREDDIT_VALUE;

                //check if new data needs to be loaded; null - start
                if (flag == Static.REFRESH)
                    after = "";
                else if (mActivity.getDB().exists(Static.FEED_AFTER))
                    after = mActivity.getDB().get(Static.FEED_AFTER);
                else
                    after = "";
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }


    public void setAdapterAndData(SubRedditResult subReddit) {

        SubRedditItem item = new SubRedditItem();
        mItems = new ArrayList<>();

        if (subReddit.getData() != null &&
                subReddit.getData().getChildren() != null &&
                !subReddit.getData().getChildren().isEmpty()) {

            ArrayList<Child> itemz = new ArrayList<>(subReddit.getData().getChildren());

            for (Child chld : itemz) {
                Matcher m = Pattern.compile(Static.PIC_PATTERN).matcher(chld.getData().getUrl());
                if (m.matches()) {
                    item.setTitle(chld.getData().getTitle());
                    item.setUrl(chld.getData().getUrl());
                    mItems.add(item);
                }
                item = new SubRedditItem();

            }

            if (mItems != null && mRvPosts != null) {
                mItemsRecyclerAdapter = new ItemsRecyclerAdapter(getActivity(), mItems);
                mRvPosts.setAdapter(mItemsRecyclerAdapter);
                //set onItemClickListener
                mRvPosts.addOnItemTouchListener(
                        new RecyclerItemClickListener(mActivity, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                UIUtil.showSuperToast(mActivity, "Position clicked: " + Integer.toString(position), SuperToast.Duration.MEDIUM);
                            }
                        })
                );

                mRvPosts.scrollToPosition(lastVisivleItem);
            }
        }
    }


}
