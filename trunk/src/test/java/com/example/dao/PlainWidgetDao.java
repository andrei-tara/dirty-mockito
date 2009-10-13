/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Oct 12, 2009
 */
package com.example.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.example.entities.Widget;

/**
 * @author Alistair A. Israel
 * @since 0.2.1
 */
public class PlainWidgetDao implements WidgetDao {

    @PersistenceContext
    private EntityManager em;

    /**
     * {@inheritDoc}
     *
     * @see com.example.dao.WidgetDao#findById(long)
     */
    public final Widget findById(final long id) {
        final Query query = em.createNamedQuery(Widget.FIND_BY_ID);
        query.setParameter("id", id);
        return (Widget) query.getSingleResult();
    }

}
