/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Oct 12, 2009
 */
package dirty.mockito.spring.jpa;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;
import org.mockito.Mock;

import com.example.dao.PlainWidgetDao;
import com.example.entities.Widget;

import dirty.mockito.ActiveTest;

/**
 * Unit test that verifies that Spring's JPA annotation handling is
 * transparently and properly handled.
 *
 * @author Alistair A. Israel
 */
public final class PlainWidgetDaoTest extends ActiveTest<PlainWidgetDao> {

    @Mock
    private EntityManager em;

    private PlainWidgetDao plainWidgetDao;

    /**
     * Test for {@link PlainWidgetDao#findById(long)}.
     */
    @Test
    public void testFindById() {

        final Widget widget = new Widget();
        widget.setId(123L);
        widget.setName("Example Widget");

        final Query query = mock(Query.class);
        when(query.getSingleResult()).thenReturn(widget);
        when(em.createNamedQuery(Widget.FIND_BY_ID)).thenReturn(query);

        final Widget found = plainWidgetDao.findById(123L);
        verify(query).setParameter("id", 123L);

        assertEquals(Long.valueOf(123L), found.getId());
    }
}
