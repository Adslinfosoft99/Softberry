package com.adslinfosoft.softberry.frag;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.adslinfosoft.softberry.adapter.DividerDecoration;
import com.adslinfosoft.softberry.model.Job;
import com.adslinfosoft.softberry.R;

import java.util.ArrayList;
import java.util.Collections;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HistoryFragment extends Fragment implements AdapterView.OnItemClickListener {

    // TODO: Customize parameter argument names
    private static final String EXTRA_HISTORY = "column-count";
    // TODO: Customize parameters
    private ArrayList<Job> mJobs;
    private OnListFragmentInteractionListener mListener;
    private int mClentId;

    private static final String CLINT_ID = "client-id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HistoryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HistoryFragment newInstance(ArrayList<Job> jobs, int clientId) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_HISTORY, jobs);
        args.putInt(CLINT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mJobs = (ArrayList<Job>) getArguments().getSerializable(EXTRA_HISTORY);
            Collections.reverse(mJobs);
            mClentId = (int) getArguments().getSerializable(CLINT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView mList = (RecyclerView) view;
//            if (mColumnCount <= 1) {
            mList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

            mList.addItemDecoration(new DividerDecoration(getActivity()));

            mList.getItemAnimator().setAddDuration(1000);
            mList.getItemAnimator().setChangeDuration(1000);
            mList.getItemAnimator().setMoveDuration(1000);
            mList.getItemAnimator().setRemoveDuration(1000);
//            SimpleAdapter mAdapter = new SimpleAdapter(getActivity(), mJobs);
//            mAdapter.setOnItemClickListener(this);
//            mList.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.e("Job Size:", "" + mJobs.size());
//        int buzzer = DataTrackApplication.getPreference().getInt(mJobs.get(position).getNo(), 0);
//        if (buzzer != 0) {
//            Intent intent = new Intent(getActivity(), GalleryActivity.class);
//            intent.putExtra("JOB_NO", mJobs.get(position).getNo());
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(getActivity(), JobDetailActivity.class);
//            intent.putExtra(JobDetailActivity.EXTRA_JOB, mJobs.get(position));
//            intent.putExtra(JobDetailActivity.CLIENT_ID, mClentId);
//            startActivity(intent);
//        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Job item);
    }
}
