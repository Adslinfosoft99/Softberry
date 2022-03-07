package com.adslinfosoft.softberry.activity.detail.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adslinfosoft.softberry.model.Invoice;
import com.adslinfosoft.softberry.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ChallanAdapter extends RecyclerView.Adapter<ChallanAdapter.RowViewHolder>{

    private final ArrayList<Invoice> productList;
    private final Context mContext;
    ChallanItemClickListener recyclerViewItemClickListener;

    public ChallanAdapter(Context mContext, ChallanItemClickListener listener,  ArrayList<Invoice> productListm) {
        this.productList = productListm;
        this.mContext = mContext;
        this.recyclerViewItemClickListener = listener;
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.table_list_challan_item, parent, false);

        return new RowViewHolder(itemView, this);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {
        RowViewHolder rowViewHolder = holder;
        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {
            // Header Cells. Main Headings appear here
            rowViewHolder.txtQno.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtStatus.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtQno.setText("Challan No.");
            rowViewHolder.txtStatus.setText("Challan Status");
        } else {
            final Invoice modal = productList.get(rowPos - 1);
            rowViewHolder.txtQno.setText("" + modal.getInvoiceNo());
            rowViewHolder.txtStatus.setText("" + modal.getStatus());
            rowViewHolder.txtQno.setPaintFlags(rowViewHolder.txtQno.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.sanspro_semibold);
            rowViewHolder.txtQno.setTypeface(typeface);

            rowViewHolder.txtQno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewItemClickListener.clickOnChallantItem(modal.getInvoiceId(), modal.getInvoiceNo());
                }
            });

        }
    }
    @Override
    public int getItemCount() {
        return productList.size() + 1; // one more to add header row
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtQno;
        protected TextView txtStatus;


        public RowViewHolder(View itemView, ChallanAdapter adapter) {
            super(itemView);
            txtQno = itemView.findViewById(R.id.txtChallanNo);
            txtStatus = itemView.findViewById(R.id.txtChallanStatus);

        }

    }
    public interface ChallanItemClickListener {
        void clickOnChallantItem(int cId, String cNo);
    }

}
