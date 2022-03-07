package com.adslinfosoft.softberry.activity.login;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.API.API;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.Utils.AppUtils;
import com.adslinfosoft.softberry.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by alokgupta on 30/08/16.
 */
public class ActivityForgotPassword extends AppCompatActivity {

    private EditText mEtEmail;
    private Button mBtSubmit;
    private static final String TAG = "ForgotPassword";
    private CircularProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.title);
        title.setText("Forgot Password");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actionbar_indicator);
        getViews();
        hideKeyPad();
    }

    public void hideKeyPad() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void getViews() {
        progressView = findViewById(R.id.progress_view);
        mEtEmail = findViewById(R.id.et_mobile);
        mBtSubmit = findViewById(R.id.btn_submit);
        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
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

    private void validate() {
        String str = mEtEmail.getText().toString();
        if (AppUtils.isEmailValid(str)) {
            Register task = new Register("username", str);
            task.execute();
        } else if (AppUtils.isValidPhoneNumber(str)) {
            Register task = new Register("email", str);
            task.execute();
        } else {
            Toast.makeText(ActivityForgotPassword.this, "Error! Please enter valid email or mobile number.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private class Register extends AsyncTask<String, String, String> {
        String mType, mStr;

        public Register(String type, String str) {
            mType = type;
            mStr = str;
        }

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
                params.put("type", mType);
                params.put("value", mStr);
                Log.e("params", String.valueOf(params));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = API.FORGOT_PAASWORD;
            JsonObjectRequest req = new JsonObjectRequest(url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("response", "" + response);
                            try {
                                progressView.stopAnimation();
                                progressView.setVisibility(View.GONE);
                                String msg = response.getString("message");
                                if (msg.equals("success")) {
                                    Toast.makeText(getApplicationContext(),
                                            "Request added Successfully.", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (msg.equals("invalid details")) {
                                    Toast.makeText(getApplicationContext(),
                                            "Invalid Details.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong. please try again later.", Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);

                }
            }) {
            };
            req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Softberry.getInstance().addToRequestQueue(req);
            return status;
        }
    }

}
