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

import me.stupideme.zhihucolumn.Constants;
import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.model.Article;
import me.stupideme.zhihucolumn.presenter.ArticlesPresenter;


public class StupidArticlesFragment extends Fragment implements IArticlesView {

    private static final String TAG = StupidArticlesFragment.class.getSimpleName();
    private String mSlug;
    private ArticlesPresenter mPresenter;
    public List<Article> mDataSet;
    private ArticleRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int limit = 50;
    private int offset = 0;


    public StupidArticlesFragment() {
        // Required empty public constructor
    }


    public static StupidArticlesFragment newInstance(String slug) {
        StupidArticlesFragment fragment = new StupidArticlesFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_COLUMN_SLUG, slug);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSlug = getArguments().getString(Constants.EXTRA_COLUMN_SLUG);
        }
        mPresenter = ArticlesPresenter.getInstance(StupidArticlesFragment.this);
        mPresenter.attachArticleObserver(StupidArticlesFragment.this);
        mDataSet = new ArrayList<>();
        mAdapter = new ArticleRecyclerAdapter(getActivity(), mDataSet);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.requestArticles(mSlug, limit, offset);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachArticleObserver(StupidArticlesFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stupid_articles, container, false);
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
                mPresenter.requestArticles(mSlug, limit, offset);
                mAdapter.notifyDataSetChanged();
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_columns);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        int space = getResources().getDimensionPixelSize(R.dimen.space);
        recyclerView.addItemDecoration(new SpaceItemDecoration(space));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void startArticleRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopArticleRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onReceiveArticle(Article article) {
        mDataSet.add(article);
        Log.v(TAG, "receive article");
        mAdapter.notifyDataSetChanged();
    }
}
