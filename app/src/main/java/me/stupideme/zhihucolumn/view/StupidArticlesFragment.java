package me.stupideme.zhihucolumn.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.stupideme.zhihucolumn.R;

public class StupidArticlesFragment extends Fragment {

    public StupidArticlesFragment() {
        // Required empty public constructor
    }


    public static StupidArticlesFragment newInstance() {
        StupidArticlesFragment fragment = new StupidArticlesFragment();

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
        return inflater.inflate(R.layout.fragment_stupid_articles, container, false);
    }

}
