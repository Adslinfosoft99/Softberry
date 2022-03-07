package com.adslinfosoft.softberry.activity.enquiry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adslinfosoft.softberry.adapter.AllCommentAdapter;
import com.adslinfosoft.softberry.model.Job;
import com.adslinfosoft.softberry.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class EnquiryAdapter extends RecyclerView.Adapter<EnquiryAdapter.VerticalItemHolder>{

    private final ArrayList<Job> mItems;
    private final Context mContext;

    public EnquiryAdapter(Context mContext, ArrayList<Job> items) {
        mItems = items;
        this.mContext = mContext;
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.enquiry_items, container, false);

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
        private final TextView mJobDate;
        private final TextView mJobTargetDate;
        private final RecyclerView mList;
        private final EnquiryAdapter mAdapter;
        private AllCommentAdapter mAdapter2;

        public VerticalItemHolder(View itemView, EnquiryAdapter adapter) {
            super(itemView);

            mAdapter = adapter;

            mJobDate = itemView.findViewById(R.id.txt_jobDate);
            mJobTargetDate = itemView.findViewById(R.id.txt_TargetDate);
            mList = itemView.findViewById(R.id.list);
        }


        public void setData(final Job item) {
            mJobDate.setText("" + item.getDate());
            mJobTargetDate.setText("" + item.getTargetDate());
            if (item.getProducts().size() !=0) {
                mAdapter2 = new AllCommentAdapter(mContext, item.getProducts(), false);
                mList.setAdapter(mAdapter2);
            }

        }
    }
}
