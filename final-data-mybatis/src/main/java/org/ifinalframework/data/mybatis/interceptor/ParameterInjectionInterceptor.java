/*
 * Copyright 2020-2021 the original author or authors.
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

package org.ifinalframework.data.mybatis.interceptor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.ifinalframework.context.user.UserContextHolder;
import org.ifinalframework.core.IEntity;
import org.ifinalframework.core.IQuery;
import org.ifinalframework.core.Viewable;
import org.ifinalframework.data.annotation.Tenant;
import org.ifinalframework.data.core.TenantSupplier;
import org.ifinalframework.data.mybatis.mapper.AbsMapper;
import org.ifinalframework.data.query.DefaultQEntityFactory;
import org.ifinalframework.data.spi.composite.QueryConsumerComposite;
import org.ifinalframework.data.spi.core.OrderQueryConsumer;
import org.ifinalframework.query.QEntity;

/**
 * 参数注入拦截器
 *
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                        RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                        RowBounds.class, ResultHandler.class, CacheKey.class,
                        BoundSql.class}),
        }
)
@Order
@Component
@SuppressWarnings({"unchecked"})
public class ParameterInjectionInterceptor extends AbsMapperInterceptor {

    private static final String TABLE_PARAMETER_NAME = "table";
    private static final String QUERY_PARAMETER_NAME = "query";

    private static final String PROPERTIES_PARAMETER_NAME = "properties";

    private QueryConsumerComposite queryConsumerComposite = new QueryConsumerComposite(
            Arrays.asList(
                    new OrderQueryConsumer()
            )
    );

    private final TenantSupplier<?> tenantSupplier;

    public ParameterInjectionInterceptor(ObjectProvider<TenantSupplier<?>> tenantSupplierObjectProvider) {
        this.tenantSupplier = tenantSupplierObjectProvider.getIfAvailable();
    }

    @Override
    protected Object intercept(Invocation invocation, Class<?> mapper, Class<?> entityClass) throws Throwable {

        Object[] args = invocation.getArgs();

        Object parameter = args[1];

        if (parameter instanceof Map && AbsMapper.class.isAssignableFrom(mapper)) {
            Map<String, Object> parameters = (Map<String, Object>) parameter;

            if (parameters.containsKey(QUERY_PARAMETER_NAME)) {
                IQuery query = (IQuery) parameters.get(QUERY_PARAMETER_NAME);

                if (Objects.nonNull(query)) {
                    queryConsumerComposite.accept(query, (Class<IEntity<Long>>) entityClass);
                }

                if (Objects.nonNull(query) && parameters.containsKey("view") && query instanceof Viewable && Objects.isNull(parameters.get("view"))) {
                    parameters.put("view", ((Viewable) query).getView());
                }
            }

            final QEntity<?, ?> entity = DefaultQEntityFactory.INSTANCE.create(entityClass);
            parameters.computeIfAbsent(TABLE_PARAMETER_NAME, k -> entity.getTable());
            parameters.putIfAbsent(PROPERTIES_PARAMETER_NAME, entity);
            parameters.putIfAbsent("USER", UserContextHolder.getUser());

            if (Objects.nonNull(tenantSupplier) && entityClass.isAnnotationPresent(Tenant.class)) {
                parameters.put("tenant", tenantSupplier.get());
            } else {
                parameters.put("tenant", null);
            }

            args[1] = buildParamMap(parameters);

        }

        return invocation.proceed();


    }

    /**
     * {@link org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator} only supports {@link Map} types:
     * <ul>
     *     <li>{@link org.apache.ibatis.binding.MapperMethod.ParamMap}</li>
     *     <li>{@link DefaultSqlSession.StrictMap}</li>
     * </ul>
     *
     * @see org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator#assignKeys(Configuration, ResultSet, ResultSetMetaData, String[], Object)
     * @since 1.2.2
     */
    private MapperMethod.ParamMap<Object> buildParamMap(Map<String, Object> map) {
        final MapperMethod.ParamMap<Object> paramMap = new MapperMethod.ParamMap<>();
        paramMap.putAll(map);
        return paramMap;
    }


}
