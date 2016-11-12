package me.stupideme.zhihucolumn.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.stupideme.zhihucolumn.App;
import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.model.Column;
import me.stupideme.zhihucolumn.util.ColumnName;
import me.stupideme.zhihucolumn.util.ParseJsonUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ColumnsActivity extends AppCompatActivity {

    private ColumnsFragment mFragment;
    private Handler handler;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();

        dialog = new ProgressDialog(this);
        dialog.setTitle("加载数据");
        dialog.setMessage("正在加载数据，请稍等...");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(true);
        dialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 3000);

        mFragment = new ColumnsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mFragment)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.columnsList.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ColumnsFragment extends Fragment {

        private static RecyclerViewAdapter mAdapter;

        public ColumnsFragment() {
            // Required empty public constructor
        }

        public static ColumnsFragment newInstance() {
            return new ColumnsFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            mAdapter = new RecyclerViewAdapter(App.columnsList, R.layout.item_column);
            new GetColumnTask().execute();
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

        private void parse(String columnName) throws IOException {
            String url = ColumnName.BASE_URL + columnName;
            Request request = new Request.Builder().url(url).build();
            Response response = new OkHttpClient().newCall(request).execute();
            String body = response.body().string();
            if (response.isSuccessful()) {
                Column column = ParseJsonUtil.parseJsonToColumn(body);
                App.columnsList.add(column);
            }
        }

        public class GetColumnTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    for (int i = 0; i < ColumnName.NAMES.length; i++) {
                        parse(ColumnName.NAMES[i]);
                    }
                } catch (IOException e) {
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
            private List<Column> mList;
            private int layoutId;

            public RecyclerViewAdapter(List<Column> list, int resLayoutId) {
                this.mList = list;
                this.layoutId = resLayoutId;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                System.out.println(getActivity());
                System.out.println(layoutId);
                View view = LayoutInflater.from(getActivity()).inflate(layoutId, null);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                Column model = mList.get(position);
                String url = model.getAvatarUrl();
                Glide.with(getActivity()).load(url).into(holder.avatar);
                System.out.println("AVATAR loaded");
                holder.author.setText(model.getAuthorName());
                holder.title.setText(model.getName());
                holder.info.setText(model.getFollowerCount() + " 关注\t" + model.getPostCount() + " 文章");
                holder.summary.setText(model.getDescription());
                Column column = App.columnsList.get(position);
//                final String columnName = column.getSlug();
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ArticlesActivity.class);
//                        intent.putExtra("columnName", columnName);
                        getActivity().startActivity(intent);
                        App.articlesList.clear();
                    }
                };
                holder.ll.setOnClickListener(listener);
            }

            @Override
            public int getItemCount() {
                return mList.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                private LinearLayout ll;
                private CircleImageView avatar;
                private TextView author;
                private TextView title;
                private TextView info;
                private TextView summary;

                ViewHolder(View itemView) {
                    super(itemView);
                    ll = (LinearLayout) itemView.findViewById(R.id.linear_layout);
                    avatar = (CircleImageView) itemView.findViewById(R.id.author_avatar);
                    author = (TextView) itemView.findViewById(R.id.author_name);
                    title = (TextView) itemView.findViewById(R.id.column_title);
                    info = (TextView) itemView.findViewById(R.id.column_info);
                    summary = (TextView) itemView.findViewById(R.id.column_summary);
                }
            }
        }

    }
}
