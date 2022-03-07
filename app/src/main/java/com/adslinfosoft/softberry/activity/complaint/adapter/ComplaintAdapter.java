package com.adslinfosoft.softberry.activity.complaint.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.activity.complaint.ActivityComplaintDetails;
import com.adslinfosoft.softberry.model.Complaint;
import com.adslinfosoft.softberry.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.VerticalItemHolder> {

    private final ArrayList<Complaint> mItems;
    private final Context mContext;

    public ComplaintAdapter(Context mContext, ArrayList<Complaint> items) {
        mItems = items;
        this.mContext = mContext;
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.complaint_items, container, false);

        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        Complaint item = mItems.get(position);
        itemHolder.setData(item);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public class VerticalItemHolder extends RecyclerView.ViewHolder {
        private final TextView mJobNo;
        private final TextView mJobDescr;
        private final ComplaintAdapter mAdapter;
        private final TextView mJobStatusVal1;
        private final TextView mJobStatusVal2;
        private final LinearLayout mRelMain;

        public VerticalItemHolder(View itemView, ComplaintAdapter adapter) {
            super(itemView);

            mAdapter = adapter;

            mJobNo = itemView.findViewById(R.id.txt_jobNoVal);
            mJobDescr = itemView.findViewById(R.id.tv_title);
            mJobStatusVal1 = itemView.findViewById(R.id.txt_StatusVal);
            mJobStatusVal2 = itemView.findViewById(R.id.txt_StatusVal2);
            mRelMain = itemView.findViewById(R.id.rel_main);
        }


        public void setData(final Complaint item) {
            mJobNo.setText("" + item.getJobNo());
            mJobDescr.setText("" + item.getIssueDescr());
            if (item.getStatus().equalsIgnoreCase("Open")) {
                mJobStatusVal2.setVisibility(View.VISIBLE);
                mJobStatusVal1.setVisibility(View.GONE);
                mJobStatusVal2.setText("" + item.getStatus());
            } else {
                mJobStatusVal2.setVisibility(View.GONE);
                mJobStatusVal1.setVisibility(View.VISIBLE);
                mJobStatusVal1.setText("" + item.getStatus());
            }
            mRelMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityComplaintDetails.class);
                    intent.putExtra(AppConstants.USER_DATA, item);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
