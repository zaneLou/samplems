package org.jupport.server.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public abstract class AbstractJpaConfig {

	private Properties properties;
	
	protected LocalContainerEntityManagerFactoryBean em;
	
	public abstract DataSource getDataSource();
	
	public abstract JpaVendorAdapter getJpaVendorAdaper();
	
	public abstract String getDataSourceFile();

	public Properties getProperties()  {
		if(properties == null){
		    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		    Resource resource = new ClassPathResource(getDataSourceFile());
		    propertiesFactoryBean.setLocation(resource);
		    try {
				propertiesFactoryBean.afterPropertiesSet();
				properties = propertiesFactoryBean.getObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    return properties;
	}
	
    public EntityManagerFactory getEntityManagerFactory() {
    	if(em == null){
            em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(getDataSource());
            em.setPersistenceUnitName(getProperties().getProperty("persistenceUnitName"));
            String[] packages = getProperties().getProperty("packagesToScan").split(",");
            em.setPackagesToScan(packages);
            em.setJpaVendorAdapter(getJpaVendorAdaper());
            em.setJpaProperties(getProperties());
            em.afterPropertiesSet();
    	}
        return em.getObject();
    }

}
