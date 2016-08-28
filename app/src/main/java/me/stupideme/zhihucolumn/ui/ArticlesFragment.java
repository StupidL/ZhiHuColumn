package me.stupideme.zhihucolumn.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.stupideme.zhihucolumn.App;
import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.bean.Article;
import me.stupideme.zhihucolumn.util.ColumnName;
import me.stupideme.zhihucolumn.util.ParseJsonUtil;

public class ArticlesFragment extends Fragment {

    private RecyclerViewAdapter mAdapter;
    private int imageWidth;
    private String mColumnName;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog dialog;
    private Handler handler;
    private int limit = 50;
    private int offset = 0;

    public ArticlesFragment() {
        // Required empty public constructor
    }

    public static ArticlesFragment newInstance() {
        return new ArticlesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("加载数据");
        dialog.setMessage("正在加载数据，请稍等...");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(true);
        dialog.show();
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = getActivity().getWindowManager();
        wm.getDefaultDisplay().getMetrics(metric);
        imageWidth = metric.widthPixels;
        Bundle bundle = getArguments();
        mColumnName = bundle.getString("columnName");
        mAdapter = new RecyclerViewAdapter(R.layout.item_article, App.articlesList);
        handler = new Handler();
        new GetArticlesTask().execute();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1500);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_articals, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_all_articles);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        int space = getResources().getDimensionPixelSize(R.dimen.space);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(space));
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private void parse(String name) throws IOException, JSONException {
        String url = ColumnName.BASE_URL + name + "/posts?limit=" + limit + "&offset=" + offset;
        Request request = new Request.Builder().url(url).build();
        Response response = new OkHttpClient().newCall(request).execute();
        String body = response.body().string();
        int code = response.code();
        System.out.println(code + " " + body);
        if (response.isSuccessful()) {
            JSONArray articles = new JSONArray(body);
            for (int i = 0; i < articles.length(); i++) {
                JSONObject object = articles.getJSONObject(i);
                Article article = ParseJsonUtil.parseJsonToArticle(object);
                App.articlesList.add(article);
                System.out.println("SUCCESS!");
                System.out.println(App.articlesList.size());
            }
        }
    }

    private class GetArticlesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                parse(mColumnName);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private List<Article> mList;
        private int layoutId;

        RecyclerViewAdapter(int layoutId, List<Article> list) {
            this.layoutId = layoutId;
            this.mList = list;
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(layoutId, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, int position) {
            Article article = mList.get(position);
            String url = article.getTitleImage();
            System.out.println("=====" + position + "====+" + url + "+");
            if (url == null || url.isEmpty()) {
                holder.image.setImageResource(R.drawable.background_empty);
                System.out.println("====null===");
            } else {
                Glide.with(getActivity()).load(url).into(holder.image);
            }
            holder.title.setText(article.getTitle());
            holder.author.setText(article.getAuthor().getName());
            String time = article.getPublishedTime();
            String t = time.substring(0, 10);
            holder.time.setText(t);
            holder.commentCount.setText(String.valueOf(article.getCommentsCount()));
            holder.thumbCount.setText(String.valueOf(article.getLikesCount()));
            holder.image.setMaxWidth(imageWidth);
            holder.image.setMinimumWidth(imageWidth);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                    intent.putExtra("position", holder.getAdapterPosition());
                    getActivity().startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView image;
            private TextView title;
            private TextView author;
            private TextView time;
            private ImageButton comment;
            private TextView commentCount;
            private ImageButton thumb;
            private TextView thumbCount;

            ViewHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.article_background);
                title = (TextView) itemView.findViewById(R.id.article_title);
                author = (TextView) itemView.findViewById(R.id.article_author);
                time = (TextView) itemView.findViewById(R.id.article_days_ago);
                comment = (ImageButton) itemView.findViewById(R.id.article_comments_image);
                commentCount = (TextView) itemView.findViewById(R.id.article_comments_number);
                thumb = (ImageButton) itemView.findViewById(R.id.article_thumb_image);
                thumbCount = (TextView) itemView.findViewById(R.id.article_thumb_number);
            }
        }
    }


}
