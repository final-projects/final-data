/*
 * Copyright 2020-2021 the original author or authors.
 *
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

package org.ifinal.finalframework.mybatis.sql.provider;

import org.ifinal.finalframework.core.IEntity;
import org.ifinal.finalframework.core.IQuery;
import org.ifinal.finalframework.data.query.DefaultQEntityFactory;
import org.ifinal.finalframework.mybatis.sql.AbsMapperSqlProvider;
import org.ifinal.finalframework.query.QEntity;
import org.ifinal.finalframework.query.Query;
import org.ifinal.finalframework.query.QueryProvider;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.ibatis.builder.annotation.ProviderContext;

/**
 * @author likly
 * @version 1.0.0
 * @see org.ifinal.finalframework.mybatis.mapper.AbsMapper#selectIds(String, IQuery)
 * @since 1.0.0
 */
public class SelectIdsSqlProvider implements AbsMapperSqlProvider {

    public static final String QUERY_PARAMETER_NAME = "query";

    /**
     * @param context    context
     * @param parameters parameters
     * @return sql
     * @see org.ifinal.finalframework.mybatis.mapper.AbsMapper#selectIds(String, IQuery)
     */
    @SuppressWarnings("unused")
    public String selectIds(final ProviderContext context, final Map<String, Object> parameters) {

        return provide(context, parameters);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void doProvide(final StringBuilder sql, final ProviderContext context,
        final Map<String, Object> parameters) {

        final Class<?> entity = getEntityClass(context.getMapperType());
        final QEntity<?, ?> properties = DefaultQEntityFactory.INSTANCE.create(entity);
        parameters.put("entity", properties);

        /*
         * <trim prefix="SELECT">
         *      columns
         * </trim>
         */
        sql.append("<trim prefix=\"SELECT\" suffixOverrides=\",\">");
        sql.append(properties.getIdProperty().getColumn());
        sql.append("</trim>");

        sql.append("<trim prefix=\"FROM\">")
            .append("${table}")
            .append("</trim>");

        Object query = parameters.get(QUERY_PARAMETER_NAME);

        if (query instanceof Query) {
            QueryProvider provider = query((Query) query);

            Optional.ofNullable(provider.where()).ifPresent(sql::append);
            Optional.ofNullable(provider.orders()).ifPresent(sql::append);
            Optional.ofNullable(provider.limit()).ifPresent(sql::append);
        } else if (query != null) {

            final QueryProvider provider = query(QUERY_PARAMETER_NAME, (Class<? extends IEntity<?>>) entity,
                query.getClass());

            if (Objects.nonNull(provider.where())) {
                sql.append(provider.where());
            }

            if (Objects.nonNull(provider.orders())) {
                sql.append(provider.orders());
            }

            if (Objects.nonNull(provider.limit())) {
                sql.append(provider.limit());
            }
        }
    }

}
