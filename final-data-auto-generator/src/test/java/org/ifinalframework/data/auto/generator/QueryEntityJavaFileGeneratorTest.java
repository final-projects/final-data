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

package org.ifinalframework.data.auto.generator;

import org.ifinalframework.data.annotation.AbsEntity;
import org.ifinalframework.data.auto.annotation.AutoQuery;
import org.ifinalframework.data.query.AbsQEntity;
import org.ifinalframework.java.compiler.Compiler;
import org.ifinalframework.java.compiler.DynamicClassLoader;

import com.squareup.javapoet.JavaFile;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * QueryEntityJavaFileGeneratorTest.
 *
 * @author ilikly
 * @version 1.4.2
 * @since 1.4.2
 */
class QueryEntityJavaFileGeneratorTest {

    @Test
    @SneakyThrows
    void generate() {
        QueryEntityJavaFileGenerator generator = new QueryEntityJavaFileGenerator();
        AutoQuery autoService = Mockito.mock(AutoQuery.class);
        JavaFile javaFile = generator.generate(autoService, AbsEntity.class);
        Compiler compiler = new Compiler(getClass().getClassLoader());
        String name = generator.getName(autoService, AbsEntity.class);
        compiler.addSource(name, javaFile.toString());
        DynamicClassLoader compile = compiler.compile();
        Class<?> query = compile.getClasses().get(name);
        Assertions.assertTrue(AbsQEntity.class.isAssignableFrom(query));
    }
}