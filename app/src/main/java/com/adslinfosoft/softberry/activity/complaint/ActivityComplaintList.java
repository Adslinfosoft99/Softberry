package com.adslinfosoft.softberry.activity.complaint;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.API.API;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.Utils.AppUtils;
import com.adslinfosoft.softberry.activity.complaint.adapter.ComplaintAdapter;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.model.Complaint;
import com.adslinfosoft.softberry.R;
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
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

public class ActivityComplaintList extends AppCompatActivity {

    private static final String TAG = "ActivityComplaintList";
    private RecyclerView mList;
    private CircularProgressView progressView;
    private TextView tvNoJobs;
    private ThemedToggleButtonGroup btnGrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.title);
        title.setText("Complaint List");
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
        btnGrp = findViewById(R.id.time);
        btnGrp.selectButton(R.id.btn1);
        btnGrp.setOnSelectListener((ThemedButton btn) -> {
            fetchDetailsRequest(btn.getText());
            return kotlin.Unit.INSTANCE;
        });
        fetchDetailsRequest("Complaint Open");
    }

    private void fetchDetailsRequest(String type) {

        if (AppUtils.isNetworkAvailable(getApplicationContext())) {
            progressView.startAnimation();
            progressView.setVisibility(View.VISIBLE);
            String url = API.GET_COMPLAINT_LIST;

            StringRequest jsonObjRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("response", response);
                        ArrayList<Complaint> jobList = new ArrayList<>();
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
                                Complaint job = new Complaint();
                                job.setJcId(jsonObj.getInt("jcid"));
                                job.setDate(jsonObj.getString("complaintdate"));
                                job.setJobNo(jsonObj.getString("jno"));
                                job.setIssue(jsonObj.getString("issue"));
                                job.setStatus(jsonObj.getString("complaintstatus"));
                                job.setIssueDescr(jsonObj.getString("issuedesc"));
                                if (type.contains("Complaint Open") && !jsonObj.getString("complaintstatus").equalsIgnoreCase("Close")) {
                                    jobList.add(job);
                                } else {
                                    if (type.contains("Complaint Closed")) {
                                        if (jsonObj.getString("complaintstatus").equalsIgnoreCase("Close")) {
                                            jobList.add(job);
                                        }
                                    }
                                }
                            }
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

    private void reloadData(ArrayList<Complaint> jobList) {
        mList.setVisibility(View.VISIBLE);
        ComplaintAdapter mAdapter = new ComplaintAdapter(ActivityComplaintList.this, jobList);
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
