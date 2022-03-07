package com.adslinfosoft.softberry.activity.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.API.API;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.activity.detail.adapter.ChallanAdapter;
import com.adslinfosoft.softberry.activity.detail.adapter.NewInvoiceAdapter;
import com.adslinfosoft.softberry.activity.detail.adapter.ProductAdapter;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.model.FileVO;
import com.adslinfosoft.softberry.model.Invoice;
import com.adslinfosoft.softberry.model.Product;
import com.adslinfosoft.softberry.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityJobDetails extends AppCompatActivity implements ChallanAdapter.ChallanItemClickListener, NewInvoiceAdapter.InvoiceItemClickListener {
    private RecyclerView mList, mChallanList, mInvoiceList;
    private CircularProgressView progressView;
    private static final String TAG = "ActivityJobDetails";
    private TextView tvJobNo, tvJobDate, tvStatusVal1, tvStatusVal2;
    private TextView tvTargetDate, tvMarketPre, tvCoordinator, tvRankee, tvApproved, tvDeliveryProf;
    private TextView tvExcel, tvClientPO, tvQuotation;
    private String jobNo;
    private int jId;
    private RelativeLayout mRelChallan, mRelInvoice;
    private LinearLayout linDate, linMarket, linCoordi, linRankee, linApprov, linDelivery, linExcel, linClientPO, linQuot;
    private FloatingActionButton btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_job_details);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.title);
        title.setText("Job Details");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.loading_color)));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actionbar_indicator);
        hideKeyPad();
        getViews();
    }

    public void hideKeyPad() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void getViews() {
        jobNo = getIntent().getStringExtra(AppConstants.JOB_NO);
        jId = getIntent().getIntExtra(AppConstants.JID, 0);
        progressView = findViewById(R.id.progress_view);
        mList = findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mList.getRecycledViewPool().setMaxRecycledViews(0, 0);
        mChallanList = findViewById(R.id.recyclerViewChallanList);
        mChallanList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mChallanList.getRecycledViewPool().setMaxRecycledViews(0, 0);
        mInvoiceList = findViewById(R.id.recyclerViewInvoicesList);
        mInvoiceList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mInvoiceList.getRecycledViewPool().setMaxRecycledViews(0, 0);
        tvJobNo = findViewById(R.id.txt_jobNoVal);
        tvJobDate = findViewById(R.id.txt_jobDate);
        tvStatusVal1 = findViewById(R.id.txt_StatusVal);
        tvStatusVal2 = findViewById(R.id.txt_StatusVal2);
        tvTargetDate = findViewById(R.id.txt_targetDate);
        tvMarketPre = findViewById(R.id.txt_marketingPreVal);
        tvCoordinator = findViewById(R.id.txt_coordinatorVal);
        tvRankee = findViewById(R.id.txt_rankeeVal);
        tvApproved = findViewById(R.id.txt_approveVal);
        tvDeliveryProf = findViewById(R.id.txt_deliveryVal);
        tvExcel = findViewById(R.id.txt_excelVal);
        tvClientPO = findViewById(R.id.txt_clientPOVal);
        tvQuotation = findViewById(R.id.txt_quotationVal);
        mRelChallan = findViewById(R.id.relFour);
        mRelInvoice = findViewById(R.id.relFive);
        linDate = findViewById(R.id.lin1);
        linMarket = findViewById(R.id.lin2);
        linCoordi = findViewById(R.id.lin3);
        linRankee = findViewById(R.id.lin4);
        linApprov = findViewById(R.id.lin5);
        linDelivery = findViewById(R.id.lin6);
        linExcel = findViewById(R.id.lin7);
        linClientPO = findViewById(R.id.lin8);
        linQuot = findViewById(R.id.lin9);
        btnChat = findViewById(R.id.chat_fab);
        Register task = new Register();
        task.execute();

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
                params.put("jid", jId);
                params.put("jno", jobNo);
                Log.e("params", String.valueOf(params));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = API.GET_JOB_DETAILS;
            JsonObjectRequest req = new JsonObjectRequest(url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "response: " + response);
                            progressView.stopAnimation();
                            progressView.setVisibility(View.GONE);
                            parseData(response);
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

    private void parseData(JSONObject response) {
        ArrayList<Product> productList = new ArrayList<>();
        try {
            tvJobNo.setText("" + response.getString("jno"));
            tvJobDate.setText("" + response.getString("jdate"));
            if (response.getString("jstatus").equalsIgnoreCase("Cancelled")) {
                tvStatusVal2.setVisibility(View.VISIBLE);
                tvStatusVal1.setVisibility(View.GONE);
                tvStatusVal2.setText("" + response.getString("jstatus"));
            } else {
                tvStatusVal2.setVisibility(View.GONE);
                tvStatusVal1.setVisibility(View.VISIBLE);
                tvStatusVal1.setText("" + response.getString("jstatus"));
            }
            JSONArray jsonArr = response.getJSONArray("jobproductdetails");
            if (jsonArr.length() == 0) {
                mList.setVisibility(View.GONE);
            } else {
                mList.setVisibility(View.VISIBLE);
                for (int j = 0; j < jsonArr.length(); j++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(j);
                    Product product = new Product();
                    product.setProductName(jsonObj.getString("productname"));
                    product.setProductQty(jsonObj.getString("productquantity"));
                    product.setProductDescr(jsonObj.getString("productdesc"));
                    productList.add(product);
                }
                ProductAdapter mAdapter = new ProductAdapter(ActivityJobDetails.this, productList);
                mList.setAdapter(mAdapter);
            }
            if (TextUtils.isEmpty(response.getString("targetdate"))) {
                linDate.setVisibility(View.GONE);
            } else {
                linDate.setVisibility(View.VISIBLE);
                tvTargetDate.setText("" + response.getString("targetdate"));
            }
            if (TextUtils.isEmpty(response.getString("marketingperson"))) {
                linMarket.setVisibility(View.GONE);
            } else {
                linMarket.setVisibility(View.VISIBLE);
                tvMarketPre.setText("" + response.getString("marketingperson"));
            }
            if (TextUtils.isEmpty(response.getString("coordinator"))) {
                linCoordi.setVisibility(View.GONE);
            } else {
                linCoordi.setVisibility(View.VISIBLE);
                tvCoordinator.setText("" + response.getString("coordinator"));
            }
            JSONArray jsonArr2 = response.getJSONArray("jobpptfiles");
            if (jsonArr2.length() == 0) {
                linRankee.setVisibility(View.GONE);
            } else {
                linRankee.setVisibility(View.VISIBLE);
                ArrayList<FileVO> fileList = new ArrayList<>();
                for (int j = 0; j < jsonArr2.length(); j++) {
                    JSONObject jsonObj = jsonArr2.getJSONObject(j);
                    FileVO product = new FileVO();
                    product.setFileName(jsonObj.getString("filename"));
                    product.setFileType(jsonObj.getString("filetype"));
                    product.setFilePath(jsonObj.getString("filepath"));
                    fileList.add(product);
                }
                tvRankee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ActivityJobDetails.this, ActivityFileList.class);
                        intent.putExtra(AppConstants.USER_DATA, fileList);
                        intent.putExtra(AppConstants.JOB_NO, jobNo);
                        intent.putExtra(AppConstants.NAME, "Rankee PPT");
                        try {
                            intent.putExtra(AppConstants.JOB_DATE, response.getString("jdate"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
            }
            JSONArray jsonArr3 = response.getJSONArray("jobapprovedpptfiles");
            if (jsonArr3.length() == 0) {
                linApprov.setVisibility(View.GONE);
            } else {
                ArrayList<FileVO> fileList = new ArrayList<>();
                linApprov.setVisibility(View.VISIBLE);
                for (int j = 0; j < jsonArr3.length(); j++) {
                    JSONObject jsonObj = jsonArr3.getJSONObject(j);
                    FileVO product = new FileVO();
                    product.setFileName(jsonObj.getString("filename"));
                    product.setFileType(jsonObj.getString("filetype"));
                    product.setFilePath(jsonObj.getString("filepath"));
                    fileList.add(product);
                }
                tvApproved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ActivityJobDetails.this, ActivityFileList.class);
                        intent.putExtra(AppConstants.USER_DATA, fileList);
                        intent.putExtra(AppConstants.JOB_NO, jobNo);
                        intent.putExtra(AppConstants.NAME, "Approved Rankee PPT");
                        try {
                            intent.putExtra(AppConstants.JOB_DATE, response.getString("jdate"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
            }
            JSONArray jsonArr4 = response.getJSONArray("jobdeliverypdffiles");
            if (jsonArr4.length() == 0) {
                linDelivery.setVisibility(View.GONE);
            } else {
                ArrayList<FileVO> fileList = new ArrayList<>();
                linDelivery.setVisibility(View.VISIBLE);
                for (int j = 0; j < jsonArr4.length(); j++) {
                    JSONObject jsonObj = jsonArr4.getJSONObject(j);
                    FileVO product = new FileVO();
                    product.setFileName(jsonObj.getString("filename"));
                    product.setFileType(jsonObj.getString("filetype"));
                    product.setFilePath(jsonObj.getString("filepath"));
                    fileList.add(product);
                }
                tvDeliveryProf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ActivityJobDetails.this, ActivityFileList.class);
                        intent.putExtra(AppConstants.USER_DATA, fileList);
                        intent.putExtra(AppConstants.JOB_NO, jobNo);
                        intent.putExtra(AppConstants.NAME, "Delivery Proof");
                        try {
                            intent.putExtra(AppConstants.JOB_DATE, response.getString("jdate"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
            }
            JSONArray jsonArr5 = response.getJSONArray("jobvendorpofiles");
            if (jsonArr5.length() == 0) {
                linExcel.setVisibility(View.GONE);
            } else {
                ArrayList<FileVO> fileList = new ArrayList<>();
                linExcel.setVisibility(View.VISIBLE);
                for (int j = 0; j < jsonArr5.length(); j++) {
                    JSONObject jsonObj = jsonArr5.getJSONObject(j);
                    FileVO product = new FileVO();
                    product.setFileName(jsonObj.getString("filename"));
                    product.setFileType(jsonObj.getString("filetype"));
                    product.setFilePath(jsonObj.getString("filepath"));
                    fileList.add(product);
                }
                tvExcel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ActivityJobDetails.this, ActivityFileList.class);
                        intent.putExtra(AppConstants.USER_DATA, fileList);
                        intent.putExtra(AppConstants.JOB_NO, jobNo);
                        intent.putExtra(AppConstants.NAME, "Excel Estimate");
                        try {
                            intent.putExtra(AppConstants.JOB_DATE, response.getString("jdate"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
            }
            JSONArray jsonArr6 = response.getJSONArray("jobpofiles");
            if (jsonArr6.length() == 0) {
                linClientPO.setVisibility(View.GONE);
            } else {
                ArrayList<FileVO> fileList = new ArrayList<>();
                linClientPO.setVisibility(View.VISIBLE);
                for (int j = 0; j < jsonArr6.length(); j++) {
                    JSONObject jsonObj = jsonArr6.getJSONObject(j);
                    FileVO product = new FileVO();
                    product.setFileName(jsonObj.getString("filename"));
                    product.setFileType(jsonObj.getString("filetype"));
                    product.setFilePath(jsonObj.getString("filepath"));
                    fileList.add(product);
                }
                tvClientPO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ActivityJobDetails.this, ActivityFileList.class);
                        intent.putExtra(AppConstants.USER_DATA, fileList);
                        intent.putExtra(AppConstants.JOB_NO, jobNo);
                        intent.putExtra(AppConstants.NAME, "Client PO");
                        try {
                            intent.putExtra(AppConstants.JOB_DATE, response.getString("jdate"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
            }
            if (TextUtils.isEmpty(response.getString("qno"))) {
                linQuot.setVisibility(View.GONE);
            } else {
                linQuot.setVisibility(View.VISIBLE);
                tvQuotation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ActivityJobDetails.this, ActivitySingelFile.class);
                        try {
                            String url = API.GET_QUOTATION + response.getInt("qid") + "," + response.getString("qno");
                            intent.putExtra(AppConstants.USER_DATA, url);
                            intent.putExtra(AppConstants.NAME, "QUOTATION - " + response.getString("qno"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);

                    }
                });
            }
            JSONArray jsonArr7 = response.getJSONArray("jobchallan");
            if (jsonArr7.length() == 0) {
                mRelChallan.setVisibility(View.GONE);
            } else {
                ArrayList<Invoice> fileList = new ArrayList<>();
                mRelChallan.setVisibility(View.VISIBLE);
                for (int j = 0; j < jsonArr7.length(); j++) {
                    JSONObject jsonObj = jsonArr7.getJSONObject(j);
                    Invoice product = new Invoice();
                    product.setInvoiceId(jsonObj.getInt("chid"));
                    product.setInvoiceNo(jsonObj.getString("chno"));
                    product.setStatus(jsonObj.getString("status"));
                    fileList.add(product);
                }
                ChallanAdapter mAdapter = new ChallanAdapter(ActivityJobDetails.this, ActivityJobDetails.this, fileList);
                mChallanList.setAdapter(mAdapter);
            }
            JSONArray jsonArrInvoice = response.getJSONArray("jobinvoice");
            if (jsonArrInvoice.length() == 0) {
                mRelInvoice.setVisibility(View.GONE);
            } else {
                ArrayList<Invoice> fileList2 = new ArrayList<>();
                mRelInvoice.setVisibility(View.VISIBLE);
                for (int j = 0; j < jsonArrInvoice.length(); j++) {
                    JSONObject jsonObj = jsonArrInvoice.getJSONObject(j);
                    Invoice product = new Invoice();
                    product.setInvoiceId(jsonObj.getInt("invid"));
                    product.setInvoiceNo(jsonObj.getString("invno"));
                    product.setStatus(jsonObj.getString("status"));
                    product.setTotalAmount(jsonObj.getDouble("totalamount"));
                    product.setPaymentDate(jsonObj.getString("paymentdate"));
                    product.setDueAmount(jsonObj.getString("dueamount"));
                    fileList2.add(product);
                }
                NewInvoiceAdapter mAdapter = new NewInvoiceAdapter(ActivityJobDetails.this, ActivityJobDetails.this, fileList2);
                mInvoiceList.setAdapter(mAdapter);
            }
            btnChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openChat();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openChat() {
        ChatDialog addPhotoBottomDialogFragment =
                ChatDialog.newInstance(getApplicationContext(), jId, jobNo);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                ChatDialog.TAG);

    }


    @Override
    public void clickOnChallantItem(int cId, String cNo) {
        Intent intent = new Intent(ActivityJobDetails.this, ActivitySingelFile.class);
        String url = API.GET_CHALLAN + cId + "," + cNo;
        intent.putExtra(AppConstants.USER_DATA, url);
        intent.putExtra(AppConstants.NAME, "Challan - " + cNo);
        startActivity(intent);
    }

    @Override
    public void clickOnInvoiceItem(int invId, String invNo) {
        Intent intent = new Intent(ActivityJobDetails.this, ActivitySingelFile.class);
        String url = API.GET_INVOICE + invId + "," + invNo;
        intent.putExtra(AppConstants.USER_DATA, url);
        intent.putExtra(AppConstants.NAME, "Invoice - " + invNo);
        startActivity(intent);
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
