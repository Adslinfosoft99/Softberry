package com.adslinfosoft.softberry.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.activity.detail.ActivityJobDetails;
import com.adslinfosoft.softberry.model.Job;
import com.adslinfosoft.softberry.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.VerticalItemHolder> {
    private final ArrayList<Job> mItems;
    private final Context mContext;


    public SimpleAdapter(Context mContext, ArrayList<Job> items) {
        mItems = items;
        this.mContext = mContext;
    }


    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.view_match_item, container, false);

        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        Job item = mItems.get(position);
        itemHolder.setData(item);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public class VerticalItemHolder extends RecyclerView.ViewHolder {
        private final TextView mJobNoVal;
        private final TextView mJobDateVal;
        private final TextView mJobStatusVal1;
        private final TextView mJobStatusVal2;
        private final TextView tvProducts;
        private final RecyclerView mList;
        private final SimpleAdapter mAdapter;
        private AllCommentAdapter mAdapter2;
        private final LinearLayout mRelMain;
        private final RelativeLayout mRelTwo;

        public VerticalItemHolder(View itemView, SimpleAdapter adapter) {
            super(itemView);

            mAdapter = adapter;

            mJobNoVal = itemView.findViewById(R.id.txt_jobNoVal);
            mJobDateVal = itemView.findViewById(R.id.txt_jobDate);
            mJobStatusVal1 = itemView.findViewById(R.id.txt_StatusVal);
            mJobStatusVal2 = itemView.findViewById(R.id.txt_StatusVal2);
            tvProducts = itemView.findViewById(R.id.txt_products);
            mList = itemView.findViewById(R.id.list);
            mRelMain = itemView.findViewById(R.id.rel_main);
            mRelTwo = itemView.findViewById(R.id.relTwo);
        }


        public void setData(final Job item) {
            mJobNoVal.setText("" + item.getJobNo());
            mJobDateVal.setText("" + item.getDate());
            if (item.getStatus().equalsIgnoreCase("Cancelled"))
            {
                mJobStatusVal2.setVisibility(View.VISIBLE);
                mJobStatusVal1.setVisibility(View.GONE);
                mJobStatusVal2.setText(""+item.getStatus());
            } else {
                mJobStatusVal2.setVisibility(View.GONE);
                mJobStatusVal1.setVisibility(View.VISIBLE);
                mJobStatusVal1.setText(""+item.getStatus());
            }
            if (item.getProducts().size() !=0) {
                mAdapter2 = new AllCommentAdapter(mContext, item.getProducts(), true);
                mList.setAdapter(mAdapter2);
            } else {
                tvProducts.setVisibility(View.GONE);
            }

            mRelMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityJobDetails.class);
                    intent.putExtra(AppConstants.JOB_NO, item.getJobNo());
                    intent.putExtra(AppConstants.JID, item.getJobId());
                    mContext.startActivity(intent);
                }
            });
            mRelTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityJobDetails.class);
                    intent.putExtra(AppConstants.JOB_NO, item.getJobNo());
                    intent.putExtra(AppConstants.JID, item.getJobId());
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
