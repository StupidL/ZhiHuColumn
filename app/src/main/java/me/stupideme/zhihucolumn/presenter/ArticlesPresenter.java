package me.stupideme.zhihucolumn.presenter;

import android.os.Handler;
import android.os.Message;

import java.util.HashSet;
import java.util.Set;

import me.stupideme.zhihucolumn.Constants;
import me.stupideme.zhihucolumn.model.Article;
import me.stupideme.zhihucolumn.model.INetModel;
import me.stupideme.zhihucolumn.model.NetModelImp;
import me.stupideme.zhihucolumn.view.IArticlesView;

/**
 * Created by StupidL on 2016/11/12.
 */

public class ArticlesPresenter {

    private INetModel iNetModel;
    private IArticlesView iArticlesView;
    private Set<Article> mDataSet;
    private static ArticlesPresenter INSTANCE;
    private Handler mHandler;

    private ArticlesPresenter(IArticlesView view) {
        iArticlesView = view;
        mDataSet = new HashSet<>();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                int what = message.what;
                if (what == Constants.MESSAGE_ARTICLE_RESULT) {
                    Article article = (Article) message.obj;
                    mDataSet.add(article);
                }
            }
        };
        iNetModel = NetModelImp.getInstance(mHandler);

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

    public void clearArticles() {
        mDataSet.clear();
    }

    public void requestArticles(String name, int limit, int offset) {
        iNetModel.requestArticles(name, limit, offset);
    }

    public Set<Article> getArticles() {
        return mDataSet;
    }
}
