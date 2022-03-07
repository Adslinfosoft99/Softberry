package com.adslinfosoft.softberry.frag;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.API.API;
import com.adslinfosoft.softberry.adapter.SimpleAdapter;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.model.Job;
import com.adslinfosoft.softberry.model.Product;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.Utils.AppUtils;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

public class HomeFragment extends Fragment {

    private ThemedToggleButtonGroup btnGrp;
    private RecyclerView mList;
    private CircularProgressView progressView;
    private static final String TAG = "HomeFragment";
    private TextView tvNoJobs, tvOngoing, tvCompleted, tvName;
    private int pendingNo, confirmNo;
    private SimpleAdapter mAdapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        btnGrp = view.findViewById(R.id.time);
        btnGrp.selectButton(R.id.btn1);
        btnGrp.setOnSelectListener((ThemedButton btn) -> {
            fetchDetailsRequest(btn.getText());
            return kotlin.Unit.INSTANCE;
        });

        progressView = view.findViewById(R.id.progress_view);
        mList = view.findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mList.getRecycledViewPool().setMaxRecycledViews(0, 0);

        tvNoJobs = view.findViewById(R.id.txtNoJob);
        tvOngoing = view.findViewById(R.id.num_txt);
        tvCompleted = view.findViewById(R.id.num_txt2);
        tvName = view.findViewById(R.id.txtName);

        tvName.setText(Softberry.getPreference().getString(AppConstants.CLIENT_NAME, "") + "!");

        fetchDetailsRequest("Ongoing Task");

        return view;
    }

    private void fetchDetailsRequest(String type) {

        if (AppUtils.isNetworkAvailable(getActivity())) {
            progressView.startAnimation();
            progressView.setVisibility(View.VISIBLE);
            String url = API.GET_JOB_LIST;

            StringRequest jsonObjRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        pendingNo = 0;
                        confirmNo = 0;
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
                                job.setJobId(jsonObj.getInt("jid"));
                                job.setcId(jsonObj.getInt("cid"));
                                job.setJobNo(jsonObj.getString("jno"));
                                job.setDate(jsonObj.getString("jdate"));
                                job.setStatus(jsonObj.getString("jstatus"));
                                ArrayList<Product> productList = new ArrayList<>();
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                    JSONArray jsonArr2 = jsonObj.getJSONArray("getjobproducts");
                                    for (int j = 0; j < jsonArr2.length(); j++) {
                                        JSONObject jsonObj2 = jsonArr2.getJSONObject(j);
                                        Product product = new Product();
                                        product.setProductName(jsonObj2.getString("productname"));
                                        productList.add(product);
                                        Log.e("Name", "" + jsonObj2.getString("productname"));
                                    }
                                }
                                job.setProducts(productList);
                                if (jsonObj.getString("jstatus").equalsIgnoreCase("Delivered")) {
                                    confirmNo++;
                                } else {
                                    pendingNo++;
                                }
                                if (type.contains("Ongoing Task") && !jsonObj.getString("jstatus").equalsIgnoreCase("Delivered")) {
                                    job.setProducts(productList);
                                    jobList.add(job);
                                } else {
                                    if (type.contains("Completed Task")) {
                                        if (jsonObj.getString("jstatus").equalsIgnoreCase("Delivered")) {
                                            job.setProducts(productList);
                                            jobList.add(job);
                                        }
                                    }
                                }
                            }
                            progressView.stopAnimation();
                            progressView.setVisibility(View.GONE);
                            setData();
                            reloadData(jobList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),
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
            Softberry.getInstance().addToRequestQueue(jsonObjRequest);
        } else {
            Toast.makeText(getActivity(),
                    "Error! Please check your net connection.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setData() {
        tvOngoing.setText("" + pendingNo);
        tvCompleted.setText("" + confirmNo);
    }

    private void reloadData(ArrayList<Job> jobList) {
        mList.setVisibility(View.VISIBLE);
        mAdapter = new SimpleAdapter(getActivity(), jobList);
        mList.setAdapter(mAdapter);
    }

}
