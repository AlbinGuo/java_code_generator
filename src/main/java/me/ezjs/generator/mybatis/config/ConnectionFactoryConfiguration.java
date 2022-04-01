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
package me.ezjs.generator.mybatis.config;

import static me.ezjs.generator.mybatis.internal.util.StringUtility.stringHasValue;
import static me.ezjs.generator.mybatis.internal.util.messages.Messages.getString;

import java.util.List;

import me.ezjs.generator.mybatis.api.dom.xml.Attribute;
import me.ezjs.generator.mybatis.internal.util.messages.Messages;
import me.ezjs.generator.mybatis.api.dom.xml.XmlElement;
import me.ezjs.generator.mybatis.internal.util.StringUtility;

public class ConnectionFactoryConfiguration extends TypedPropertyHolder {

    public ConnectionFactoryConfiguration() {
        super();
    }

    public void validate(List<String> errors) {
        if (getConfigurationType() == null || "DEFAULT".equals(getConfigurationType())) { //$NON-NLS-1$
            if (!StringUtility.stringHasValue(getProperty("driverClass"))) { //$NON-NLS-1$
                errors.add(Messages.getString("ValidationError.18", "connectionFactory", "driverClass")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }

            if (!StringUtility.stringHasValue(getProperty("connectionURL"))) { //$NON-NLS-1$
                errors.add(Messages.getString("ValidationError.18", "connectionFactory", "connectionURL")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }
    }

    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("connectionFactory"); //$NON-NLS-1$

        if (stringHasValue(getConfigurationType())) {
            xmlElement.addAttribute(new Attribute("type", getConfigurationType())); //$NON-NLS-1$
        }

        addPropertyXmlElements(xmlElement);

        return xmlElement;
    }
}
