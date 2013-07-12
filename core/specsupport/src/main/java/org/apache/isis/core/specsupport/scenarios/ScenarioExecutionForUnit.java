/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.isis.core.specsupport.scenarios;

import java.util.Map;

import com.google.common.collect.Maps;

import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.States;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.internal.ExpectationBuilder;
import org.jmock.lib.legacy.ClassImposteriser;

import org.apache.isis.applib.DomainObjectContainer;

/**
 * An implementation of {@link ScenarioExecution} with which uses JMock to provide
 * all services.
 * 
 * <p>
 * Expectations can be {@link Mockery#checking(org.jmock.internal.ExpectationBuilder) set} 
 * and interactions {@link Mockery#assertIsSatisfied() verified} by 
 * {@link #mockery() accessing} the underlying {@link Mockery}.  
 */
public class ScenarioExecutionForUnit extends ScenarioExecution {

    private static class DomainServiceProviderMockery implements DomainServiceProvider {

        private DomainObjectContainer mockContainer = null;
        private final Map<Class<?>, Object> mocks = Maps.newHashMap();
        
        private final Mockery context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};
        private ScenarioExecution scenarioExecution;

        @Override
        public DomainObjectContainer getContainer() {
            if(mockContainer == null) {
                mockContainer = getService(DomainObjectContainer.class);
                context.checking(new Expectations() {
                    {
                        allowing(mockContainer).newTransientInstance(with(Expectations.<Class<?>>anything()));
                        will(new Action() {
                            
                            @SuppressWarnings("rawtypes")
                            public Object invoke(Invocation invocation) throws Throwable {
                                Class cls = (Class) invocation.getParameter(0);
                                return scenarioExecution.injectServices(cls.newInstance());
                            }
                            
                            public void describeTo(Description description) {
                                description.appendText("newTransientInstance");
                            }
                        });
                        
                        allowing(mockContainer).persistIfNotAlready(with(anything()));
                    }
                });
            }
            return mockContainer;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T getService(Class<T> serviceClass) {
            Object mock = mocks.get(serviceClass);
            if(mock == null) {
                mock = context.mock(serviceClass);
            }
            mocks.put(serviceClass, mock);
            return (T) mock;
        }
        
        public Mockery mockery() {
            return context;
        }

        private DomainServiceProviderMockery init(ScenarioExecution scenarioExecution) {
            this.scenarioExecution = scenarioExecution;
            return this;
        }
    }

    private final ScenarioExecutionForUnit.DomainServiceProviderMockery dspm;
    
    public ScenarioExecutionForUnit() {
        this(new DomainServiceProviderMockery());
    }
    private ScenarioExecutionForUnit(ScenarioExecutionForUnit.DomainServiceProviderMockery dspm) {
        super(dspm);
        this.dspm = dspm.init(this);
    }
    
    // //////////////////////////////////////

    public void checking(ExpectationBuilder expectations) {
        dspm.mockery().checking(expectations);
    }
    
    public void assertIsSatisfied() {
        dspm.mockery().assertIsSatisfied();
    }
    
    public Sequence sequence(String name) {
        return dspm.mockery().sequence(name);
    }
    public States states(String name) {
        return dspm.mockery().states(name);
    }

}
