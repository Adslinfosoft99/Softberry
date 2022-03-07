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

public class NewInvoiceAdapter extends RecyclerView.Adapter<NewInvoiceAdapter.RowViewHolder> {

    private final ArrayList<Invoice> productList;
    private final Context mContext;
    InvoiceItemClickListener recyclerViewItemClickListener;

    public NewInvoiceAdapter(Context mContext, InvoiceItemClickListener listener, ArrayList<Invoice> productListm) {
        this.productList = productListm;
        this.mContext = mContext;
        this.recyclerViewItemClickListener = listener;
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.table_list_invoice_item, parent, false);

        return new RowViewHolder(itemView, this);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {
        RowViewHolder rowViewHolder = holder;
        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {
            // Header Cells. Main Headings appear here
            rowViewHolder.txtInvoice.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtStatus.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtAmount.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtDate.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtInvoice.setText("Invoice No.");
            rowViewHolder.txtStatus.setText("Status");
            rowViewHolder.txtAmount.setText("Due Amount");
            rowViewHolder.txtDate.setText("Payment Date");
        } else {
            final Invoice modal = productList.get(rowPos - 1);
            rowViewHolder.txtInvoice.setText("" + modal.getInvoiceNo());
            rowViewHolder.txtStatus.setText("" + modal.getStatus());
            rowViewHolder.txtAmount.setText("" + modal.getDueAmount());
            rowViewHolder.txtDate.setText("" + modal.getPaymentDate());
            rowViewHolder.txtInvoice.setPaintFlags(rowViewHolder.txtInvoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.sanspro_semibold);
            rowViewHolder.txtInvoice.setTypeface(typeface);

            rowViewHolder.txtInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewItemClickListener.clickOnInvoiceItem(modal.getInvoiceId(), modal.getInvoiceNo());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return productList.size() + 1; // one more to add header row
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtInvoice;
        protected TextView txtStatus;
        protected TextView txtAmount;
        protected TextView txtDate;

        public RowViewHolder(View itemView, NewInvoiceAdapter adapter) {
            super(itemView);
            txtInvoice = itemView.findViewById(R.id.txtInvoice);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDate = itemView.findViewById(R.id.txtPaymentDate);
        }

    }

    public interface InvoiceItemClickListener {
        void clickOnInvoiceItem(int cId, String cNo);
    }
}
