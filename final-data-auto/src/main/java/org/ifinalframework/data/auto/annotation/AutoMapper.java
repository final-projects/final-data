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

package org.ifinalframework.data.auto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Auto generate java mapper source.
 *
 * <pre class="code">
 *      &64;Mapper
 *      public interface XXXMapper extends AbsMapper&lt;Long,XXX&gt;{
 *
 *      }
 * </pre>
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.SOURCE)
public @interface AutoMapper {

    String[] value() default {};

    String entity() default "entity";

    String mapper() default "dao.mapper";

}
