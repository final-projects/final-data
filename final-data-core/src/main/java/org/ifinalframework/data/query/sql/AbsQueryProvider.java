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

package org.ifinalframework.data.query.sql;

import org.ifinalframework.query.QueryProvider;

/**
 * AbsQueryProvider.
 *
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbsQueryProvider implements QueryProvider {

    private static final String LIMIT = "<trim prefix=\"LIMIT \">"
        + "     <if test=\"query.offset != null\">"
        + "         #{query.offset},"
        + "     </if>"
        + "     <if test=\"query.limit != null\">"
        + "         #{query.limit}"
        + "     </if>"
        + "</trim>";

    private static final String ORDERS = "<if test=\"query.orders != null\">"
        + "     <foreach collection=\"query.orders\" item=\"item\" open=\"ORDER BY\" separator=\",\">${item}</foreach>"
        + "</if>";

    private static final String GROUPS = "<if test=\"query.groups != null\">"
        + "     <foreach collection=\"query.groups\" item=\"item\" open=\"GROUP BY\" separator=\",\">${item}</foreach>"
        + "</if>";

    @Override
    public String groups() {
        return GROUPS;
    }

    @Override
    public String orders() {
        return ORDERS;
    }

    @Override
    public String limit() {
        return LIMIT;
    }

}
