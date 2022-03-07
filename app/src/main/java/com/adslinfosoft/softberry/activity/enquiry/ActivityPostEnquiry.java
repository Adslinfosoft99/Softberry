package com.adslinfosoft.softberry.activity.enquiry;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.API.API;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.Utils.AppUtils;
import com.adslinfosoft.softberry.Utils.dialog.ListModel;
import com.adslinfosoft.softberry.Utils.dialog.OnSpinerItemClick;
import com.adslinfosoft.softberry.Utils.dialog.SpinnerDialog;
import com.adslinfosoft.softberry.adapter.ProductListAdapter;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.model.Product;
import com.adslinfosoft.softberry.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityPostEnquiry extends AppCompatActivity {
    private CircularProgressView progressView;
    private EditText etEnquiry, etDate, etImg, etCompany, etContactPerson;
    private EditText etMobile1, etMobile2, etEmail1, etEmail2, etQuantity;
    private EditText etMarketing, etDescr, etComment;
    private RecyclerView mList;
    private Button mbtnAdd, mbtnSubmit;
    private SpinnerDialog spFranchies;
    private static final String TAG = "ActivityPostEnquiry";
    private RelativeLayout mRelProducts;
    private TextView tvProduct;
    private int productId;
    private String productName;
    private static SimpleDateFormat mFormat;
    private static TextView SetDateView;
    private ArrayList<Product> productList;
    private ProductListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.title);
        title.setText("Post Enquiry");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.loading_color)));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actionbar_indicator);
        hideKeyPad();
        getViews();

    }

    public void hideKeyPad() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void getViews() {
        progressView = findViewById(R.id.progress_view);
        etEnquiry = findViewById(R.id.et_name);
        etDate = findViewById(R.id.et_date);
        etImg = findViewById(R.id.et_image);
        etCompany = findViewById(R.id.et_company);
        etContactPerson = findViewById(R.id.et_contact);
        etMobile1 = findViewById(R.id.et_mobile1);
        etMobile2 = findViewById(R.id.et_mobile2);
        etEmail1 = findViewById(R.id.et_email1);
        etEmail2 = findViewById(R.id.et_email2);
        etQuantity = findViewById(R.id.et_qty);
        etMarketing = findViewById(R.id.et_marketing);
        etDescr = findViewById(R.id.et_descr);
        etComment = findViewById(R.id.et_comment);
        mList = findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mList.getRecycledViewPool().setMaxRecycledViews(0, 0);
        mbtnAdd = findViewById(R.id.btn_add);
        mbtnSubmit = findViewById(R.id.btn_submit);
        mRelProducts = findViewById(R.id.relFranchies);
        tvProduct = findViewById(R.id.txt_product);
        addDataInListState();
        mbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
        mFormat = new SimpleDateFormat("MM/dd/yyyy");
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDateView = etDate;
                new DatePickerFragment().show(ActivityPostEnquiry.this.getSupportFragmentManager(), "datePicker");
            }
        });
        etDate.setText(mFormat.format(new Date()));
        productList = new ArrayList<>();
        if (productList.size() == 0) {
            mList.setVisibility(View.GONE);
        }
        mAdapter = new ProductListAdapter(ActivityPostEnquiry.this, productList);
        mList.setAdapter(mAdapter);
        setData();
        mbtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields2();
            }
        });
    }

    private void validateFields2() {
        if (TextUtils.isEmpty(etDate.getText().toString())) {
            showToast("Please Select Target Date");
        } else {
            if (productList.size() == 0) {
                showToast("Please Add Product");
            } else {
                Register task = new Register();
                task.execute();
            }
        }
    }

    private void setData() {
        etCompany.setText(Softberry.getPreference().getString(AppConstants.COMPANY_NAME, ""));
        etContactPerson.setText(Softberry.getPreference().getString(AppConstants.CLIENT_NAME, ""));
        etMobile1.setText(Softberry.getPreference().getString(AppConstants.CLIENT_MOBILE, ""));
        etEmail1.setText(Softberry.getPreference().getString(AppConstants.CLIENT_EMAIL, ""));
        etMarketing.setText(Softberry.getPreference().getString(AppConstants.MARKETING_MANAGER, ""));
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Date today = new Date();
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(mFormat.parse(mFormat.format(today)));
            } catch (Exception e) {
            }
            return new DatePickerDialog(getActivity(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            SetDateView.setText(mFormat.format(c.getTime()));
            SetDateView.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void validateFields() {
        if (TextUtils.isEmpty(tvProduct.getText().toString())) {
            showToast("Please Select Product");
        } else {
            if (TextUtils.isEmpty(etQuantity.getText().toString())) {
                showToast("Please Enter Quantity");
            } else {
                addProduct();
            }
        }
    }


    private void addProduct() {
        mList.setVisibility(View.VISIBLE);
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName(productName);
        product.setProductQty(etQuantity.getText().toString());
        product.setProductDescr(etDescr.getText().toString());
        productList.add(product);
        mAdapter.notifyDataSetChanged();
    }


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void addDataInListState() {
        if (AppUtils.isNetworkAvailable(getApplicationContext())) {
            progressView.stopAnimation();
            progressView.setVisibility(View.GONE);
            String url = API.GET_PRODUCTS;
            Log.e("url", url);
            StringRequest jsonObjRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("response", response);
                    try {
                        progressView.stopAnimation();
                        progressView.setVisibility(View.GONE);
                        ArrayList<ListModel> stateList = new ArrayList<>();
                        JSONArray jsonArr = new JSONArray(response);
                        Log.e("response", response);
                        if (jsonArr.length() == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "No Product Found", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject jsonObj = jsonArr.getJSONObject(i);
                                ListModel member = new ListModel();
                                member.setID(jsonObj.getInt("productid"));
                                member.setName(jsonObj.getString("productname"));
                                stateList.add(member);
                            }
                            setBookingData(stateList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong. please try again later.", Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "bearer " + Softberry.getPreference().getString(AppConstants.ACCESS_TOKEN, ""));
                    return params;
                }
            };
            Softberry.getInstance().addToRequestQueue(jsonObjRequest);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Error! Please check your net connection.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setBookingData(ArrayList<ListModel> stateList) {
        if (stateList != null || stateList.size() > 0) {
            mRelProducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spFranchies.showSpinerDialog();
                }
            });
        }
        spFranchies = new SpinnerDialog(ActivityPostEnquiry.this, stateList, "Select Product");
        spFranchies.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(ListModel listModel) {
                tvProduct.setText(listModel.getName());
                productId = listModel.getID();
                productName = listModel.getName();
            }
        });
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private class Register extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressView.startAnimation();
            progressView.setVisibility(View.VISIBLE);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... args) {
            String status = null;
            JSONObject params = new JSONObject();
            try {
                params.put("company", etCompany.getText().toString());
                params.put("contactperson", etContactPerson.getText().toString());
                params.put("mobile1", etMobile1.getText().toString());
                params.put("mobile2", etMobile2.getText().toString());
                params.put("emailid1", etEmail1.getText().toString());
                params.put("emailid2", etEmail2.getText().toString());
                params.put("targetdate", etDate.getText().toString());
                params.put("marketingperson", etMarketing.getText().toString());
                params.put("noofimages", etImg.getText().toString());
                params.put("comment", etComment.getText().toString());
                JSONArray jsonArrayLikes = new JSONArray();
                for (Product product : productList) {
                    JSONObject jsonLikes = new JSONObject();
                    jsonLikes.put("productid", product.getProductId());
                    jsonLikes.put("productname", product.getProductName());
                    jsonLikes.put("productquantity", product.getProductQty());
                    jsonLikes.put("productdesc", product.getProductDescr());
                    jsonArrayLikes.put(jsonLikes);
                }
                params.put("enquiryjobproduct", jsonArrayLikes);
                Log.e("params", String.valueOf(params));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = API.JOB_ENQUIRY;
            JsonObjectRequest req = new JsonObjectRequest(url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "response: " + response);
                            progressView.stopAnimation();
                            progressView.setVisibility(View.GONE);
                            try {
                                String msg = response.getString("message");
                                if (msg.equals("success")) {
                                    Toast.makeText(getApplicationContext(),
                                            "Job Enquiry Submitted Successfully", Toast.LENGTH_SHORT).show();
                                  finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong. please try again later.", Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "bearer " + Softberry.getPreference().getString(AppConstants.ACCESS_TOKEN, ""));
                    return params;
                }
            };

            Softberry.getInstance().addToRequestQueue(req);
            return status;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
