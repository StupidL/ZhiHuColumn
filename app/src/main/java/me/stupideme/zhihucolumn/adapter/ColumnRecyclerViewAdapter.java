package me.stupideme.zhihucolumn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.bean.Column;
import me.stupideme.zhihucolumn.ui.ProfileActivity;

/**
 * Created by StupidL on 2016/8/20.
 */

public class ColumnRecyclerViewAdapter extends RecyclerView.Adapter<ColumnRecyclerViewAdapter.ViewHolder> {

    private List<Column> mList;
    private Context mContext;
    private int layoutId;

    public ColumnRecyclerViewAdapter(Context context, List<Column> list, int resLayoutId) {
        this.mList = list;
        this.mContext = context;
        this.layoutId = resLayoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Column model = mList.get(position);
        holder.setIsRecyclable(true);
        Glide.with(mContext).load(model.getAvatar().getTemplate()).into(holder.avatar);
        holder.author.setText(model.getAuthor().getName());
        holder.title.setText(model.getName());
        holder.info.setText(model.getFollowerCount() + " 关注\t" + model.getPostCount() + " 文章");
        holder.summary.setText(model.getDescription());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ProfileActivity.class));
            }
        };
        holder.title.setOnClickListener(listener);
        holder.summary.setOnClickListener(listener);
        holder.info.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(Column model, int position) {
        mList.add(position, model);
        notifyItemInserted(position);
    }

    public void remove(Column model) {
        int position = mList.indexOf(model);
        mList.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView avatar;
        private TextView author;
        private TextView title;
        private TextView info;
        private TextView summary;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.author_avatar);
            author = (TextView) itemView.findViewById(R.id.author_name);
            title = (TextView) itemView.findViewById(R.id.column_title);
            info = (TextView) itemView.findViewById(R.id.column_info);
            summary = (TextView) itemView.findViewById(R.id.column_summary);
        }
    }
}
