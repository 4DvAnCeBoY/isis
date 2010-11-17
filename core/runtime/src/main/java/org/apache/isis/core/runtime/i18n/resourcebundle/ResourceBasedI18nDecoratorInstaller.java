/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


package org.apache.isis.core.runtime.i18n.resourcebundle;

import java.util.Arrays;
import java.util.List;

import org.apache.isis.core.metamodel.facetdecorator.FacetDecorator;
import org.apache.isis.core.metamodel.specloader.FacetDecoratorInstaller;
import org.apache.isis.core.runtime.installers.InstallerAbstract;


public class ResourceBasedI18nDecoratorInstaller extends InstallerAbstract implements FacetDecoratorInstaller {

    public ResourceBasedI18nDecoratorInstaller() {
		super(FacetDecoratorInstaller.TYPE, "resource-i18n");
	}
    
	public List<FacetDecorator> createDecorators() {
        final ResourceBasedI18nManager manager = new ResourceBasedI18nManager(getConfiguration());
        manager.init();
        return Arrays.<FacetDecorator>asList(new I18nFacetDecorator(manager));
    }
	
	@Override
	public List<Class<?>> getTypes() {
		return listOf(List.class); // ie, of List<FacetDecorator>
	}
}
