/*
 * Copyright 2020-2021 the original author or authors.
 *
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

package org.ifinal.finalframework.mybatis.configuration;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import org.ifinal.finalframework.core.IEntity;
import org.ifinal.finalframework.io.support.ServicesLoader;
import org.ifinal.finalframework.mybatis.handler.EnumTypeHandler;
import org.ifinal.finalframework.mybatis.mapper.AbsMapper;
import org.ifinal.finalframework.mybatis.resumtmap.ResultMapFactory;

import java.lang.reflect.Field;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;

/**
 * @author likly
 * @version 1.0.0
 * @see org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
 * @since 1.0.0
 */
@Slf4j
@Component
@SuppressWarnings("unused")
public class FinalMybatisConfigurationCustomizer implements ConfigurationCustomizer {

    private static final Field composites = Objects
        .requireNonNull(ReflectionUtils.findField(ResultMapping.class, "composites"));

    static {
        ReflectionUtils.makeAccessible(composites);
    }

    @Override
    public void customize(final Configuration configuration) {

        logger.info("setDefaultEnumTypeHandler:{}", EnumTypeHandler.class.getCanonicalName());
        configuration.addMapper(AbsMapper.class);
        configuration.getTypeHandlerRegistry().setDefaultEnumTypeHandler(EnumTypeHandler.class);

        ServicesLoader.load(IEntity.class, IEntity.class.getClassLoader())
            .stream()
            .map((String className) -> {

                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {
                    //ignore
                    throw new IllegalArgumentException(e);
                }
            })
            .forEach(clazz -> {
                ResultMap resultMap = ResultMapFactory.from(configuration, clazz);

                if (logger.isInfoEnabled()) {
                    logger.info("==> addResultMap:[{}],class={}", resultMap.getId(), clazz);
                }

                configuration.addResultMap(resultMap);

                resultMap.getResultMappings()
                    .stream()
                    .filter(ResultMapping::isCompositeResult)
                    .forEach(resultMapping -> {

                        ResultMap map = new ResultMap.Builder(configuration, resultMapping.getNestedResultMapId(),
                            resultMap.getType(),
                            resultMapping.getComposites()).build();
                        configuration.addResultMap(map);

                        // mybatis not support composites result mapping
                        ReflectionUtils.setField(composites, resultMapping, null);

                    });

            });

    }

}

