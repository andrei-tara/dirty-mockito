/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Oct 12, 2009
 */
package com.example.dao;

import com.example.entities.Widget;

/**
 * @author Alistair A. Israel
 * @since 0.2.1
 */
public interface WidgetDao {

    /**
     * Find a widget by id
     *
     * @param id
     *        the widget id
     * @return the Widget, if found
     */
    Widget findById(long id);
}
