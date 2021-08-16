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

DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id            BIGINT(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    name          VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    password      VARCHAR(30) NULL DEFAULT NULL COMMENT '密码',
    age           INT(11)     NULL DEFAULT NULL COMMENT '年龄',
    last_modified DATETIME    NULL DEFAULT NULL ON UPDATE NOW() COMMENT '更新时间',
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS person;

CREATE TABLE person
(
    id           BIGINT(20)  NOT NULL AUTO_INCREMENT COMMENT '流水号',
    name         VARCHAR(30) NOT NULL COMMENT 'name',
    age          INT(11)     NULL DEFAULT NULL COMMENT '年龄',
    creator      BIGINT(20)  NULL DEFAULT NULL COMMENT 'creator',
    creator_name VARCHAR(30) NOT NULL COMMENT 'creator_name',
    PRIMARY KEY (id)
)