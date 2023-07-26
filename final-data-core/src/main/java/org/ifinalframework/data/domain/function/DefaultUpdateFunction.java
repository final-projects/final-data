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

import lombok.RequiredArgsConstructor;
import org.ifinalframework.core.IEntity;
import org.ifinalframework.core.IQuery;
import org.ifinalframework.core.IUser;
import org.ifinalframework.data.repository.Repository;
import org.ifinalframework.data.spi.BiUpdateFunction;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * DefaultUpdateFunction
 *
 * @author mik
 * @since 1.5.2
 **/
@RequiredArgsConstructor
public class DefaultUpdateFunction<ID extends Serializable, T extends IEntity<ID>, P, U extends IUser<?>>
        implements BiUpdateFunction<T, P, Boolean, T, U> {
    private final Repository<ID, T> repository;

    @Override
    @SuppressWarnings("unchecked")
    public Integer update(List<T> entities, P param, Boolean selective, T value, U user) {
        if (param instanceof IQuery) {
            return repository.update(value, selective, (IQuery) param);
        } else if (param instanceof Collection) {
            return repository.update(value, selective, (Collection<ID>) param);
        } else {
            return repository.update(value, selective, (ID) param);
        }
    }
}