package com.adslinfosoft.softberry.activity.profile.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.R;

import androidx.fragment.app.Fragment;

public class MyProfileFragment extends Fragment {
    private TextView tvName, tvClientEmail, tvCompanyName, tvBranch, tvAddress, tvCompanyMobile;
    private TextView tvPhoneNumber, tvStateCode, tvWebsite, tvGSTN, tvClientMobile, tvCompanyEmail;

    public MyProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_profile, container, false);
        tvName = view.findViewById(R.id.txt_Name);
        tvClientEmail = view.findViewById(R.id.txt_email);
        tvCompanyName = view.findViewById(R.id.txt_companyName);
        tvBranch = view.findViewById(R.id.txt_branchName);
        tvAddress = view.findViewById(R.id.txt_addressVal);
        tvCompanyMobile = view.findViewById(R.id.txt_companyPhone);
        tvPhoneNumber = view.findViewById(R.id.txt_mobileVal);
        tvStateCode = view.findViewById(R.id.txt_stateVal);
        tvWebsite = view.findViewById(R.id.txt_websiteVal);
        tvGSTN = view.findViewById(R.id.txt_gstnVal);
        tvClientMobile = view.findViewById(R.id.txt_mobile2Val);
        tvCompanyEmail = view.findViewById(R.id.txt_companyEmailVal);
        setData();
        return view;
    }

    private void setData() {
        tvName.setText(Softberry.getPreference().getString(AppConstants.CLIENT_NAME, ""));
        tvClientEmail.setText(Softberry.getPreference().getString(AppConstants.CLIENT_EMAIL, ""));
        tvCompanyName.setText(Softberry.getPreference().getString(AppConstants.COMPANY_NAME, ""));
        tvBranch.setText(Softberry.getPreference().getString(AppConstants.COMPANY_BRANCH, ""));
        String address = Softberry.getPreference().getString(AppConstants.ADDRESSLINE1, "") + " " + Softberry.getPreference().getString(AppConstants.ADDRESSLINE2, "") +
                ", " + Softberry.getPreference().getString(AppConstants.CITY, "") +", " + Softberry.getPreference().getString(AppConstants.STATE, "") +"- " + Softberry.getPreference().getString(AppConstants.PINCODE, "");
        tvAddress.setText(address);
        tvCompanyMobile.setText(Softberry.getPreference().getString(AppConstants.COMPANY_MOBILE, ""));
        tvPhoneNumber.setText(Softberry.getPreference().getString(AppConstants.COMPANY_PHONE, ""));
        tvStateCode.setText(Softberry.getPreference().getString(AppConstants.STATE_CODE, ""));
        tvWebsite.setText(Softberry.getPreference().getString(AppConstants.COMPANY_SITE, ""));
        tvGSTN.setText(Softberry.getPreference().getString(AppConstants.COMPANY_GSTIN, ""));
        tvClientMobile.setText(Softberry.getPreference().getString(AppConstants.CLIENT_MOBILE, ""));
        tvCompanyEmail.setText(Softberry.getPreference().getString(AppConstants.COMPANY_EMAIL, ""));
    }
}
