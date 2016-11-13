package me.stupideme.zhihucolumn.presenter;

import android.util.Log;

import me.stupideme.zhihucolumn.model.ArticleObserver;
import me.stupideme.zhihucolumn.model.INetModel;
import me.stupideme.zhihucolumn.model.NetModelImp;
import me.stupideme.zhihucolumn.util.ColumnName;
import me.stupideme.zhihucolumn.view.IArticlesView;

/**
 * Created by StupidL on 2016/11/12.
 */

public class ArticlesPresenter {

    private static final String TAG = ArticlesPresenter.class.getSimpleName();
    private INetModel iNetModel;
    private IArticlesView iArticlesView;
    private static ArticlesPresenter INSTANCE;

    private ArticlesPresenter(IArticlesView view) {
        iArticlesView = view;
        iNetModel = NetModelImp.getInstance();
    }

    public static ArticlesPresenter getInstance(IArticlesView view) {
        if (INSTANCE == null) {
            synchronized (ArticlesPresenter.class) {
                if (INSTANCE == null)
                    INSTANCE = new ArticlesPresenter(view);
            }
        }
        return INSTANCE;
    }


    public void attachArticleObserver(ArticleObserver observer) {
        iNetModel.attach(observer);
    }


    public void detachArticleObserver(ArticleObserver observer) {
        iNetModel.detach(observer);
    }

    public void requestArticles(String name, int limit, int offset) {
        iArticlesView.startArticleRefresh();
        String url = ColumnName.BASE_URL + name + "/posts?limit=" + limit + "&offset=" + offset;
        Log.v(TAG, "url = " + url);
        iNetModel.requestArticles(name, url);
        iArticlesView.stopArticleRefresh();
    }

}
