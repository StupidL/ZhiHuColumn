package me.stupideme.zhihucolumn.view;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;

import java.util.List;

import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.model.Article;

/**
 * Created by StupidL on 2016/11/13.
 */

public class ArticleRecyclerAdapter extends RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder> {

    private List<Article> mDataSet;
    private Context mContext;

    public ArticleRecyclerAdapter(Context context, List<Article> list) {
        mDataSet = list;
        mContext = context;
    }

    @Override
    public ArticleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_article, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleRecyclerAdapter.ViewHolder holder, final int position) {
        Article article = mDataSet.get(position);
        holder.title.setText(article.getTitle());
        holder.author.setText(article.getAuthorName());
        holder.time.setText(article.getPublishedTime().substring(0, 10));
        holder.commentsCount.setText(article.getCommentsCount() + "");
        holder.likesCount.setText(article.getLikesCount() + "");

        Glide.with(mContext)
                .load(article.getTitleImageUrl())
                .crossFade()
                .signature(new StringSignature(article.getTitleImageUrl()))
                .placeholder(R.drawable.background_empty)
               // .override(300, 600)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                intent.putExtra("title", mDataSet.get(position).getTitle());
                intent.putExtra("author", mDataSet.get(position).getAuthorName());
                intent.putExtra("published_time", mDataSet.get(position).getPublishedTime());
                intent.putExtra("content", mDataSet.get(position).getContent());
                intent.putExtra("imageUrl", mDataSet.get(position).getTitleImageUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;
        private TextView author;
        private TextView time;
        private ImageButton comment;
        private TextView commentsCount;
        private ImageButton like;
        private TextView likesCount;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.article_background);
            title = (TextView) itemView.findViewById(R.id.article_title);
            author = (TextView) itemView.findViewById(R.id.article_author);
            time = (TextView) itemView.findViewById(R.id.article_days_ago);
            comment = (ImageButton) itemView.findViewById(R.id.article_comments_image);
            commentsCount = (TextView) itemView.findViewById(R.id.article_comments_number);
            like = (ImageButton) itemView.findViewById(R.id.article_thumb_image);
            likesCount = (TextView) itemView.findViewById(R.id.article_thumb_number);
        }
    }
}
