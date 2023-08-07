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

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import org.ifinalframework.core.IEntity;
import org.ifinalframework.core.IQuery;
import org.ifinalframework.data.repository.Repository;
import org.ifinalframework.data.spi.SelectFunction;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * DefaultSelectFunction.
 *
 * @author ilikly
 * @version 1.5.1
 * @see DefaultSelectOneFunction
 * @since 1.5.1
 */
@RequiredArgsConstructor
public class DefaultSelectFunction<K extends Serializable, T extends IEntity<K>, P, U> implements SelectFunction<P, U, List<T>> {
    private final Repository<K, T> repository;

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public List<T> select(@NonNull P param, @NonNull U user) {
        if (param instanceof IQuery) {
            return repository.select((IQuery) param);
        } else if (param instanceof Collection) {
            return repository.select((Collection<K>) param);
        } else {
            return repository.select((K) param);
        }
    }
}
