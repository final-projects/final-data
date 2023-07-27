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

package org.ifinalframework.data.domain.excel;

import org.ifinalframework.context.exception.InternalServerException;
import org.ifinalframework.json.Json;
import org.ifinalframework.poi.Excel;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassPathDomainResourceExcelExportProvider
 *
 * @author mik
 * @since 1.5.2
 **/
public class ClassPathDomainResourceExcelExportProvider implements DomainResourceExcelExportProvider {

    private static final String EXCEL_RESOURCE_PATH = "classpath:excel/%s.json";

    private final Map<String, Excel> cache = new ConcurrentHashMap<>();

    @Override
    public Excel getResourceExcel(String resource, Class<?> exportClass) throws Exception {


        return cache.computeIfAbsent(resource, key -> {

            try {
                final String excelJson = String.format(EXCEL_RESOURCE_PATH, resource);
                final File file = ResourceUtils.getFile(excelJson);
                final String json = Files.readString(Path.of(file.toURI()));
                return Json.toObject(json, Excel.class);
            } catch (FileNotFoundException e) {
                throw new InternalServerException("导出配置文件不存在：" + e.getMessage());
            } catch (IOException e) {
                throw new InternalServerException("导出配置文件解析异常:" + e.getMessage());
            }

        });


    }
}
