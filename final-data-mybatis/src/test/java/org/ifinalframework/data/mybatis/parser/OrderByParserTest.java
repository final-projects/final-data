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

package org.ifinalframework.data.mybatis.parser;

import com.github.pagehelper.parser.OrderByParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * OrderByParserTest.
 *
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
class OrderByParserTest {

    @Test
    void converToOrderBySql() {
        final String sql = "SELECT * FROM table ORDER BY name DESC";
        String orderBySql = OrderByParser.converToOrderBySql(sql, null);
        logger.info(sql);
        logger.info(orderBySql);
    }

}
