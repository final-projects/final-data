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

package org.ifinalframework.data.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import org.ifinalframework.context.exception.ForbiddenException;
import org.ifinalframework.context.exception.NotFoundException;
import org.ifinalframework.core.IEntity;
import org.ifinalframework.core.IEnum;
import org.ifinalframework.core.ILock;
import org.ifinalframework.core.IQuery;
import org.ifinalframework.core.IUser;
import org.ifinalframework.core.IView;
import org.ifinalframework.data.annotation.YN;
import org.ifinalframework.data.repository.Repository;
import org.ifinalframework.data.spi.AfterReturningConsumer;
import org.ifinalframework.data.spi.AfterReturningQueryConsumer;
import org.ifinalframework.data.spi.AfterThrowingConsumer;
import org.ifinalframework.data.spi.AfterThrowingQueryConsumer;
import org.ifinalframework.data.spi.Consumer;
import org.ifinalframework.data.spi.Filter;
import org.ifinalframework.data.spi.PostQueryConsumer;
import org.ifinalframework.data.spi.PostQueryFunction;
import org.ifinalframework.data.spi.PreInsertFunction;
import org.ifinalframework.data.spi.PreQueryConsumer;
import org.ifinalframework.data.spi.PreUpdateValidator;
import org.ifinalframework.data.spi.SpiAction;
import org.ifinalframework.data.spi.UpdateConsumer;
import org.ifinalframework.query.Update;

import lombok.Builder;

/**
 * DefaultDomainService.
 *
 * @author ilikly
 * @version 1.4.3
 * @since 1.4.3
 */
@Builder
@SuppressWarnings("unchecked")
public class DefaultDomainResourceService<ID extends Serializable, T extends IEntity<ID>> implements DomainResourceService<ID, T> {

    private final Repository<ID, T> repository;

    private final Class<T> entityClass;

    private final Map<Class<?>, Class<? extends IQuery>> queryClassMap;
    private final Map<Class<?>, Class<?>> domainClassMap;

    private final PreInsertFunction<Object, IUser<?>, T> preInsertFunction;

    // create
    private final Filter<T, IUser<?>> preInsertFilter;
    private final Consumer<T, IUser<?>> preInsertConsumer;
    private final Consumer<T, IUser<?>> postInsertConsumer;
    private final AfterThrowingConsumer<T, IUser<?>> afterThrowingInsertConsumer;
    private final AfterReturningConsumer<T, Integer, IUser<?>> afterReturningInsertConsumer;


    // list
    private final PreQueryConsumer<IQuery, IUser<?>> preQueryConsumer;
    private final PostQueryConsumer<T, IQuery, IUser<?>> postQueryConsumer;

    private final PostQueryFunction<List<T>, IQuery, IUser<?>> postQueryFunction;

    private final AfterThrowingQueryConsumer<T, IQuery, IUser<?>> afterThrowingQueryConsumer;
    private final AfterReturningQueryConsumer<T, IQuery, IUser<?>> afterReturningQueryConsumer;

    // detail
    private final PreQueryConsumer<IQuery, IUser<?>> preDetailQueryConsumer;
    private final PostQueryConsumer<T, IQuery, IUser<?>> postDetailQueryConsumer;
    private final Consumer<T, IUser<?>> postDetailConsumer;


    // count
    private final PreQueryConsumer<IQuery, IUser<?>> preCountQueryConsumer;

    // update
    private final Consumer<T, IUser<?>> preUpdateConsumer;
    private final Consumer<T, IUser<?>> postUpdateConsumer;

    // update yn
    private final PreUpdateValidator<T, YN, IUser<?>> preUpdateYnValidator;

    private final UpdateConsumer<T, YN, IUser<?>> postUpdateYnConsumer;

    // update status
    private final UpdateConsumer<T, IEnum<?>, IUser<?>> preUpdateStatusConsumer;
    private final UpdateConsumer<T, IEnum<?>, IUser<?>> postUpdateStatusConsumer;

