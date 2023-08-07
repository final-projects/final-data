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

package org.ifinalframework.data.mybatis.entity;

import org.ifinalframework.data.annotation.AbsUser;
import org.ifinalframework.data.annotation.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User.
 *
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */
@Setter
@Getter
@Table("`user`")
@NoArgsConstructor
public class User extends AbsUser {

    private String password;

    private Integer age;

    public User(String name, String password) {
        this.setName(name);
        this.password = password;
    }
}
