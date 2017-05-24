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
package com.phn.tojoy.laucher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.sabomichal.akkaspringfactory.ActorSystemFactoryBean;
import com.typesafe.config.ConfigFactory;

@Configuration
@ComponentScan(basePackages={ "org.jupport.manager", "com.phn.base.component", "com.phn.tojoy.manager", "com.phn.tojoy.listener", "com.phn.tojoy.service"})
// Configurer that replaces ${...} placeholders with values from a properties file
// (in this case, JDBC-related settings for the JPA EntityManager definition below)
@PropertySource("classpath:spring/data-access.properties")
@EnableTransactionManagement
@Import({JpaPhnImConfig.class})
public class ImBusinessConfig {
		
	@Bean(name="actorSystem")
	public ActorSystemFactoryBean actorSystem() {
		ActorSystemFactoryBean factoryBean = new ActorSystemFactoryBean();
		factoryBean.setName("PhnActorSystem");
		factoryBean.setConfig(ConfigFactory.load("akkaActor.conf"));
		return factoryBean;
	}

}
