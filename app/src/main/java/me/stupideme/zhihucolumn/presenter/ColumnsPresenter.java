package me.stupideme.zhihucolumn.presenter;

import me.stupideme.zhihucolumn.model.ColumnObserver;
import me.stupideme.zhihucolumn.model.INetModel;
import me.stupideme.zhihucolumn.model.NetModelImp;
import me.stupideme.zhihucolumn.view.IColumnsView;

/**
 * Created by StupidL on 2016/11/12.
 */

public class ColumnsPresenter {

    private INetModel iNetModel;
    private IColumnsView iColumnsView;
    private static ColumnsPresenter INSTANCE;

    private ColumnsPresenter(IColumnsView view) {
        iColumnsView = view;
        iNetModel = NetModelImp.getInstance();
    }

    public static ColumnsPresenter getInstance(IColumnsView view) {
        if (INSTANCE == null) {
            synchronized (ColumnsPresenter.class) {
                if (INSTANCE == null)
                    INSTANCE = new ColumnsPresenter(view);
            }
        }
        return INSTANCE;
    }

    public void attachColumnObserver(ColumnObserver observer) {
        iNetModel.attach(observer);
    }

    public void detachColumnObserver(ColumnObserver observer) {
        iNetModel.detach(observer);
    }

    public void requestColumns(String[] names) {
        iColumnsView.startColumnRefresh();
        for (String name : names)
            iNetModel.requestColumn(name);
        iColumnsView.stopColumnRefresh();
    }

}
