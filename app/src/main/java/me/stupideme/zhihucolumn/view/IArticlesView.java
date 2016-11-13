package me.stupideme.zhihucolumn.view;

import me.stupideme.zhihucolumn.model.ArticleObserver;

/**
 * Created by StupidL on 2016/11/12.
 */

public interface IArticlesView extends ArticleObserver {
    void startArticleRefresh();

    void stopArticleRefresh();
}
