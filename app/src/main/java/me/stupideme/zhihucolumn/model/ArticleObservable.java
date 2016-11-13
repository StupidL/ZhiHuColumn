package me.stupideme.zhihucolumn.model;

/**
 * Created by StupidL on 2016/11/13.
 */

public interface ArticleObservable {
    void attach(ArticleObserver observer);

    void detach(ArticleObserver observer);

    void notifyArticleObservers(Article article);
}
