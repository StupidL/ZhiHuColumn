package me.stupideme.zhihucolumn.view;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.stupideme.zhihucolumn.Constants;
import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.model.Column;

/**
 * Created by StupidL on 2016/11/12.
 */

public class StupidRecyclerAdapter extends RecyclerView.Adapter<StupidRecyclerAdapter.ViewHolder> {

    private List<Column> mDataSet;
    private Context mContext;

    public StupidRecyclerAdapter(Context context, List<Column> set) {
        mContext = context;
        mDataSet = set;
    }

    @Override
    public StupidRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_column, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StupidRecyclerAdapter.ViewHolder holder, int position) {
        final Column column = mDataSet.get(position);
        holder.title.setText(column.getName());
        holder.author.setText(column.getAuthorName());
        holder.info.setText(column.getFollowerCount() + "关注 " + column.getPostCount() + " 文章");
        holder.summary.setText(column.getDescription());

        //TODO: use cache
        Glide.with(mContext).load(column.getAvatarUrl()).into(holder.avatar);

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StupidArticlesActivity.class);
                intent.putExtra(Constants.EXTRA_COLUMN_SLUG, column.getSlug());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll;
        private CircleImageView avatar;
        private TextView author;
        private TextView title;
        private TextView info;
        private TextView summary;

        public ViewHolder(View itemView) {
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
