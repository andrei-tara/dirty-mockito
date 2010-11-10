/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Oct 12, 2009
 */
package dirty.mockito.spring.jpa;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * <p>
 * Implements {@link EntityManagerFactory} by merely returning an existing
 * (mock) {@link EntityManager}.
 * </p>
 * <p>
 * Used to register an {@link EntityManagerFactory} bean to the Spring context
 * at test time to enable Spring's
 * {@link org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor}
 * to work transparently.
 * </p>
 *
 * @author Alistair A. Israel
 * @since 0.2.1
 */
public class MockEntityManagerFactory implements EntityManagerFactory {

    private final EntityManager mockEntityManager;

    private boolean isOpen = true;

    /**
     * @param mockEntityManager
     *        the mock {@link EntityManager}
     */
    public MockEntityManagerFactory(final EntityManager mockEntityManager) {
        this.mockEntityManager = mockEntityManager;
    }

    /**
     * {@inheritDoc}
     *
     * @see javax.persistence.EntityManagerFactory#close()
     */
    public final void close() {
        isOpen = false;
    }

    /**
     * {@inheritDoc}
     *
     * @see javax.persistence.EntityManagerFactory#createEntityManager()
     */
    public final EntityManager createEntityManager() {
        return mockEntityManager;
    }

    /**
     * {@inheritDoc}
     *
     * @see javax.persistence.EntityManagerFactory#createEntityManager(java.util.Map)
     */
    public final EntityManager createEntityManager(@SuppressWarnings("rawtypes") final Map map) {
        return mockEntityManager;
    }

    /**
     * {@inheritDoc}
     *
     * @see javax.persistence.EntityManagerFactory#isOpen()
     */
    public final boolean isOpen() {
        return this.isOpen;
    }
}
