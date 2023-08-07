/*
 * Copyright 2020-2022 the original author or authors.
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

package org.ifinalframework.data.sharding.config;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */

public class ShardingDataSourceRegistry {

    @Getter(AccessLevel.PACKAGE)
    private final Map<String, DataSource> dataSources = new HashMap<>();

    public ShardingDataSourceRegistry addDataSource(final String name, final DataSource dataSource) {

        this.dataSources.put(name, dataSource);
        return this;
    }

}
