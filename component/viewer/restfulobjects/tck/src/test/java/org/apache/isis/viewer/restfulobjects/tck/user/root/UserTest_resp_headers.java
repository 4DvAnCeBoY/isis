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
package org.apache.isis.viewer.restfulobjects.tck.user.root;

import static org.apache.isis.viewer.restfulobjects.tck.RestfulMatchers.assertThat;
import static org.apache.isis.viewer.restfulobjects.tck.RestfulMatchers.hasMaxAge;
import static org.apache.isis.viewer.restfulobjects.tck.RestfulMatchers.hasParameter;
import static org.apache.isis.viewer.restfulobjects.tck.RestfulMatchers.hasSubType;
import static org.apache.isis.viewer.restfulobjects.tck.RestfulMatchers.hasType;
import static org.apache.isis.viewer.restfulobjects.tck.RestfulMatchers.isArray;
import static org.apache.isis.viewer.restfulobjects.tck.RestfulMatchers.isFollowableLinkToSelf;
import static org.apache.isis.viewer.restfulobjects.tck.RestfulMatchers.isLink;
import static org.apache.isis.viewer.restfulobjects.tck.RestfulMatchers.isMap;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.apache.isis.viewer.restfulobjects.applib.Rel;
import org.apache.isis.viewer.restfulobjects.applib.RepresentationType;
import org.apache.isis.viewer.restfulobjects.applib.RestfulHttpMethod;
import org.apache.isis.viewer.restfulobjects.applib.client.RestfulClient;
import org.apache.isis.viewer.restfulobjects.applib.client.RestfulResponse;
import org.apache.isis.viewer.restfulobjects.applib.client.RestfulResponse.Header;
import org.apache.isis.viewer.restfulobjects.applib.client.RestfulResponse.HttpStatusCode;
import org.apache.isis.viewer.restfulobjects.applib.homepage.HomePageRepresentation;
import org.apache.isis.viewer.restfulobjects.applib.user.UserRepresentation;
import org.apache.isis.viewer.restfulobjects.applib.user.UserResource;
import org.apache.isis.viewer.restfulobjects.tck.IsisWebServerRule;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class UserTest_resp_headers {

    @Rule
    public IsisWebServerRule webServerRule = new IsisWebServerRule();

    private RestfulClient client;
    private UserResource resource;

    @Before
    public void setUp() throws Exception {
        client = webServerRule.getClient();
        resource = client.getUserResource();
    }


    @Test
    public void contentType_and_cacheControl() throws Exception {

        // given
        final Response resp = resource.user();

        // when
        final RestfulResponse<UserRepresentation> restfulResponse = RestfulResponse.ofT(resp);

        // then
        final MediaType contentType = restfulResponse.getHeader(Header.CONTENT_TYPE);
        assertThat(contentType, hasType("application"));
        assertThat(contentType, hasSubType("json"));
        assertThat(contentType, hasParameter("profile", "urn:org.restfulobjects:repr-types/user"));
        assertThat(contentType, is(RepresentationType.USER.getMediaType()));

        // then
        final CacheControl cacheControl = restfulResponse.getHeader(Header.CACHE_CONTROL);
        assertThat(cacheControl, hasMaxAge(60 * 60));
        assertThat(cacheControl.getMaxAge(), is(60 * 60));
    }
}
