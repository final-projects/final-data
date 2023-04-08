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

package org.ifinalframework.data.mybatis.interceptor;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * DispatchInterceptor.
 *
 * @author ilikly
 * @version 1.5.0
 * @since 1.5.0
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                        RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                        RowBounds.class, ResultHandler.class, CacheKey.class,
                        BoundSql.class}),
                @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        }
)
@Order
@Slf4j
@Component
public class DispatchInterceptor implements Interceptor {

    private final List<ParameterConsumer<?>> parameterConsumers;

    public DispatchInterceptor(ObjectProvider<ParameterConsumer<?>> parameterConsumers) {
        this.parameterConsumers = parameterConsumers.orderedStream().collect(Collectors.toList());
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        final Method method = invocation.getMethod();
        final String methodName = method.getName();
        final Object[] args = invocation.getArgs();


        if (methodName.equals("update")) {
            final MappedStatement mappedStatement = (MappedStatement) args[0];
            final Object parameters = args[1];

            final SqlSource sqlSource = mappedStatement.getSqlSource();

            final String id = mappedStatement.getId();
            final String mapperClassName = id.substring(0, id.lastIndexOf("."));
            final String mapperMethodName = id.substring(id.lastIndexOf(".") + 1);
            applyParameterConsumer(parameters, mapperClassName, mapperMethodName);
            logger.info("{}#{},parameters={}", mapperClassName, mapperMethodName, parameters);
            return doUpdate(invocation);
        } else if (methodName.equals("query")) {
            final MappedStatement mappedStatement = (MappedStatement) args[0];
            final Object parameters = args[1];

            final String id = mappedStatement.getId();
            final String mapperClassName = id.substring(0, id.lastIndexOf("."));
            final String mapperMethodName = id.substring(id.lastIndexOf(".") + 1);
            applyParameterConsumer(parameters, mapperClassName, mapperMethodName);
            logger.info("{}#{},parameters={}", mapperClassName, mapperMethodName, parameters);
            return doQuery(invocation);
        } else if (methodName.equals("prepare")) {
            return doPrepare(invocation);
        }

        return invocation.proceed();


    }

    private void applyParameterConsumer(Object parameter, String mapperClassName, String mapperMethodName) {
        if (CollectionUtils.isEmpty(parameterConsumers)) {
            return;
        }

        final Class<?> mapperClass = ClassUtils.resolveClassName(mapperClassName, getClass().getClassLoader());


        for (final ParameterConsumer parameterConsumer : parameterConsumers) {
            if (parameterConsumer.supports(parameter)) {
                parameterConsumer.accept(parameter, mapperClass, null);
            }
        }

    }

    /**
     * @param invocation
     *
     * @return
     * @see org.apache.ibatis.executor.Executor#query(MappedStatement, Object, RowBounds, ResultHandler)
     * @see org.apache.ibatis.executor.Executor#query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql)
     */
    private Object doQuery(Invocation invocation) throws Throwable {

        return invocation.proceed();
    }

    /**
     * @param invocation
     *
     * @return
     * @see org.apache.ibatis.executor.Executor#update(MappedStatement, Object)
     */
    private Object doUpdate(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];
        return invocation.proceed();
    }

    /**
     * @param invocation
     *
     * @return
     * @throws Throwable
     * @see StatementHandler#prepare(Connection, Integer)
     */
    private Object doPrepare(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }
}
