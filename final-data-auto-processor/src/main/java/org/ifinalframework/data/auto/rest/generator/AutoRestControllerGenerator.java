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

package org.ifinalframework.data.auto.rest.generator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

import org.ifinalframework.core.IEntity;
import org.ifinalframework.data.auto.annotation.AutoRestController;
import org.ifinalframework.data.auto.generator.AutoGenerator;

/**
 * AutoMapperGenerator.
 *
 * <pre class="code">
 *      package xxx.dao.mapper;
 *
 *      &#64;Mapper
 *      public interface XxxMapper extends AbsMapper&lt;Long,XxxEntity&gt;{
 *
 *      }
 * </pre>
 *
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */
public class AutoRestControllerGenerator implements AutoGenerator<AutoRestController, TypeElement> {

    private static final String CONTROLLER_SUFFIX = "RestController";

    private final RestControllerGenerator restControllerGenerator = new DefaultRestControllerGenerator();

    private final ProcessingEnvironment processingEnv;

    public AutoRestControllerGenerator(final ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    @Override
    public void generate(final AutoRestController ann, final TypeElement entity) {

        final String packageName = processingEnv.getElementUtils().getPackageOf(entity).getQualifiedName().toString()
                .replace(".entity", ".web.controller");
        String mapperName = entity.getSimpleName().toString() + CONTROLLER_SUFFIX;

        final String elementName = packageName + "." + mapperName;

        try {
            final TypeElement mapperElement = processingEnv.getElementUtils().getTypeElement(elementName);

            if (mapperElement == null) {
                final JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(elementName);

                try (Writer writer = sourceFile.openWriter()) {
                    Class<? extends IEntity> clazz = (Class<? extends IEntity>) Class.forName(entity.getQualifiedName().toString());
                    String source = restControllerGenerator.generate(clazz);
                    writer.write(source);
                    writer.flush();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }

        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    private void error(final String msg) {

        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }

}
