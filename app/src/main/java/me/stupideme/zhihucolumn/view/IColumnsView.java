package me.stupideme.zhihucolumn.view;

import java.util.Set;

import me.stupideme.zhihucolumn.model.Column;

/**
 * Created by StupidL on 2016/11/12.
 */

public interface IColumnsView {
    void refresh(Set<Column> set);
}
