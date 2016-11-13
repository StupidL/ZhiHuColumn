package me.stupideme.zhihucolumn.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.model.Column;
import me.stupideme.zhihucolumn.presenter.ColumnsPresenter;
import me.stupideme.zhihucolumn.util.ColumnName;


public class StupidColumnFragment extends Fragment implements IColumnsView {

    private static final String TAG = StupidColumnFragment.class.getSimpleName();
    private ColumnsPresenter mPresenter;
    private ColumnRecyclerAdapter mAdapter;
    private List<Column> mDataSet;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public StupidColumnFragment() {
        // Required empty public constructor
    }

    public static StupidColumnFragment newInstance() {
        return new StupidColumnFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = ColumnsPresenter.getInstance(StupidColumnFragment.this);
        mPresenter.attachColumnObserver(StupidColumnFragment.this);
        Log.v(TAG, "attach column observer");
        mDataSet = new ArrayList<>();
        mAdapter = new ColumnRecyclerAdapter(getActivity(), mDataSet);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.requestColumns(ColumnName.NAMES);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachColumnObserver(StupidColumnFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stupid_column, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataSet.clear();
                mPresenter.requestColumns(ColumnName.NAMES);
                mAdapter.notifyDataSetChanged();
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_columns);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onReceiveColumn(Column column) {
        mDataSet.add(column);
        Log.v(TAG, "column = " + column);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void startColumnRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopColumnRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
