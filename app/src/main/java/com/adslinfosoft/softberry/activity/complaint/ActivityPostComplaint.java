package com.adslinfosoft.softberry.activity.complaint;

import android.annotation.SuppressLint;
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
import com.adslinfosoft.softberry.app.Softberry;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityPostComplaint extends AppCompatActivity {
    private CircularProgressView progressView;
    private EditText etDate, etDescr;
    private TextView tvJobNo, tvIssue;
    private static final String TAG = "ActivityPostComplaint";
    private static SimpleDateFormat mFormat;
    private SpinnerDialog spJobNo, spIssue;
    private RelativeLayout mRelJobs, mRelIssue;
    private Button mbtnSubmit;
    private String jobNo, issue;
    private ArrayList<String> issueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_complaint);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.title);
        title.setText("Post Complaint");
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
        etDate = findViewById(R.id.et_date);
        etDescr = findViewById(R.id.et_descr);
        mRelJobs = findViewById(R.id.relJobNo);
        mRelIssue = findViewById(R.id.relIssues);
        tvJobNo = findViewById(R.id.txt_jobNo);
        tvIssue = findViewById(R.id.txt_Issue);
        mbtnSubmit = findViewById(R.id.btn_submit);
        mFormat = new SimpleDateFormat("MM/dd/yyyy");
        etDate.setText(mFormat.format(new Date()));
        issueList = new ArrayList<String>();
        addDataInListState();
        mbtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
        addDataIssues();
    }

    private void validateFields() {
        if (TextUtils.isEmpty(tvJobNo.getText().toString())) {
            showToast("Please Select Job Number");
        } else {
            if (TextUtils.isEmpty(tvIssue.getText().toString())) {
                showToast("Please Select Issue");
            } else {
                Register task = new Register();
                task.execute();
            }
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    private void addDataInListState() {
        if (AppUtils.isNetworkAvailable(getApplicationContext())) {
            progressView.stopAnimation();
            progressView.setVisibility(View.GONE);
            String url = API.GET_JOB_NUMBERS;
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
                                    "No Jobs Found", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject jsonObj = jsonArr.getJSONObject(i);
                                ListModel member = new ListModel();
                                member.setID(jsonObj.getInt("jid"));
                                member.setName(jsonObj.getString("jno"));
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
            mRelJobs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spJobNo.showSpinerDialog();
                }
            });
        }
        spJobNo = new SpinnerDialog(ActivityPostComplaint.this, stateList, "Select Job Number");
        spJobNo.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(ListModel listModel) {
                tvJobNo.setText(listModel.getName());
                jobNo = listModel.getName();
            }
        });
    }

    private void addDataIssues() {
        issueList.add("1");
        issueList.add("2");
        issueList.add("3");
        issueList.add("4");
        issueList.add("5");
        issueList.add("6");
        issueList.add("7");
        ArrayList<ListModel> stateList = new ArrayList<>();
        for (int i = 0; i < issueList.size(); i++) {
            ListModel member = new ListModel();
            member.setName(issueList.get(i));
            member.setID(i + 1);
            stateList.add(member);
        }
        if (stateList != null || stateList.size() > 0) {
            mRelIssue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spIssue.showSpinerDialog();
                }
            });
        }
        spIssue = new SpinnerDialog(ActivityPostComplaint.this, stateList, "Select Issue");
        spIssue.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(ListModel listModel) {
                tvIssue.setText(listModel.getName());
                issue = listModel.getName();
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
                params.put("jno", jobNo);
                params.put("issue", issue);
                params.put("issuedesc", etDescr.getText().toString());
                Log.e("params", String.valueOf(params));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = API.POST_COMPLAINT;
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
                                            "Complaint Submitted Successfully", Toast.LENGTH_SHORT).show();
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
