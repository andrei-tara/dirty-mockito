/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Oct 13, 2009
 */
package com.example.services;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.example.dao.SpringJpaWidgetDao;
import com.example.dao.WidgetDao;
import com.example.entities.Widget;

/**
 * An example EJB 3.0 style service
 *
 * @author Alistair A. Israel
 * @since 0.2.2
 */
public class WidgetService {

    @PersistenceContext
    private EntityManager em;

    private WidgetDao widgetDao;

    /**
     *
     */
    @SuppressWarnings("unused")
    @PostConstruct
    private void setupDao() {
        final SpringJpaWidgetDao springJpaWidgetDao = new SpringJpaWidgetDao();
        springJpaWidgetDao.setEntityManager(em);
        widgetDao = springJpaWidgetDao;
    }

    /**
     * @param id
     *        the widget id
     * @return the found {@link Widget}
     * @see com.example.dao.WidgetDao#findById(long)
     */
    public final Widget findById(final long id) {
        return widgetDao.findById(id);
    }

}
