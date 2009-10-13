/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Oct 13, 2009
 */
package com.example.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.orm.jpa.support.JpaDaoSupport;

import com.example.entities.Widget;

/**
 * Implementation of {@link WidgetDao} that extends Spring's
 * {@link JpaDaoSupport}, used to test @PostConstruct post-processing in
 * {@link com.example.services.WidgetService}.
 *
 * @author Alistair A. Israel
 */
public class SpringJpaWidgetDao extends JpaDaoSupport implements WidgetDao {

    /**
     * {@inheritDoc}
     *
     * @see com.example.dao.WidgetDao#findById(long)
     */
    @SuppressWarnings("unchecked")
    public final Widget findById(final long id) {
        final Map<String, Long> params = Collections.singletonMap("id", id);
        final List<Widget> list = getJpaTemplate()
                .findByNamedQueryAndNamedParams(Widget.FIND_BY_ID, params);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
