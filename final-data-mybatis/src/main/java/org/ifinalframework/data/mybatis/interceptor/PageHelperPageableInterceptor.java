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

package org.ifinalframework.data.mybatis.interceptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.ifinalframework.core.Pageable;
import org.ifinalframework.util.Asserts;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PageHelperPageableInterceptor.
 *
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */
@Intercepts(
    {
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
            RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
            RowBounds.class, ResultHandler.class, CacheKey.class,
            BoundSql.class}),
    }
)
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
@Component
@ConditionalOnClass(PageHelper.class)
public class PageHelperPageableInterceptor extends PageableInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(PageHelperPageableInterceptor.class);

    @Override
    protected void startPage(final Pageable pageable) {

        if (Asserts.isNull(pageable) || Asserts.isNull(pageable.getPage()) || Asserts.isNull(pageable.getSize())) {
            return;
        }
        startPage(pageable.getPage(), pageable.getSize(), Boolean.TRUE.equals(pageable.getCount()), false, false);
    }

    /**
     * @param pageNum      ??????
     * @param pageSize     ??????????????????
     * @param count        ????????????count??????
     * @param reasonable   ???????????????,null??????????????????
     * @param pageSizeZero true???pageSize=0????????????????????????false?????????,null??????????????????
     * @see PageHelper#startPage(int, int, boolean, Boolean, Boolean)
     */
    private void startPage(final int pageNum, final int pageSize, final boolean count, final Boolean reasonable,
        final Boolean pageSizeZero) {

        final Page<Object> result = PageMethod.startPage(pageNum, pageSize, count, reasonable, pageSizeZero);
        logger.info("pageResult:page={},size={},pages={},total={}",
            result.getPageNum(), result.getPageSize(), result.getPages(), result.getTotal());
    }

}
