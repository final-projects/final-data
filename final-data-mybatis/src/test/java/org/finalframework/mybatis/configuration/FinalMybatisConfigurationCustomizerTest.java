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

package org.finalframework.mybatis.configuration;

import org.finalframework.mybatis.entity.Person;

import java.util.Collections;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * FinalMybatisConfigurationCustomizerTest.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
class FinalMybatisConfigurationCustomizerTest {

    private FinalMybatisConfigurationCustomizer customizer = new FinalMybatisConfigurationCustomizer();

    @Test
    void customize() {

        customizer.setPackages(Collections.singletonList("org.finalframework"));
        Configuration configuration = new Configuration();
        customizer.customize(configuration);
        ResultMap resultMap = configuration.getResultMap(Person.class.getName());
        Assertions.assertNotNull(resultMap);

    }

}
