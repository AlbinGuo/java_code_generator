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
package me.ezjs.generator.mybatis.internal.util;

import static org.junit.Assert.assertEquals;

import me.ezjs.generator.mybatis.api.dom.java.FullyQualifiedJavaType;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jeff Butler
 *
 */
public class JavaBeansUtilTest {

    /**
     *
     */
    public JavaBeansUtilTest() {
        super();
    }

    @Test
    public void testGetValidPropertyName() {
        Assert.assertEquals("eMail", JavaBeansUtil.getValidPropertyName("eMail")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("firstName", JavaBeansUtil.getValidPropertyName("firstName")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("URL", JavaBeansUtil.getValidPropertyName("URL")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("XAxis", JavaBeansUtil.getValidPropertyName("XAxis")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("a", JavaBeansUtil.getValidPropertyName("a")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("b", JavaBeansUtil.getValidPropertyName("B")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("yaxis", JavaBeansUtil.getValidPropertyName("Yaxis")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("i_PARAM_INT_1", JavaBeansUtil.getValidPropertyName("I_PARAM_INT_1")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("_fred", JavaBeansUtil.getValidPropertyName("_fred")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("accountType", JavaBeansUtil.getValidPropertyName("AccountType")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void testGetGetterMethodName() {
        Assert.assertEquals("geteMail", JavaBeansUtil.getGetterMethodName("eMail", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("getFirstName", JavaBeansUtil.getGetterMethodName("firstName", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("getURL", JavaBeansUtil.getGetterMethodName("URL", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("getXAxis", JavaBeansUtil.getGetterMethodName("XAxis", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("getA", JavaBeansUtil.getGetterMethodName("a", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("isActive", JavaBeansUtil.getGetterMethodName("active", FullyQualifiedJavaType.getBooleanPrimitiveInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("getI_PARAM_INT_1", JavaBeansUtil.getGetterMethodName("i_PARAM_INT_1", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("get_fred", JavaBeansUtil.getGetterMethodName("_fred", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("getAccountType", JavaBeansUtil.getGetterMethodName("AccountType", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void testGetSetterMethodName() {
        Assert.assertEquals("seteMail", JavaBeansUtil.getSetterMethodName("eMail")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("setFirstName", JavaBeansUtil.getSetterMethodName("firstName")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("setURL", JavaBeansUtil.getSetterMethodName("URL")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("setXAxis", JavaBeansUtil.getSetterMethodName("XAxis")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("setA", JavaBeansUtil.getSetterMethodName("a")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("setI_PARAM_INT_1", JavaBeansUtil.getSetterMethodName("i_PARAM_INT_1")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("set_fred", JavaBeansUtil.getSetterMethodName("_fred")); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("setAccountType", JavaBeansUtil.getSetterMethodName("AccountType")); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
