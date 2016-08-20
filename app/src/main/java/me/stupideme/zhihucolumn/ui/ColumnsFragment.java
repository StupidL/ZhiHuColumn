package me.stupideme.zhihucolumn.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.stupideme.zhihucolumn.App;
import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.adapter.ColumnRecyclerViewAdapter;

public class ColumnsFragment extends Fragment {

    private static ColumnRecyclerViewAdapter mAdapter;
    public ColumnReceiver mReceiver;
    public static final String COLUMN_ACTION = "me.stupidme.zhihucolumn.ACTION_COLUMN";

    public ColumnsFragment() {
        // Required empty public constructor
        mAdapter = new ColumnRecyclerViewAdapter(getActivity(), App.columnsList, R.layout.item_column);
        mReceiver = new ColumnReceiver();
    }

    public static ColumnsFragment newInstance() {
        ColumnsFragment fragment = new ColumnsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(COLUMN_ACTION);
        getActivity().registerReceiver(mReceiver, filter);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_columns, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_all_columns);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    public class ColumnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mAdapter.notifyDataSetChanged();
        }
    }

}
