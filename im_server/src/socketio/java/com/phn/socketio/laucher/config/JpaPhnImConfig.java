/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phn.socketio.laucher.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.jupport.server.config.AbstractJpaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile("jpa")
@ComponentScan({"com.phn.base.repository.jpa", "com.phn.socketio.repository.jpa"})
//@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory",
//		transactionManagerRef = "transactionManager",
//		basePackages="com.phn.repository.jpa" )
public class JpaPhnImConfig extends AbstractJpaConfig {
	
    @Autowired
    private Environment        env;

	private HikariDataSource phnDataSource;
	
	@Autowired
	private JpaVendorAdapter jpaVendorAdaper;
	
	@PostConstruct
	public void postConstruct(){
		log.info("JpaMoveOnConfig JpaMoveOnConfig {} ", getDataSource());
	}

    @PreDestroy
    public void destroy(){
    	phnDataSource.close();
    	em.destroy();
    	log.info("destroy JpaPhn ");
    }
	
	@Override
	public DataSource getDataSource(){
		return phnDataSource();
	}
	
	@Override
	public JpaVendorAdapter getJpaVendorAdaper(){
		return jpaVendorAdaper;
	}
	
	@Override
	public String getDataSourceFile(){
		return "spring/data-access.properties";
	}

	@Bean
	@Qualifier(value = "phnDataSource")
	public DataSource phnDataSource() {
		if(phnDataSource == null){
			phnDataSource = new HikariDataSource();
		}
		return phnDataSource;
	}

    @Bean(name = "phnEntityManager")
    @Qualifier(value = "phnEntityManager")
    public EntityManager entityManager() {
        return getEntityManagerFactory().createEntityManager();
    }
   
    @Bean
    @Qualifier(value = "transactionManager")
    public JpaTransactionManager jpaTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(getEntityManagerFactory());
        return jpaTransactionManager;
    }
    
    @Bean
    public JpaVendorAdapter jpaVendorAdaper() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        // the 'database' parameter refers to the database dialect being used.
    	// By default, Hibernate will use a 'HSQL' dialect because 'jpa.database' has been set to 'HSQL'
    	// inside file spring/data-access.properties
        vendorAdapter.setDatabase(env.getProperty("jpa.database", Database.class));
        vendorAdapter.setShowSql(env.getProperty("jpa.showSql", Boolean.class));
        return vendorAdapter;
    }
    
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
    	return new PersistenceExceptionTranslationPostProcessor();
    }
}
