package com.adslinfosoft.softberry.activity.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.adslinfosoft.softberry.API.API;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.activity.detail.adapter.ChatAdapter;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.model.Chat;
import com.adslinfosoft.softberry.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatDialog extends BottomSheetDialogFragment {
    public static final String TAG = "example_dialog";
    private static int mjId;
    private static Context mContext;
    private static String mjobNo;
    private RecyclerView mList;
    private CircularProgressView progressView;
    private EditText tvMsg;
    private ImageButton btnSend;

    public static ChatDialog newInstance(Context context, int jId, String jobNo) {
        mjobNo = jobNo;
        mjId = jId;
        mContext = context;
        return new ChatDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.example_dialog, container, false);
        progressView = view.findViewById(R.id.progress_view);
        mList = view.findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mList.getRecycledViewPool().setMaxRecycledViews(0, 0);
        tvMsg = view.findViewById(R.id.messageSend);
        btnSend = view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = tvMsg.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(getActivity(), "Error! Please Enter Message", Toast.LENGTH_SHORT).show();
                } else {
                    sendChat task = new sendChat(tvMsg.getText().toString());
                    task.execute();
                }
            }
        });
        Button btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Register task = new Register();
        task.execute();
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setDraggable(true);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
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
            String url = API.GET_JOB_CHATS;
            StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response: " + response);
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);
                    parseData(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getActivity(),
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

                @Override
                public byte[] getBody() {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("jid", String.valueOf(mjId));
                    params.put("jno", mjobNo);
                    Log.e(TAG, "params: " + params);
                    return new JSONObject(params).toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

            Softberry.getInstance().addToRequestQueue(jsonObjRequest);
            return status;
        }
    }

    private void parseData(String response) {
        ArrayList<Chat> fileList2 = new ArrayList<>();
        try {
            JSONArray jsonArr = new JSONArray(response);
            for (int j = 0; j < jsonArr.length(); j++) {
                JSONObject jsonObj = jsonArr.getJSONObject(j);
                Chat product = new Chat();
                product.setCacId(jsonObj.getInt("cacid"));
                product.setMessage(jsonObj.getString("message"));
                product.setMessageusertype(jsonObj.getString("messageusertype"));
                product.setAdminname(jsonObj.getString("adminname"));
                product.setClientname(jsonObj.getString("clientname"));
                product.setMessagedate(jsonObj.getString("messagedate"));
                fileList2.add(product);
            }
            ChatAdapter mAdapter = new ChatAdapter(mContext, fileList2);
            mList.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mList.scrollToPosition(fileList2.size() - 1);
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private class sendChat extends AsyncTask<String, String, String> {
        String mStr;

        public sendChat(String str) {
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
                params.put("jid", mjId);
                params.put("jno", mjobNo);
                params.put("message", mStr);
                Log.e("params", String.valueOf(params));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = API.SEND_MESSAGE;
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
                                    reloadChat();
                                } else if (msg.equals("some error occurred")) {
                                    Toast.makeText(getActivity(),
                                            "Some error occurred, try again later.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getActivity(),
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
            // disabling retry policy so that it won't make
            // multiple http calls
            int socketTimeout = 0;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            req.setRetryPolicy(policy);
            Softberry.getInstance().addToRequestQueue(req);
            return status;
        }
    }

    private void reloadChat() {
        tvMsg.setText("");
        tvMsg.setHint("Type Message..");
        Register task = new Register();
        task.execute();
    }

}
