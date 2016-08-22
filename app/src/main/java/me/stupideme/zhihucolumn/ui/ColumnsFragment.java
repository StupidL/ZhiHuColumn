package me.stupideme.zhihucolumn.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.stupideme.zhihucolumn.App;
import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.bean.Column;
import me.stupideme.zhihucolumn.util.ColumnName;
import me.stupideme.zhihucolumn.util.ParseJsonUtil;

public class ColumnsFragment extends Fragment {

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
            Column column = ParseJsonUtil.ParseJsonToColumn(body);
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
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            System.out.println(getActivity());
            System.out.println(layoutId);
            View view = LayoutInflater.from(getActivity()).inflate(layoutId, null);
            return new RecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
            Column model = mList.get(position);
            String id = model.getAvatar().getId();
            String url = "https://pic1.zhimg.com/" + id + "_m.jpg";
            Glide.with(getActivity()).load(url).into(holder.avatar);
            System.out.println("AVATAR loaded");
            holder.author.setText(model.getCreator().getName());
            holder.title.setText(model.getName());
            holder.info.setText(model.getFollowerCount() + " 关注\t" + model.getPostCount() + " 文章");
            holder.summary.setText(model.getDescription());
            Column column = App.columnsList.get(position);
            final String columnName = column.getSlug();
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ArticlesActivity.class);
                    intent.putExtra("columnName", columnName);
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
