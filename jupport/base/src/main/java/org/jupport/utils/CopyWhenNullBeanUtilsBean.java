package org.jupport.utils;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtilsBean;  
import org.apache.commons.beanutils.PropertyUtils;  
  
public class CopyWhenNullBeanUtilsBean extends BeanUtilsBean{  
  
    @Override  
    public void copyProperty(Object bean, String name, Object value)  
            throws IllegalAccessException, InvocationTargetException {  
        try {  
            Object destValue = PropertyUtils.getSimpleProperty(bean, name);  
            if (destValue == null) {  
                super.copyProperty(bean, name, value);  
            }  
        } catch (NoSuchMethodException e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
}
