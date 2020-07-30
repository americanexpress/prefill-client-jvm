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

import com.americanexpress.sdk.prefill.client.core.utils.PrefillUtil;
import com.americanexpress.sdk.prefill.client.http.HttpClient;
import com.americanexpress.sdk.prefill.configuration.Config;
import com.americanexpress.sdk.prefill.configuration.JWEConfig;
import com.americanexpress.sdk.prefill.exception.PrefillException;
import com.americanexpress.sdk.prefill.models.entities.RequestHeader;
import com.americanexpress.sdk.prefill.models.prefill.PrefillRequest;
import com.americanexpress.sdk.prefill.models.prefill.PrefillResponsePushResponse;
import com.americanexpress.sdk.prefill.models.prefill.PushResponse;
import com.americanexpress.sdk.prefill.service.PrefillService;
import com.americanexpress.sdk.prefill.service.constants.PrefillApiConstants;
import com.americanexpress.sdk.prefill.service.impl.PrefillServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpEntity;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PrefillUtil.class)
public class PrefillServiceImplTest {

    private HttpClient authClient;
    private PrefillService prefillService;
    private PrefillRequest prefillRequest;
    private RequestHeader requestHeader;
    private PrefillResponsePushResponse prefillResponsePushResponse;

    @Before
    public void setUp() {
        RSAPublicKey publicKey = EasyMock.createNiceMock(RSAPublicKey.class);
        Config config = Config.builder().accessToken("accessToken")
                .url("https://example.americanexpress.com")
                .jweConfig(new JWEConfig(true, publicKey)).build();
        authClient = EasyMock.createNiceMock(HttpClient.class);
        prefillService = new PrefillServiceImpl(config, authClient);
        prefillRequest = new PrefillRequest();
        requestHeader = RequestHeader.builder().build();
        prefillResponsePushResponse = new PrefillResponsePushResponse();
        prefillResponsePushResponse.setPrefillInfo(new PushResponse());
    }

    @Test
    public void testSaveData() throws PrefillException{
        EasyMock.expect(authClient.postClientResource(EasyMock.isA(HttpEntity.class),
                EasyMock.isA(String.class),
                EasyMock.isA(MultivaluedMap.class),
                (TypeReference<Object>) EasyMock.isA(Object.class),
                EasyMock.isA(Map.class)))
                .andReturn(prefillResponsePushResponse);
        EasyMock.replay(authClient);

        PrefillResponsePushResponse result = prefillService.saveData(prefillRequest, requestHeader);
        assertNotNull(result);
    }

    @Test
    public void testSaveEncryptedData() throws PrefillException{
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.add(PrefillApiConstants.AUTHORIZATION, "authorization");

        PowerMock.mockStatic(PrefillUtil.class);
        EasyMock.expect(PrefillUtil.buildHeaders(EasyMock.isA(RequestHeader.class), EasyMock.isA(Config.class)))
                .andReturn(headers);
        EasyMock.expect(PrefillUtil.encrypt(EasyMock.isA(PrefillRequest.class), EasyMock.isA(RSAPublicKey.class)))
                .andReturn("encrypted data");
        PowerMock.replay(PrefillUtil.class);

        EasyMock.expect(authClient.postClientResource(EasyMock.isA(HttpEntity.class),
                EasyMock.isA(String.class),
                EasyMock.isA(MultivaluedMap.class),
                (TypeReference<Object>) EasyMock.isA(Object.class),
                EasyMock.isA(Map.class)))
                .andReturn(prefillResponsePushResponse);
        EasyMock.replay(authClient);

        PrefillResponsePushResponse result = prefillService.saveEncryptedData(prefillRequest, requestHeader);
        assertNotNull(result);
    }
}