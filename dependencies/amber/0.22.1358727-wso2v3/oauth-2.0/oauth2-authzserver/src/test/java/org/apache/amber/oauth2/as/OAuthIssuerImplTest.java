/**
 *       Copyright 2010 Newcastle University
 *
 *          http://research.ncl.ac.uk/smart/
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.amber.oauth2.as;

import junit.framework.Assert;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.junit.Test;
import org.apache.amber.oauth2.as.issuer.OAuthIssuer;
import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;

/**
 *
 *
 *
 */
public class OAuthIssuerImplTest {
    private OAuthIssuer issuer = new OAuthIssuerImpl(new MD5Generator());

    @Test
    public void testAccessToken() throws Exception {
        Assert.assertNotNull(issuer.accessToken());
    }

    @Test
    public void testRefreshToken() throws Exception {
        Assert.assertNotNull(issuer.refreshToken());
    }

    @Test
    public void testAuthorizationCode() throws Exception {
        Assert.assertNotNull(issuer.authorizationCode());
    }
}