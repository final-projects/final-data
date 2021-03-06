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

package org.ifinalframework.data.auto;

import org.ifinalframework.data.auto.annotation.Template;
import org.ifinalframework.util.Asserts;

import java.io.Writer;

/**
 * The generator of template code.
 *
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Coder {

    static Coder getDefaultCoder() {
        return new VelocityCoder();
    }

    default void coding(Object model, Writer writer) {
        Asserts.requiredNonNull(model, "the model must not ne null!");
        Asserts.requiredNonNull(writer, "the writer must not be null!");

        Template template = model.getClass().getAnnotation(Template.class);

        if (template == null) {
            throw new NullPointerException("the model must one Template annotation , model=" + model
                .getClass()
                .getName());
        }
        coding(template.value(), model, writer);
    }

    /**
     * coding the template code with data model by writer.
     *
     * @param template the name of template
     * @param model    the data model of template
     * @param writer   the writer of coding file
     */
    void coding(String template, Object model, Writer writer);

}
