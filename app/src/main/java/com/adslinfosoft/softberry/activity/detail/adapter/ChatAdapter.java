package com.adslinfosoft.softberry.activity.detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adslinfosoft.softberry.model.Chat;
import com.adslinfosoft.softberry.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VerticalItemHolder>{

    private final ArrayList<Chat> productList;
    private final Context mContext;

    public ChatAdapter(Context mContext, ArrayList<Chat> productListm) {
        this.productList = productListm;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VerticalItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.chat_items, parent, false);

        return new VerticalItemHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        Chat item = productList.get(position);
        itemHolder.setData(item);
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public class VerticalItemHolder extends RecyclerView.ViewHolder {
        private final TextView tvUserMsg;
        private final TextView tvAdminMsg;
        private final ChatAdapter mAdapter;
        private final RelativeLayout relUser;
        private final RelativeLayout relAdmin;
        private final TextView tvUserTime;
        private final TextView tvAdminTime;

        public VerticalItemHolder(View itemView, ChatAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            tvUserMsg = itemView.findViewById(R.id.txtUser);
            tvAdminMsg = itemView.findViewById(R.id.txtAdmin);
            relUser = itemView.findViewById(R.id.relIncoming);
            relAdmin = itemView.findViewById(R.id.relOutgoing);
            tvUserTime = itemView.findViewById(R.id.txtUserTime);
            tvAdminTime = itemView.findViewById(R.id.txtAdminTime);
        }

        public void setData(Chat item) {
             if (item.getMessageusertype().equalsIgnoreCase("Client")) {
                 relUser.setVisibility(View.VISIBLE);
                 relAdmin.setVisibility(View.GONE);
                 tvUserMsg.setText(""+item.getMessage());
                 tvUserTime.setText(""+item.getMessagedate());
             } else {
                 relUser.setVisibility(View.GONE);
                 relAdmin.setVisibility(View.VISIBLE);
                 tvAdminMsg.setText(""+item.getMessage());
                 tvAdminTime.setText(""+item.getMessagedate());
             }
        }
    }
}
