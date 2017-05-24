package utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;

public class CollectionUtil 
{
	//remove null
	public static void  removeNullValue(Map map)
	{
		Iterator<Map.Entry> it = map.entrySet().iterator();
		while (it.hasNext()) 
		{
			Map.Entry entry = it.next();
			if (entry.getValue() == null) 
				it.remove();
		}
	}
	
	public static Map getBeanDescription(BeanUtilsBean beanUtilsBean, Object bean, Map description) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		if(description == null)
			description = new HashMap();
		
		PropertyDescriptor[] descriptors = beanUtilsBean.getPropertyUtils().getPropertyDescriptors(bean);
		Class clazz = bean.getClass();
		for (int i = 0; i < descriptors.length; i++) {
			String name = descriptors[i].getName();
			if (beanUtilsBean.getPropertyUtils().getReadMethod(descriptors[i]) != null) 
			{
				Object value = PropertyUtils.getProperty(bean, name);
				if(value!=null)
				{
					//System.out.println("[key]" + name + "[value]" + value.getClass() + "[mvalue]" + beanUtilsBean.getConvertUtils().convert(value, String.class));
					description.put(name, beanUtilsBean.getConvertUtils().convert(value, String.class));
				}
			}
		}
		return description;
	}
}