    // update locked
    private final UpdateConsumer<T, Boolean, IUser<?>> preUpdateLockedConsumer;
    private final UpdateConsumer<T, Boolean, IUser<?>> postUpdateLockedConsumer;

    // delete

    private final PreQueryConsumer<IQuery, IUser<?>> preDeleteQueryConsumer;
    private final PostQueryConsumer<T, IQuery, IUser<?>> postDeleteQueryConsumer;
    private final Consumer<T, IUser<?>> preDeleteConsumer;
    private final Consumer<T, IUser<?>> postDeleteConsumer;

    @NonNull
    @Override
    public Class<T> entityClass() {
        return entityClass;
    }

    @Nullable
    @Override
    public Class<?> domainEntityClass(Class<?> prefix) {
        return domainClassMap.get(prefix);
    }

    @NonNull
    @Override
    public Class<? extends IQuery> domainQueryClass(Class<?> prefix) {
        return queryClassMap.get(prefix);
    }

    @Override
    public PreInsertFunction<Object, IUser<?>, T> preInsertFunction() {
        return preInsertFunction;
    }

    @Override
    public Integer create(@NonNull List<T> entities, @NonNull IUser<?> user) {
        Integer result = null;
        Throwable exception = null;
        try {
            entities = entities.stream().filter(item -> preInsertFilter.test(SpiAction.CREATE, item, user)).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(entities)) {
                return result = 0;
            }

            preInsertConsumer.accept(SpiAction.CREATE, SpiAction.Advice.PRE, entities, user);
            result = repository.insert(entities);
            postInsertConsumer.accept(SpiAction.CREATE, SpiAction.Advice.POST, entities, user);
            return result;
        } catch (Throwable e) {
            exception = e;
            afterThrowingInsertConsumer.accept(SpiAction.CREATE, entities, user, exception);
            throw e;
        } finally {
            afterReturningInsertConsumer.accept(SpiAction.CREATE, entities, result, user, exception);
        }
    }

    @Override
    public Object list(@NonNull IQuery query, @NonNull IUser<?> user) {
        List<T> list = null;
        Throwable throwable = null;
        try {
            preQueryConsumer.accept(SpiAction.LIST, query, user);
            list = repository.select(query);
            if (CollectionUtils.isEmpty(list)) {
                return list;
            }
            postQueryConsumer.accept(SpiAction.LIST, list, query, user);

            if (Objects.nonNull(postQueryFunction)) {
                return postQueryFunction.map(list, query, user);
            }


            return list;
        } catch (Exception e) {
            throwable = e;
            afterThrowingQueryConsumer.accept(SpiAction.LIST, list, query, user, e);
            throw e;
        } finally {
            afterReturningQueryConsumer.accept(SpiAction.LIST, list, query, user, throwable);
        }
    }

    @Override
    public T detail(@NonNull IQuery query, @NonNull IUser<?> user) {

        T entity = null;
        Throwable throwable = null;
        try {

            preDetailQueryConsumer.accept(SpiAction.DETAIL, query, user);
            entity = repository.selectOne(IView.Detail.class, query);
            if (Objects.nonNull(entity)) {
                postDetailQueryConsumer.accept(SpiAction.DETAIL, Collections.singletonList(entity), query, user);
                postDetailConsumer.accept(SpiAction.DETAIL, SpiAction.Advice.POST, Collections.singletonList(entity), user);
            }
            return entity;
        } catch (Throwable e) {
            throwable = e;
            afterThrowingQueryConsumer.accept(SpiAction.DETAIL, Collections.singletonList(entity), query, user, e);
            throw e;
        } finally {
            afterReturningQueryConsumer.accept(SpiAction.DETAIL, Collections.singletonList(entity), query, user, throwable);
        }
    }

    @Override
    public T detail(@NonNull ID id, @NonNull IUser<?> user) {
        T entity = repository.selectOne(id);
        if (Objects.nonNull(entity)) {
            postDetailConsumer.accept(SpiAction.DETAIL, SpiAction.Advice.POST, Collections.singletonList(entity), user);
        }
        return entity;
    }

    @Override
    public Long count(@NonNull IQuery query, @NonNull IUser<?> user) {
        preCountQueryConsumer.accept(SpiAction.COUNT, query, user);
        return repository.selectCount(query);
    }

    @Override
    public int delete(@NonNull IQuery query, @NonNull IUser<?> user) {
        preDeleteQueryConsumer.accept(SpiAction.DELETE, query, user);
        List<T> entities = repository.select(query);
        if (CollectionUtils.isEmpty(entities)) {
            return 0;
        }
        preDeleteConsumer.accept(SpiAction.DELETE, SpiAction.Advice.PRE, entities, user);
        int delete = repository.delete(entities.stream().map(T::getId).collect(Collectors.toList()));
        postDeleteQueryConsumer.accept(SpiAction.DELETE, entities, query, user);
        postDeleteConsumer.accept(SpiAction.DELETE, SpiAction.Advice.POST, entities, user);
        return delete;
    }

    @Override
    public int delete(@NonNull ID id, @NonNull IUser<?> user) {
        T entity = repository.selectOne(id);

        if (Objects.isNull(entity)) {
            throw new NotFoundException("not found delete target. id=" + id);
        }

        preDeleteConsumer.accept(SpiAction.DELETE, SpiAction.Advice.PRE, Collections.singletonList(entity), user);
        int delete = repository.delete(id);
        postDeleteConsumer.accept(SpiAction.DELETE, SpiAction.Advice.POST, Collections.singletonList(entity), user);
        return delete;
    }

    @Override
    public int update(@NonNull T entity, @NonNull ID id, boolean selective, @NonNull IUser<?> user) {
        T dbEntity = repository.selectOne(id);
        if (Objects.isNull(dbEntity)) {
            throw new NotFoundException("not found entity by id= " + id);
        }
        preUpdateConsumer.accept(SpiAction.UPDATE, SpiAction.Advice.PRE, Collections.singletonList(entity), user);
        int update = repository.update(entity, selective, id);
        postUpdateConsumer.accept(SpiAction.UPDATE, SpiAction.Advice.POST, Collections.singletonList(entity), user);
        return update;
    }

    @Override
    public int yn(@NonNull ID id, @NonNull YN yn, @NonNull IUser<?> user) {
        T entity = repository.selectOne(id);
        if (Objects.isNull(entity)) {
            throw new NotFoundException("not found entity by id= " + id);
        }

        preUpdateYnValidator.validate(Collections.singletonList(entity), yn, user);
        Update update = Update.update().set("yn", yn);
        int rows = repository.update(update, id);
        postUpdateYnConsumer.accept(SpiAction.UPDATE, SpiAction.Advice.POST, Collections.singletonList(entity), yn, user);
        return rows;
    }

    @Override
    public int status(@NonNull ID id, @NonNull IEnum<?> status, @NonNull IUser<?> user) {
        T entity = repository.selectOne(id);
        if (Objects.isNull(entity)) {
            throw new NotFoundException("not found entity by id= " + id);
        }

        preUpdateStatusConsumer.accept(SpiAction.UPDATE_STATUS, SpiAction.Advice.PRE, Collections.singletonList(entity), status, user);
        Update update = Update.update().set("status", status);
        final int rows = repository.update(update, id);
        postUpdateStatusConsumer.accept(SpiAction.UPDATE_STATUS, SpiAction.Advice.POST, Collections.singletonList(entity), status, user);
        return rows;
    }

    @Override
    public int lock(@NonNull ID id, @NonNull Boolean locked, @NonNull IUser<?> user) {
        T entity = repository.selectOne(id);
        if (Objects.isNull(entity)) {
            throw new NotFoundException("not found entity by id= " + id);
        }
        preUpdateLockedConsumer.accept(SpiAction.UPDATE_LOCKED, SpiAction.Advice.PRE, Collections.singletonList(entity), locked, user);
        Update update = Update.update().set("locked", locked);
        final int rows = repository.update(update, id);
        postUpdateLockedConsumer.accept(SpiAction.UPDATE_LOCKED, SpiAction.Advice.POST, Collections.singletonList(entity), locked, user);
        return 0;
    }
}
