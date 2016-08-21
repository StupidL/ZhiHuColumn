package me.stupideme.zhihucolumn.ui;

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

    public ColumnsFragment() {
        // Required empty public constructor
        mAdapter = new ColumnRecyclerViewAdapter(getActivity(), App.columnsList, R.layout.item_column);
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
}
