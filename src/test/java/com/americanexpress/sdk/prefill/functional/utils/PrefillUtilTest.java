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
package com.americanexpress.sdk.prefill.functional.utils;

import com.americanexpress.sdk.prefill.client.core.utils.PrefillUtil;
import com.americanexpress.sdk.prefill.client.http.ApiClientFactory;
import com.americanexpress.sdk.prefill.configuration.Config;
import com.americanexpress.sdk.prefill.exception.PayloadEncryptionError;
import com.americanexpress.sdk.prefill.exception.PrefillException;
import com.americanexpress.sdk.prefill.models.entities.ConsentInfo;
import com.americanexpress.sdk.prefill.models.entities.RequestHeader;
import com.americanexpress.sdk.prefill.models.prefill.PrefillRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSAEncrypter;
import org.apache.http.HttpEntity;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPublicKey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpEntity.class, ObjectMapper.class, ApiClientFactory.class, JWEHeader.class})
public class PrefillUtilTest {

    @Test
    public void testBuildRequestEntity() throws UnsupportedEncodingException, JsonProcessingException {
        PrefillRequest prefillRequest = new PrefillRequest();
        HttpEntity requestEntity = PrefillUtil.buildRequestEntity(prefillRequest);
        assertNotNull(requestEntity);
    }

    @Test
    public void testGetJson() throws IOException {
        assertNotNull(PrefillUtil.getJson(buildDeserialzed()));
    }

    @Test
    public void testBuildHeaders() {
        Config config = EasyMock.createMock(Config.class);
        EasyMock.expect(config.getApiKey()).andReturn("api_key");
        EasyMock.expect(config.getAccessToken()).andReturn("access_token");
        EasyMock.replay(config);
        assertNotNull(PrefillUtil.buildHeaders(buildRequestHeader(), config));
    }

    @Test
    public void testEncrypt() throws PrefillException, JOSEException {
        JWEHeader jweHeader = PowerMock.createMock(JWEHeader.class);
        JWEObject jweObject = EasyMock.createMock(JWEObject.class);
        RSAEncrypter rsaEncrypter = EasyMock.createMock(RSAEncrypter.class);
        RSAPublicKey publicKey = EasyMock.createMock(RSAPublicKey.class);
        PrefillRequest prefillRequest = new PrefillRequest();

        PowerMock.mockStatic(ApiClientFactory.class);
        EasyMock.expect(ApiClientFactory.createJWEHeader(EasyMock.anyObject(), EasyMock.anyObject())).andReturn(jweHeader);
        EasyMock.expect(ApiClientFactory.createJWEObject(EasyMock.anyObject(), EasyMock.anyObject())).andReturn(jweObject);
        EasyMock.expect(ApiClientFactory.createRSAEncrypter(EasyMock.eq(publicKey))).andReturn(rsaEncrypter);
        PowerMock.replay(ApiClientFactory.class);

        jweObject.encrypt(EasyMock.eq(rsaEncrypter));
        EasyMock.expectLastCall();
        EasyMock.expect(jweObject.serialize()).andReturn("encrypted_string");
        EasyMock.replay(jweObject);

        assertEquals("encrypted_string", PrefillUtil.encrypt(prefillRequest, publicKey));
    }

    @Test(expected = PayloadEncryptionError.class)
    public void testEncryptException() throws PrefillException, JOSEException {
        JWEHeader jweHeader = PowerMock.createMock(JWEHeader.class);
        JWEObject jweObject = EasyMock.createMock(JWEObject.class);
        RSAEncrypter rsaEncrypter = EasyMock.createMock(RSAEncrypter.class);
        RSAPublicKey publicKey = EasyMock.createMock(RSAPublicKey.class);
        PrefillRequest prefillRequest = new PrefillRequest();

        PowerMock.mockStatic(ApiClientFactory.class);
        EasyMock.expect(ApiClientFactory.createJWEHeader(EasyMock.anyObject(), EasyMock.anyObject())).andReturn(jweHeader);
        EasyMock.expect(ApiClientFactory.createJWEObject(EasyMock.anyObject(), EasyMock.anyObject())).andReturn(jweObject);
        EasyMock.expect(ApiClientFactory.createRSAEncrypter(EasyMock.eq(publicKey))).andReturn(rsaEncrypter);
        PowerMock.replay(ApiClientFactory.class);

        jweObject.encrypt(EasyMock.eq(rsaEncrypter));
        EasyMock.expectLastCall().andThrow(new JOSEException("Mock error"));
        EasyMock.replay(jweObject);

        assertEquals("encrypted_string", PrefillUtil.encrypt(prefillRequest, publicKey));
    }

    private <T> String buildDeserialzed() throws IOException {
        T deserialized = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper.writeValueAsString(deserialized);
    }

    private RequestHeader buildRequestHeader() {
        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setResourceId("resource id");
        requestHeader.setRequestId("request id");
        requestHeader.setDataProvider("data provider");
        requestHeader.setClientId("client id");
        requestHeader.setConsentInfo(new ConsentInfo());
        requestHeader.setRedirectUri("redirect uri");
        requestHeader.setMessageTypeId("message type id");
        requestHeader.setCountryCode("country code");
        requestHeader.setDataProviderAuthToken("data provider auth token");
        return requestHeader;
    }
}