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

package org.ifinalframework.data.domain.action;

import java.io.Serializable;
import java.util.List;

import org.ifinalframework.core.IEntity;
import org.ifinalframework.core.IEnum;
import org.ifinalframework.core.IUser;
import org.ifinalframework.data.repository.Repository;
import org.ifinalframework.data.spi.SpiAction;
import org.ifinalframework.query.Update;

/**
 * UpdateStatusByIdDomainAction.
 *
 * @author ilikly
 * @version 1.5.0
 * @since 1.5.0
 */
public class UpdateStatusByIdDomainAction<ID extends Serializable, T extends IEntity<ID>, U extends IUser<?>>
        extends AbsUpdateDomainAction<ID, T, ID, IEnum<?>, Integer, U> {
    public UpdateStatusByIdDomainAction(Repository<ID, T> repository) {
        super(SpiAction.UPDATE_STATUS, repository);
    }

    @Override
    protected List<T> doActionPrepare(ID query, IEnum<?> value, U user) {
        return repository.select(query);
    }

    @Override
    protected Integer doActionInternal(List<T> list, ID query, IEnum<?> value, U user) {
        Update update = Update.update().set("status", value);
        return repository.update(update, query);
    }
}