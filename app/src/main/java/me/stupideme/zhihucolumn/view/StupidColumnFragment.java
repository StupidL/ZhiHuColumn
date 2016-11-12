package me.stupideme.zhihucolumn.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.model.Column;
import me.stupideme.zhihucolumn.presenter.ColumnsPresenter;
import me.stupideme.zhihucolumn.util.ColumnName;


public class StupidColumnFragment extends Fragment implements IColumnsView{

    private ColumnsPresenter mPresenter;
    private StupidRecyclerAdapter mAdapter;
    private List<Column> mDataSet;

    public StupidColumnFragment() {
        // Required empty public constructor
    }


    public static StupidColumnFragment newInstance() {
        return new StupidColumnFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = ColumnsPresenter.getInstance(this);
        mPresenter.requestColumns(ColumnName.NAMES);
        mDataSet = new ArrayList<>();
        mAdapter = new StupidRecyclerAdapter(getActivity(),mDataSet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_stupid_column, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_columns);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void refresh(Set<Column> set) {
        mDataSet.addAll(set);
        mAdapter.notifyDataSetChanged();
    }
}
