/*
 * Copyright 2020 American Express Travel Related Services Company, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.americanexpress.sdk.prefill.functional.service;

import static org.junit.Assert.assertNotNull;

import com.americanexpress.sdk.prefill.configuration.Config;
import com.americanexpress.sdk.prefill.exception.PrefillException;
import com.americanexpress.sdk.prefill.client.http.HttpClient;
import com.americanexpress.sdk.prefill.exception.PrefillRequestValidationError;
import com.americanexpress.sdk.prefill.models.entities.AccessTokenResponse;
import com.americanexpress.sdk.prefill.service.impl.AuthenticationServiceImpl;
import org.apache.http.HttpEntity;
import org.easymock.classextension.EasyMock;
import org.junit.Test;

import com.americanexpress.sdk.prefill.service.AuthenticationService;
import com.fasterxml.jackson.core.type.TypeReference;


public class AuthenticationServiceImplTest {

    HttpClient authClient;
    Config config;
    AuthenticationService authenticationService;

	@Test
    public void testGetAccessToken() throws PrefillException{
        authClient = EasyMock.createNiceMock(HttpClient.class);
        config = Config.builder().apiKey("apiKey").apiSecret("apiSecret")
                .url("https://example.americanexpress.com").build();
        authenticationService = new AuthenticationServiceImpl(config, authClient);
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();

        EasyMock.expect(authClient.postClientResource(EasyMock.isA(HttpEntity.class), EasyMock.isA(String.class),
                EasyMock.anyObject(), (TypeReference<Object>) EasyMock.isA(Object.class), EasyMock.anyObject()))
                .andReturn(accessTokenResponse);
        EasyMock.replay(authClient);
	    AccessTokenResponse response = authenticationService.getAccessToken();
	    assertNotNull(response);
    }

    @Test (expected = PrefillRequestValidationError.class)
    public void testGetAccessToken_InvalidConfig() throws PrefillException{
        authClient = EasyMock.createNiceMock(HttpClient.class);
        config = Config.builder().build();
        authenticationService= new AuthenticationServiceImpl(config, authClient);
        authenticationService.getAccessToken();
    }
}
