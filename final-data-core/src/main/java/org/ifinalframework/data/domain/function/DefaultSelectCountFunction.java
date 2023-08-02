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
import org.ifinalframework.context.exception.BadRequestException;
import org.ifinalframework.core.IEntity;
import org.ifinalframework.core.IQuery;
import org.ifinalframework.data.repository.Repository;
import org.ifinalframework.data.spi.SelectFunction;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * DefaultSelectCountFunction
 *
 * @author mik
 * @since 1.5.2
 **/
@RequiredArgsConstructor
public class DefaultSelectCountFunction<ID extends Serializable, T extends IEntity<ID>, P, U> implements SelectFunction<P, U, Long> {
    private final Repository<ID, T> repository;

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public Long select(@NonNull P param, @NonNull U user) {
        if (param instanceof IQuery) {
            return repository.selectCount((IQuery) param);
        }

        throw new BadRequestException("不支持的Count查询");
    }
}