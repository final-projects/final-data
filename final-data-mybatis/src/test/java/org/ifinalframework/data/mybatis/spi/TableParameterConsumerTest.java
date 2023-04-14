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

package org.ifinalframework.data.mybatis.spi;

import java.util.LinkedHashMap;
import java.util.Map;

import org.ifinalframework.data.annotation.AbsEntity;
import org.ifinalframework.data.mybatis.mapper.AbsMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TableParameterConsumerTest.
 *
 * @author ilikly
 * @version 1.5.0
 * @since 1.5.0
 */
class TableParameterConsumerTest {

    protected interface AbsEntityMapper extends AbsMapper<Long, AbsEntity>{}

    @Test
    void accept() {
        TableParameterConsumer consumer = new TableParameterConsumer();
        Map<String,Object> map = new LinkedHashMap<>();
        consumer.accept(map,AbsEntityMapper.class,null);
        Assertions.assertNotNull(map.get("table"));
    }
}