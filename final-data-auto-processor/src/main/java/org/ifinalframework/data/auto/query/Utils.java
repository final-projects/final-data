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

package org.ifinalframework.data.auto.query;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import org.ifinalframework.data.annotation.ReferenceMode;
import org.ifinalframework.data.auto.entity.Property;

/**
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Utils {

    static String formatPropertyName(final @Nullable Property referenceProperty, final @NonNull Property property) {

        if (referenceProperty == null) {
            return property.getName();
        } else {

            if (property.isIdProperty()) {
                if (referenceProperty.referenceMode() == ReferenceMode.SIMPLE) {
                    return referenceProperty.getName();
                }

                return referenceProperty.getName() + property.getName().substring(0, 1).toUpperCase() + property
                    .getName().substring(1);
            }

            return referenceProperty.getName() + property.getName().substring(0, 1).toUpperCase() + property.getName()
                .substring(1);
        }
    }

}
