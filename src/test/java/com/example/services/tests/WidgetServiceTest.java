/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Oct 13, 2009
 */
package com.example.services.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;
import org.mockito.Mock;

import com.example.entities.Widget;
import com.example.services.WidgetService;

import dirty.mockito.ActiveTest;

/**
 * Ostensibly a test for {@link WidgetService}, but actually exercises @PersistenceContext
 * injection and @PostConstruct annotation handling.
 *
 * @author Alistair A. Israel
 * @since 0.2.2
 */
public final class WidgetServiceTest extends ActiveTest<WidgetService> {

    @Mock
    private EntityManager em;

    private WidgetService service;

    /**
     * Test for {@link WidgetService#findById(long)}.
     */
    @Test
    public void testFindById() {
        final Widget widget = new Widget();
        widget.setId(123L);
        widget.setName("Example Widget");

        final Query query = mock(Query.class);
        when(query.getResultList()).thenReturn(
                Collections.singletonList(widget));
        when(em.createNamedQuery(Widget.FIND_BY_ID)).thenReturn(query);

        final Widget found = service.findById(123L);
        verify(query).setParameter("id", 123L);

        assertEquals(Long.valueOf(123L), found.getId());
    }

}
