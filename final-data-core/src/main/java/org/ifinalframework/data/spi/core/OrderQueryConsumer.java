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

package org.ifinalframework.data.spi.core;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import org.ifinalframework.core.IEntity;
import org.ifinalframework.core.IQuery;
import org.ifinalframework.core.Orderable;
import org.ifinalframework.data.query.DefaultQEntityFactory;
import org.ifinalframework.data.spi.QueryConsumer;
import org.ifinalframework.query.QEntity;
import org.ifinalframework.query.QEntityFactory;
import org.ifinalframework.query.QProperty;

/**
 * OrderQueryConsumer.
 *
 * @author ilikly
 * @version 1.4.2
 * @since 1.4.2
 */
@Component
public class OrderQueryConsumer implements QueryConsumer<IQuery, IEntity<Long>> {
    private final QEntityFactory entityFactory = DefaultQEntityFactory.INSTANCE;

    @Override
    public void accept(@NonNull IQuery query, @NonNull Class<IEntity<Long>> clazz) {

        if (!(query instanceof Orderable)) {
            return;
        }

        Orderable orderable = (Orderable) query;
        List<String> orders = orderable.getOrders();
        if (!CollectionUtils.isEmpty(orders)) {
            QEntity<?, ?> qEntity = entityFactory.create(clazz);

            List<String> newOrders = orders.stream()
                    .map(it -> {
                        String[] split = it.split(" ");
                        String property = split[0];
                        String direction = split.length == 2 ? split[1] : null;

                        QProperty<Object> requiredProperty = qEntity.getRequiredProperty(property);

                        return Stream.of(requiredProperty.getColumn(), direction)
                                .filter(Objects::nonNull)
                                .collect(Collectors.joining(" "));

                    })
                    .collect(Collectors.toList());

            orderable.setOrders(newOrders);
        }
    }
}


