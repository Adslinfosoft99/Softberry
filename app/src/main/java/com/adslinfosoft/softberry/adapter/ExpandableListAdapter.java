package com.adslinfosoft.softberry.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adslinfosoft.softberry.model.ExpandedMenuModel;
import com.adslinfosoft.softberry.R;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.RequiresApi;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final List<ExpandedMenuModel> listDataHeader;
    private final HashMap<ExpandedMenuModel, List<ExpandedMenuModel>> listDataChild;
    private final Boolean isLogin;

    public ExpandableListAdapter(Context context, List<ExpandedMenuModel> listDataHeader,
                                 HashMap<ExpandedMenuModel, List<ExpandedMenuModel>> listChildData, Boolean isLogin) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.isLogin = isLogin;
    }

    @Override
    public ExpandedMenuModel getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).menuName;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_child, null);
        }

        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .size();
    }

    @Override
    public ExpandedMenuModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).menuName;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_header, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);
        ImageView Img = convertView.findViewById(R.id.ivGroupIndicator);
        ImageView ImgIcon = convertView.findViewById(R.id.personal_image);
        RelativeLayout mRel = convertView.findViewById(R.id.icon);
        if (isLogin) {
            if (groupPosition == 0) {
                Img.setVisibility(View.VISIBLE);
                Img.setSelected(isExpanded);
                mRel.setBackground(context.getDrawable(R.drawable.my_account_circle));
                ImgIcon.setImageResource(R.drawable.ic_my_account_white);
            } else if (groupPosition == 1) {
                Img.setVisibility(View.GONE);
                mRel.setBackground(context.getDrawable(R.drawable.post_enuiry));
                ImgIcon.setImageResource(R.drawable.ic_post_enquiry_white);
            } else if (groupPosition == 2) {
                Img.setVisibility(View.GONE);
                mRel.setBackground(context.getDrawable(R.drawable.enquiry_list));
                ImgIcon.setImageResource(R.drawable.ic_enquiry_list);
            } else if (groupPosition == 3) {
                Img.setVisibility(View.GONE);
                mRel.setBackground(context.getDrawable(R.drawable.notification));
                ImgIcon.setImageResource(R.drawable.ic_notification_white);
            } else if (groupPosition == 4) {
                Img.setVisibility(View.GONE);
                mRel.setBackground(context.getDrawable(R.drawable.post_complaint));
                ImgIcon.setImageResource(R.drawable.ic_post_complaint);
            } else if (groupPosition == 5) {
                Img.setVisibility(View.GONE);
                mRel.setBackground(context.getDrawable(R.drawable.complaint_list));
                ImgIcon.setImageResource(R.drawable.ic_complaint_list);
            }  else if (groupPosition == 6) {
                Img.setVisibility(View.GONE);
                mRel.setBackground(context.getDrawable(R.drawable.enquiry_list));
                ImgIcon.setImageResource(R.drawable.ic_logout);
            }
        } else {
            Img.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
