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

package org.apache.isis.core.progmodel.facets.object.aggregated;

import org.apache.isis.applib.annotation.Aggregated;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facets.FacetFactory.ProcessClassContext;
import org.apache.isis.core.metamodel.facets.object.aggregated.ParentedFacet;
import org.apache.isis.core.progmodel.facets.AbstractFacetFactoryTest;
import org.apache.isis.core.progmodel.facets.object.aggregated.annotation.AggregatedAnnotationFacetFactory;
import org.apache.isis.core.progmodel.facets.object.aggregated.annotation.ParentedFacetSinceAggregatedAnnotation;

public class AggregatedAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private AggregatedAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new AggregatedAnnotationFacetFactory();
    }

    @Override
    protected void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }

    public void testAggregatedAnnotationPickedUpOnClass() {
        @Aggregated
        class Customer {
        }

        facetFactory.process(new ProcessClassContext(Customer.class, null, methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(ParentedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ParentedFacetSinceAggregatedAnnotation);

        assertNoMethodsRemoved();
    }

}
