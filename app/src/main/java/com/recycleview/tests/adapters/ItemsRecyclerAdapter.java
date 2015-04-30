package com.recycleview.tests.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.recycleview.tests.activity.MainActivity;
import com.recycleview.tests.activity.R;
import com.recycleview.tests.models.magnet.SubRedditItem;
import com.recycleview.tests.utils.UIUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by varsovski on 29-Apr-15.
 */
public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.SimpleItemViewHolder> {


    private ArrayList<SubRedditItem> mSubredditItems;
    private Context mCntx;
    OnItemClickListener mItemClickListener; //in order to enable onItemClickListener

    public class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition()); //getPosition());
            }
        }

        TextView labelAndText;
        ImageView pic;
        RelativeLayout container;

        public SimpleItemViewHolder(View itemView) {
            super(itemView);

            labelAndText = (TextView) itemView.findViewById(R.id.txtItemTitle);
            pic = (ImageView) itemView.findViewById(R.id.imgItem);
            container = (RelativeLayout) itemView.findViewById(R.id.rlContainer);


        }
    }

    public ItemsRecyclerAdapter(Context c, ArrayList<SubRedditItem> si) {
        this.mSubredditItems = si;
        this.mCntx = c;
    }


    @Override
    public SimpleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mCntx).inflate(R.layout.recycler_item, parent, false);
        return new SimpleItemViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(SimpleItemViewHolder holder, int position) {

        if (holder.labelAndText != null && holder.pic != null && mSubredditItems.get(position) != null) {

            //set the title
            if (mSubredditItems.get(position).getTitle() != null)
                holder.labelAndText.setText(mSubredditItems.get(position).getTitle());

            //load image with picasso
            if (mSubredditItems.get(position).getUrl() != null)
               Picasso.with(mCntx).load(mSubredditItems.get(position).getUrl()).into(holder.pic);
            //holder._pic.setImage(ImageSource.uri(mSubredditItems.get(position).getUrl()));

            //set height in proportion to screen size
            int proportionalHeight = UIUtil.containerHeight((MainActivity) mCntx);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, proportionalHeight); // (width, height)
            holder.container.setLayoutParams(params);

        }

    }


    @Override
    public int getItemCount() {
        //if (recentPost != null && recentPost.getPosts() != null)

        return mSubredditItems.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    //to be added if more suitable
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}