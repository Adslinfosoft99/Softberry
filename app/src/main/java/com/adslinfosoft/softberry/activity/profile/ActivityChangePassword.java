package com.adslinfosoft.softberry.activity.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.API.API;
import com.adslinfosoft.softberry.activity.login.ActivityLogin;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.raywenderlich.android.validatetor.ValidateTor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityChangePassword extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtOldPass, mEtNeWPass, mEtConfirmPass;
    private Button mBtSubmit;
    private static final String TAG = "ActivityChangePassword";
    private ValidateTor validateTor;
    private CircularProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.title);
        title.setText("Change Password");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.loading_color)));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actionbar_indicator);
        validateTor = new ValidateTor();
        progressView = findViewById(R.id.progress_view);
        mEtOldPass = findViewById(R.id.et_current_pass);
        mEtNeWPass = findViewById(R.id.et_new_pass);
        mEtConfirmPass = findViewById(R.id.et_confirm_pass);
        mBtSubmit = findViewById(R.id.btn_submit);
        mBtSubmit.setOnClickListener(this);

        hideKeyPad();
    }

    public void hideKeyPad() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void validateChangePass() {
        String oldPass = mEtOldPass.getText().toString();
        String newPass = mEtNeWPass.getText().toString();
        String confirmPass = mEtConfirmPass.getText().toString();
        String pass = Softberry.getPreference().getString(AppConstants.PASSWORD, "");

        if (TextUtils.isEmpty(oldPass)) {
            mEtOldPass.setError(getString(R.string.txt_EnterOldpass));
        } else if (!oldPass.equals(pass)) {
            mEtOldPass.setError(getString(R.string.txt_EnterCurrpass));
        } else {
            if (validateTor.isAtleastLength(newPass, 4)) {
                if (TextUtils.isEmpty(confirmPass)) {
                    mEtConfirmPass.setError(getString(R.string.txt_EnterRepass));
                } else {
                    if (newPass.equals(confirmPass)) {
                        Register task = new Register();
                        task.execute();
                    } else {
                        mEtConfirmPass.setText("");
                        mEtConfirmPass.setError(getString(R.string.txt_PassNotMatch));
                    }
                }
            } else {
                mEtNeWPass.setError("Password needs to be of minimum length of 4 characters");
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                validateChangePass();
                break;
            default:
                break;
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
                params.put("currentpassword", mEtOldPass.getText().toString());
                params.put("newpassword", mEtNeWPass.getText().toString());
                Log.e("params", String.valueOf(params));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = API.CHANGE_PASSWORD;
            JsonObjectRequest req = new JsonObjectRequest(url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                progressView.stopAnimation();
                                progressView.setVisibility(View.GONE);
                                String msg = response.getString("message");
                                if (msg.equals("success")) {
                                    SharedPreferences.Editor edit = Softberry.getPreference().edit();
                                    edit.putString(AppConstants.USER_NAME, "");
                                    edit.putString(AppConstants.PASSWORD, "");
                                    edit.commit();
                                    Softberry.getPreference().edit().putBoolean(AppConstants.PREF_REMEMBER, false).commit();
                                    Toast.makeText(getApplicationContext(),
                                            "Password Changed Successfully.", Toast.LENGTH_SHORT).show();
                                    finishAffinity();
                                    startHomeScreen();
                                } else if (msg.equals("invalid current password")) {
                                    Toast.makeText(getApplicationContext(),
                                            "Invalid Current Password", Toast.LENGTH_SHORT).show();
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

    private void startHomeScreen() {
        Intent mainIntent = new Intent(this, ActivityLogin.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
    }
}
