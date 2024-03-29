/*
 * Copyright 2020-2022 the original author or authors.
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

package org.ifinalframework.data.env;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import org.ifinalframework.auto.spring.factory.annotation.SpringFactory;
import org.ifinalframework.data.mapping.DefaultEntityFactory;
import org.ifinalframework.data.mapping.EntityCache;

/**
 * EntityFactoryEnvironmentPostProcessor.
 *
 * @author iimik
 * @version 1.3.1
 * @since 1.3.1
 */
@SpringFactory(EnvironmentPostProcessor.class)
public class EntityFactoryEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        EntityCache.setEntityFactory(new DefaultEntityFactory(environment));
    }
}
