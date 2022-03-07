package com.adslinfosoft.softberry.activity.detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adslinfosoft.softberry.model.FileVO;
import com.adslinfosoft.softberry.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.VerticalItemHolder> {

    private final ArrayList<FileVO> mcmts;
    private final Context mContext;
    PaymentItemClickListener recyclerViewItemClickListener;

    public FilesAdapter(Context mContext, ArrayList<FileVO> items, PaymentItemClickListener listener) {
        this.mcmts = items;
        this.mContext = mContext;
        this.recyclerViewItemClickListener = listener;
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.files_item, container, false);

        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        FileVO item = mcmts.get(position);
        itemHolder.setData(item);
    }

    @Override
    public int getItemCount() {
        return mcmts == null ? 0 : mcmts.size();
    }

    public class VerticalItemHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final FilesAdapter mAdapter;
        private final RelativeLayout mRel;

        public VerticalItemHolder(View itemView, FilesAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            tvName = itemView.findViewById(R.id.tv_title);
            mRel = itemView.findViewById(R.id.relOne);
        }

        public void setData(FileVO item) {
            tvName.setText("" + item.getFileName());
            mRel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewItemClickListener.clickOnPaymentItem(item.getFilePath(), item.getFileName(), item.getFileType());
                }
            });
        }
    }

    public interface PaymentItemClickListener {
        void clickOnPaymentItem(String path, String name, String type);
    }
}
