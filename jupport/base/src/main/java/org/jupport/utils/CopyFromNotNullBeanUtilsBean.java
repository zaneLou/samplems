package org.jupport.utils;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ContextClassLoaderLocal;

public class CopyFromNotNullBeanUtilsBean extends BeanUtilsBean {

    /**
     * Contains <code>BeanUtilsBean</code> instances indexed by context classloader.
     */
    private static final ContextClassLoaderLocal<CopyFromNotNullBeanUtilsBean>
            BEANS_BY_CLASSLOADER = new ContextClassLoaderLocal<CopyFromNotNullBeanUtilsBean>() {
                        // Creates the default instance used when the context classloader is unavailable
                        @Override
                        protected CopyFromNotNullBeanUtilsBean initialValue() {
                            return new CopyFromNotNullBeanUtilsBean();
                        }
                    };

    /**
     * Gets the instance which provides the functionality for {@link BeanUtils}.
     * This is a pseudo-singleton - an single instance is provided per (thread) context classloader.
     * This mechanism provides isolation for web apps deployed in the same container.
     *
     * @return The (pseudo-singleton) BeanUtils bean instance
     */
    public static CopyFromNotNullBeanUtilsBean getInstance() {
        return BEANS_BY_CLASSLOADER.get();
    }
    
	@Override
	public void copyProperty(Object bean, String name, Object value)
			throws IllegalAccessException, InvocationTargetException {
		if (value == null) {
			return;
		}
		super.copyProperty(bean, name, value);
	}
}