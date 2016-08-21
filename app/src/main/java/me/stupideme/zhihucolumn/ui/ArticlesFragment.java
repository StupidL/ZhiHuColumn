package me.stupideme.zhihucolumn.ui;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import me.stupideme.zhihucolumn.App;
import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.adapter.ArticleRecyclerViewAdapter;

public class ArticlesFragment extends Fragment {

    private ArticleRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public ArticlesFragment() {
        // Required empty public constructor
        mAdapter = new ArticleRecyclerViewAdapter(getActivity(), R.layout.item_article, App.articlesList);
    }

    public static ArticlesFragment newInstance(String param1, String param2) {
        ArticlesFragment fragment = new ArticlesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        new GetArticlesTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_articals, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_all_articles);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public class GetArticlesTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Request request = new Request.Builder().url("http://zhuanlan.zhihu.com/api/columns/wooyun/posts?limit=2&offset=0").build();
            try {
                Response response = new OkHttpClient().newCall(request).execute();
                if (response.isSuccessful()) {
                    System.out.println(response.code());
                    System.out.println(response.body().string());
                    //TODO: parse Json

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            mAdapter.notifyDataSetChanged();
        }
    }

}
