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

package org.ifinalframework.data.mybatis.sql.provider;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.ifinalframework.core.IEntity;
import org.ifinalframework.core.IQuery;
import org.ifinalframework.data.annotation.AbsUser;
import org.ifinalframework.data.annotation.AutoInc;
import org.ifinalframework.data.annotation.PrimaryKey;
import org.ifinalframework.data.mapping.DefaultEntityFactory;
import org.ifinalframework.data.mapping.Entity;
import org.ifinalframework.data.mybatis.dao.mapper.UserMapper;
import org.ifinalframework.data.mybatis.sql.util.SqlHelper;
import org.ifinalframework.data.query.DefaultQEntityFactory;
import org.ifinalframework.query.*;
import org.ifinalframework.query.annotation.*;
import org.ifinalframework.query.annotation.Criteria;

import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
class SqlHelperTest {

    @Test
    void insert() {
        Map<String, Object> paramsters = new HashMap<>();
        paramsters.put("table", "user");
        AbsUser user = new AbsUser();
        user.setId(1L);
        user.setName("haha");
        paramsters.put("USER", user);
        logger.info(SqlHelper.xml(UserMapper.class, "insert", paramsters));
    }

    @Test
    void and() {
        AndQuery query = new AndQuery();
        query.setColumnA("a");
        query.setColumnA2("aa");
        logger.info(SqlHelper.query(Bean.class, query));
    }

    @Test
    void or() {
        OrQuery query = new OrQuery();
        query.setColumnA("a");
        query.setColumnB(new BetweenValue<>("minB", "maxB"));
        query.setColumnC(Arrays.asList("c1", "c2", "c3"));
        logger.info(SqlHelper.query(Bean.class, query));
    }

    @Test
    void andOr() {
        AndOrQuery query = new AndOrQuery();
        query.setColumnA("a");
        InnerQuery innerQuery = new InnerQuery();
        innerQuery.setColumnB("b");
        innerQuery.setColumnC("c");
        query.setInnerQuery(innerQuery);
        logger.info(SqlHelper.query(Bean.class, query));

    }

    @Test
    void orAnd() {
        OrAndQuery query = new OrAndQuery();
        query.setColumnA("a");
        InnerQuery innerQuery = new InnerQuery();
        innerQuery.setColumnB("b");
        innerQuery.setColumnC("c");
        query.setInnerQuery(innerQuery);
        logger.info(SqlHelper.query(Bean.class, query));
    }

    @Test
    void sql() {
        final PersonQuery query = new PersonQuery();
        query.setName("haha");
        logger.info("sql={}", SqlHelper.sql(PersonMapper.class, "select", Collections.singletonMap("query", query)));
        logger.info("query={}", SqlHelper.query(Person.class, query));
    }

    @Test
    void sql2() {
        final Query query = new Query();
        QEntity<?, ?> entity = new DefaultQEntityFactory().create(Person.class);
        query.where(AndOr.OR,
                Arrays.asList(
                        entity.getRequiredProperty("name").jsonExtract("$.a").neq("2"),
                        entity.getRequiredProperty("name").date().eq("123")

                )
        );
        logger.info("sql={}", SqlHelper.sql(PersonMapper.class, "select", Collections.singletonMap("query", query)));
        logger.info("query={}", SqlHelper.query(Person.class, query));
    }

    @Data
    static class Bean implements IEntity<Long> {

        @AutoInc
        @PrimaryKey
        private Long id;

        private String columnA;

        private String columnB;

        private String columnC;

    }

    @Data
    static class AndQuery implements IQuery {

        @Equal
        private String columnA;

        @JsonContains(path = "$.columnA", property = "columnA")
        private String columnA2;

    }

    @Data
    @Or
    static class OrQuery implements IQuery {

        @Equal
        private String columnA;

        @NotBetween
        private BetweenValue<String> columnB;

        @NotIn
        private List<String> columnC;

    }

    @Data
    static class AndOrQuery implements IQuery {

        @Equal
        private String columnA;

        @Or
        private InnerQuery innerQuery;

    }

    @Data
    @Or
    static class OrAndQuery implements IQuery {

        @Equal
        private String columnA;

        @Criteria
        private InnerQuery innerQuery;

    }

    @Data
    static class InnerQuery {

        @Equal
        private String columnB;

        @NotEqual
        private String columnC;

    }

}
