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

/**
 * A simple extension of {@link DefaultListableBeanFactory} that initializes the
 * common/useful bean post processors.
 *
 * @author Alistair A. Israel
 * @since 0.2.2
 */
public class TestBeanFactory extends DefaultListableBeanFactory {

    /**
     *
     */
    public TestBeanFactory() {
        final AutowiredAnnotationBeanPostProcessor aabpp = new AutowiredAnnotationBeanPostProcessor();
        aabpp.setBeanFactory(this);
        addBeanPostProcessor(aabpp);

        final CommonAnnotationBeanPostProcessor cabpp = new CommonAnnotationBeanPostProcessor();
        cabpp.setBeanFactory(this);
        addBeanPostProcessor(cabpp);
    }
}
