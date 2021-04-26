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

package org.ifinal.finalframework.mybatis.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
class ScriptMapperHelperTest {

    @Test
    void formatTest() {
        String test = ScriptMapperHelper.formatTest("item", "creator.id", false);
        logger.info(test);
        assertEquals("item.creator != null", test);
    }

    @Test
    void formatTest2() {
        String test = ScriptMapperHelper.formatTest("item", "creator.id", true);
        logger.info(test);
        assertEquals("item.creator != null and item.creator.id != null", test);
    }

}
