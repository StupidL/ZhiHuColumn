package me.stupideme.zhihucolumn.view;

import me.stupideme.zhihucolumn.model.ColumnObserver;

/**
 * Created by StupidL on 2016/11/12.
 */

public interface IColumnsView extends ColumnObserver {
    void startColumnRefresh();

    void stopColumnRefresh();
}
