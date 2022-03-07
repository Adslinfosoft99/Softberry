package com.adslinfosoft.softberry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adslinfosoft.softberry.model.Product;
import com.adslinfosoft.softberry.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by alokgupta on 26/10/16.
 */
public class AllCommentAdapter extends RecyclerView.Adapter<AllCommentAdapter.VerticalItemHolder> {

    private final ArrayList<Product> mcmts;
    private final Context mContext;
    private final boolean fromHome;


    public AllCommentAdapter(Context mContext, ArrayList<Product> items, Boolean fromHome) {
        this.mcmts = items;
        this.mContext = mContext;
        this.fromHome = fromHome;
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root;
        if (fromHome) {
            root = inflater.inflate(R.layout.allcomment_view, container, false);
        } else {
            root = inflater.inflate(R.layout.enquiry_products_items, container, false);
        }

        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        Product item = mcmts.get(position);
        itemHolder.setData(item);
    }

    @Override
    public int getItemCount() {
        return mcmts == null ? 0 : mcmts.size();
    }

    public class VerticalItemHolder extends RecyclerView.ViewHolder {
        private final TextView mCName;
        private final AllCommentAdapter mAdapter;

        public VerticalItemHolder(View itemView, AllCommentAdapter adapter) {
            super(itemView);
            mAdapter = adapter;

            mCName = itemView.findViewById(R.id.tv_title);

        }

        public void setData(Product item) {
            mCName.setText("" + item.getProductName());
        }
    }
}
