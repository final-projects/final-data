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

package org.ifinalframework.data.mybatis.spi;

import org.springframework.stereotype.Component;

import org.ifinalframework.context.user.UserSupplier;
import org.ifinalframework.core.IUser;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

/**
 * UserMapParameterConsumer.
 *
 * @author iimik
 * @version 1.5.0
 * @since 1.5.0
 */
@Component
@RequiredArgsConstructor
public class UserMapParameterConsumer implements MapParameterConsumer {

    private final List<UserSupplier> userSuppliers;

    @Override
    public void accept(Map<String, Object> parameter, Class<?> mapper, Method method) {

        final IUser<?> iUser = userSuppliers.stream().map(Supplier::get).filter(Objects::nonNull).findFirst()
                .orElse(null);
        parameter.putIfAbsent("USER", iUser);
    }
}
