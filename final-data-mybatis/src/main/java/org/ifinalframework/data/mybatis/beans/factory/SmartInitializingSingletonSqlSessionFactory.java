/*
 * Copyright 2020-2023 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ifinalframework.data.mybatis.beans.factory;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import org.ifinalframework.data.mybatis.mapping.DefaultResultMapFactory;
import org.ifinalframework.data.mybatis.mapping.ResultMapFactory;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * SmartInitializingSingletonSqlSessionFactory.
 *
 * @author ilikly
 * @version 1.5.1
 * @since 1.5.1
 */
@Slf4j
@Setter
@Component
public class SmartInitializingSingletonSqlSessionFactory implements SmartInitializingSingleton, ApplicationContextAware {
    private ApplicationContext applicationContext;

    private final ResultMapFactory resultMapFactory = new DefaultResultMapFactory();

    @Override
    public void afterSingletonsInstantiated() {
        applicationContext.getBeanProvider(SqlSessionFactory.class)
                .forEach(sqlSessionFactory -> {
                    logger.info("find SqlSessionFactory: {}", sqlSessionFactory);

                    final Configuration configuration = sqlSessionFactory.getConfiguration();

                });
    }
}
