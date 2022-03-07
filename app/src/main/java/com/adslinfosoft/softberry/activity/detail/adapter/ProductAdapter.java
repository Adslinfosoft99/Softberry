package com.adslinfosoft.softberry.activity.detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adslinfosoft.softberry.model.Product;
import com.adslinfosoft.softberry.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VerticalItemHolder>{

    private final ArrayList<Product> mcmts;
    private final Context mContext;

    public ProductAdapter(Context mContext, ArrayList<Product> items) {
        this.mcmts = items;
        this.mContext = mContext;
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.product_items, container, false);

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
        private final TextView tvName;
        private final TextView tvQty;
        private final TextView tvDescr;
        private final ProductAdapter mAdapter;

        public VerticalItemHolder(View itemView, ProductAdapter adapter) {
            super(itemView);
            mAdapter = adapter;

            tvName = itemView.findViewById(R.id.tv_title);
            tvQty = itemView.findViewById(R.id.txt_StatusVal);
            tvDescr = itemView.findViewById(R.id.tv_descr);

        }

        public void setData(Product item) {
            tvName.setText("" + item.getProductName());
            tvQty.setText("Qty - " + item.getProductQty());
            tvDescr.setText("" + item.getProductDescr());
        }
    }
}
