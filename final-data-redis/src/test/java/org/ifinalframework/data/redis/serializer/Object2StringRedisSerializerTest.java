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

package org.ifinalframework.data.redis.serializer;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Object2StringRedisSerializerTest.
 *
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */
class Object2StringRedisSerializerTest {

    @Test
    void serialize() {
        assertNull(Object2StringRedisSerializer.UTF_8.serialize(null));
    }

    @Test
    void deserialize() {
        assertNull(Object2JsonRedisSerializer.UTF_8.deserialize(null));
    }

}
