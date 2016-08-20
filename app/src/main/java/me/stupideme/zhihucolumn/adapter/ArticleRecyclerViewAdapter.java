package me.stupideme.zhihucolumn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.bean.Article;
import me.stupideme.zhihucolumn.ui.ArticleDetailActivity;

/**
 * Created by StupidL on 2016/8/20.
 */

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<Article> mList;
    private int layoutId;

    public ArticleRecyclerViewAdapter(Context context, int layoutId, List<Article> list) {
        this.mContext = context;
        this.layoutId = layoutId;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Article article = mList.get(position);
        holder.setIsRecyclable(true);
        Glide.with(mContext).load(article.getTitleImage()).into(holder.image);
        holder.title.setText(article.getTitle());
        holder.author.setText(article.getAuthor().getName());
        holder.time.setText(article.getPublishedTime());// text is too long
        holder.commentCount.setText(article.getCommentsCount());
        holder.thumbCount.setText(article.getLikesCount());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                intent.putExtra("position", holder.getAdapterPosition());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(Article article, int position) {
        mList.add(position, article);
        notifyItemInserted(position);
    }

    public void remove(Article article) {
        int position = mList.indexOf(article);
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;
        private TextView author;
        private TextView time;
        private ImageButton comment;
        private TextView commentCount;
        private ImageButton thumb;
        private TextView thumbCount;

        public ViewHolder(View itemView) {
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
