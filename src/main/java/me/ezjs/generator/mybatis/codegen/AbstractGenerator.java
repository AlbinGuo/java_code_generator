/**
 * Copyright 2006-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.ezjs.generator.mybatis.codegen;

import java.util.List;

import me.ezjs.generator.mybatis.api.IntrospectedTable;
import me.ezjs.generator.mybatis.api.ProgressCallback;
import me.ezjs.generator.mybatis.config.Context;

/**
 *
 * @author Jeff Butler
 *
 */
public abstract class AbstractGenerator {
    protected Context context;
    protected IntrospectedTable introspectedTable;
    protected List<String> warnings;
    protected ProgressCallback progressCallback;

    public AbstractGenerator() {
        super();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }

    public void setIntrospectedTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public ProgressCallback getProgressCallback() {
        return progressCallback;
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }
}