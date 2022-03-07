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

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.VerticalItemHolder> {

    private final ArrayList<Product> mItems;
    private final Context mContext;

    public ProductListAdapter(Context mContext, ArrayList<Product> items) {
        mItems = items;
        this.mContext = mContext;
    }


    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.product_item_list, container, false);

        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        Product item = mItems.get(position);
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
        private final TextView tvProducts;
        private final ProductListAdapter mAdapter;


        public VerticalItemHolder(View itemView, ProductListAdapter adapter) {
            super(itemView);

            mAdapter = adapter;

            mJobNoVal = itemView.findViewById(R.id.txt_jobNoVal);
            mJobDateVal = itemView.findViewById(R.id.txt_jobDate);
            mJobStatusVal1 = itemView.findViewById(R.id.txt_StatusVal);
            tvProducts = itemView.findViewById(R.id.txt_products);

        }


        public void setData(final Product item) {
            mJobNoVal.setText("" + item.getProductName());
            mJobDateVal.setText("Qty - " + item.getProductQty());

            tvProducts.setText("" + item.getProductDescr());
            mJobStatusVal1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItems.remove(item);
                    notifyDataSetChanged();
                }
            });

        }
    }

}
