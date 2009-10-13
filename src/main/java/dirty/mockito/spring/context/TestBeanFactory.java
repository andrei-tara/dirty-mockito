/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Oct 13, 2009
 */
package dirty.mockito.spring.context;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;

/**
 * A simple extension of {@link DefaultListableBeanFactory} that initializes the
 * common/useful bean post processors.
 *
 * @author Alistair A. Israel
 */
public class TestBeanFactory extends DefaultListableBeanFactory {

    /**
     *
     */
    public TestBeanFactory() {
        final AutowiredAnnotationBeanPostProcessor aabpp = new AutowiredAnnotationBeanPostProcessor();
        aabpp.setBeanFactory(this);
        addBeanPostProcessor(aabpp);

        final PersistenceAnnotationBeanPostProcessor pabpp = new PersistenceAnnotationBeanPostProcessor();
        pabpp.setBeanFactory(this);
        addBeanPostProcessor(pabpp);

        final CommonAnnotationBeanPostProcessor cabpp = new CommonAnnotationBeanPostProcessor();
        cabpp.setBeanFactory(this);
        addBeanPostProcessor(cabpp);
    }
}
