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

package org.ifinalframework.data.domain.function;

import org.ifinalframework.core.IEntity;
import org.ifinalframework.core.IUser;
import org.ifinalframework.data.query.Update;
import org.ifinalframework.data.repository.Repository;
import org.ifinalframework.data.spi.UpdateFunction;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

/**
 * DefaultUpdateSortFunction
 *
 * @author mik
 * @since 1.5.4
 **/
@RequiredArgsConstructor
public class DefaultUpdateSortFunction<K extends Serializable, T extends IEntity<K>, U extends IUser<?>>
        implements UpdateFunction<T, Void, Void, Map<K, Integer>, U> {

    private final Repository<K, T> repository;
    @Override
    public Integer update(List<T> entities, Void param, Void param2, Map<K, Integer> value, U user) {
        return value.entrySet().stream().mapToInt(entry -> repository.update(Update.update().set("sort_value", entry.getValue()),entry.getKey())).sum();
    }
}
