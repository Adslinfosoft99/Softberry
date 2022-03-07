package com.adslinfosoft.softberry.activity.enquiry;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.API.API;
import com.adslinfosoft.softberry.R;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.Utils.AppUtils;
import com.adslinfosoft.softberry.activity.enquiry.adapter.EnquiryAdapter;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.model.Job;
import com.adslinfosoft.softberry.model.Product;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

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

public class ActivityEnquiryList extends AppCompatActivity {

    private static final String TAG = "ActivityEnquiryList";
    public static final String EXTRA_USER = "EXTRA_USER";
    private RecyclerView mList;
    private CircularProgressView progressView;
    private TextView tvNoJobs;
    private LinearLayout mLiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notification);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.title);
        title.setText("Enquiry List");
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
        mList = findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mList.getRecycledViewPool().setMaxRecycledViews(0, 0);
        tvNoJobs = findViewById(R.id.txtNoJob);
        mLiner = findViewById(R.id.email_login_form);
        fetchEnquiryList();
    }

    private void fetchEnquiryList() {

        if (AppUtils.isNetworkAvailable(getApplicationContext())) {
            progressView.startAnimation();
            progressView.setVisibility(View.VISIBLE);
            String url = API.GET_ENQUIRY_LIST;

            StringRequest jsonObjRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("response", response);
                        ArrayList<Job> jobList = new ArrayList<>();
                        progressView.stopAnimation();
                        progressView.setVisibility(View.GONE);
                        JSONArray jsonArr = new JSONArray(response);
                        Log.e("lenght", "" + jsonArr.length());
                        if (jsonArr.length() == 0) {
                            tvNoJobs.setVisibility(View.VISIBLE);
                        } else {
                            tvNoJobs.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject jsonObj = jsonArr.getJSONObject(i);
                                Job job = new Job();
                                job.setJobId(jsonObj.getInt("jeid"));
                                job.setTargetDate(jsonObj.getString("targetdate"));
                                job.setDate(jsonObj.getString("jobdate"));
                                ArrayList<Product> productList = new ArrayList<>();
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                    JSONArray jsonArr2 = jsonObj.getJSONArray("getenquiryproducts");
                                    for (int j = 0; j < jsonArr2.length(); j++) {
                                        JSONObject jsonObj2 = jsonArr2.getJSONObject(j);
                                        Product product = new Product();
                                        product.setProductName(jsonObj2.getString("productname"));
                                        productList.add(product);
                                    }
                                }
                                job.setProducts(productList);
                                jobList.add(job);
                            }
                            progressView.stopAnimation();
                            progressView.setVisibility(View.GONE);
                            reloadData(jobList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);
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

    private void reloadData(ArrayList<Job> jobList) {
        mLiner.setVisibility(View.VISIBLE);
        EnquiryAdapter mAdapter = new EnquiryAdapter(ActivityEnquiryList.this, jobList);
        mList.setAdapter(mAdapter);
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
