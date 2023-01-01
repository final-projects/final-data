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

package org.ifinalframework.data.spi.composite;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import org.ifinalframework.data.spi.PostInsertConsumer;

/**
 * PostInsertConsumerComposite.
 *
 * @author ilikly
 * @version 1.4.2
 * @since 1.4.2
 */
public class PostInsertConsumerComposite<T, U> implements PostInsertConsumer<T, U> {
    private final List<PostInsertConsumer<T, U>> consumers;

    public PostInsertConsumerComposite(List<PostInsertConsumer<T, U>> consumers) {
        this.consumers = consumers;
    }

    @Override
    public void accept(@NonNull T entity, @NonNull U user) {
        if (CollectionUtils.isEmpty(consumers)) {
            return;
        }

        for (PostInsertConsumer<T, U> consumer : consumers) {
            consumer.accept(entity, user);
        }
    }
}


