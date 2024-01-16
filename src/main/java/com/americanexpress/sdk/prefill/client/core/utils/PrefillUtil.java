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
package com.americanexpress.sdk.prefill.client.core.utils;

import com.americanexpress.sdk.prefill.client.http.ApiClientFactory;
import com.americanexpress.sdk.prefill.configuration.Config;
import com.americanexpress.sdk.prefill.exception.PayloadEncryptionError;
import com.americanexpress.sdk.prefill.exception.PrefillException;
import com.americanexpress.sdk.prefill.models.entities.RequestHeader;
import com.americanexpress.sdk.prefill.models.prefill.PrefillRequest;
import com.americanexpress.sdk.prefill.service.constants.PrefillApiConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.nimbusds.jose.*;
import lombok.experimental.UtilityClass;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPublicKey;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * The PrefillUtil class handles the Prefill Service API call specific
 * utility methods
 *
 */
@UtilityClass
public class PrefillUtil {

    /**
     * This method is responsible to build request entity for HttpClient
     *
     * @param request a {@link java.lang.Object} object
     * @return HttpEntity
     * @throws java.io.UnsupportedEncodingException exception
     * @throws com.fasterxml.jackson.core.JsonProcessingException json exception
     */
    public static HttpEntity buildRequestEntity(Object request)
            throws UnsupportedEncodingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new StringEntity(mapper.writeValueAsString(request));
    }

    /**
     * This method retrieves the Json to deserialized string
     *
     * @param deserialized a T object
     * @return T String
     * @throws java.io.IOException  exception
     * @param <T> a T class
     */
    public static <T> String getJson(T deserialized) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper.writeValueAsString(deserialized);
    }

    /**
     * This method is the builder for Prefill request Headers
     *
     * @param requestHeader a {@link com.americanexpress.sdk.prefill.models.entities.RequestHeader} object
     * @param config a {@link com.americanexpress.sdk.prefill.configuration.Config} object
     * @return MultivaluedMap a multiValueMap
     */
    public static MultivaluedMap<String, Object> buildHeaders(RequestHeader requestHeader, Config config) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        addHeader(headers, PrefillApiConstants.REQUEST_HEADER_RESOURCE_ID, requestHeader.getResourceId());
        addHeader(headers, PrefillApiConstants.REQUEST_HEADER_REQUEST_ID, requestHeader.getRequestId());
        addHeader(headers, PrefillApiConstants.REQUEST_HEADER_DATA_PROVIDER, requestHeader.getDataProvider());
        addHeader(headers, PrefillApiConstants.REQUEST_HEADER_CLIENT_ID, requestHeader.getClientId());
        addHeader(headers, PrefillApiConstants.REQUEST_HEADER_CONSENT_INFO, requestHeader.getConsentInfo());
        addHeader(headers, PrefillApiConstants.REQUEST_HEADER_REDIRECT_URI, requestHeader.getRedirectUri());
        addHeader(headers, PrefillApiConstants.REQUEST_HEADER_MESSAGE_TYPE_ID, requestHeader.getMessageTypeId());
        addHeader(headers, PrefillApiConstants.REQUEST_HEADER_COUNTRY_CODE, requestHeader.getCountryCode());
        addHeader(headers, PrefillApiConstants.REQUEST_HEADER_DATA_PROVIDER_AUTH_TOKEN, requestHeader.getDataProviderAuthToken());
        addHeader(headers, PrefillApiConstants.REQUEST_HEADER_CONTENT_TYPE, PrefillApiConstants.APPLICATION_JSON_UTF8);
        addHeader(headers, PrefillApiConstants.MAC_ID, config.getApiKey());
        addHeader(headers, PrefillApiConstants.AUTHORIZATION, PrefillApiConstants.BEARER + " " + config.getAccessToken());
        return headers;
    }

    /**
     * This method checks that a header is not null before adding it to the headers map
     *
     * @param headers
     * @param item
     * @param value
     */
    private static void addHeader(MultivaluedMap<String, Object> headers, String item, Object value) {
        if (null != value) {
            headers.add(item, value);
        }
    }

    /**
     * This method checks that a header is not empty before adding it to the headers map
     *
     * @param headers
     * @param item
     * @param value
     */
    private static void addHeader(MultivaluedMap<String, Object> headers, String item, String value) {
        if (isNotEmpty(value)) {
            headers.add(item, value);
        }
    }

    /**
     * This method gets the response data string
     *
     * @param entity a {@link org.apache.http.HttpEntity} object
     * @return String
     */
    public static String getResponseString(HttpEntity entity) {
        if (entity != null) {
            try {
                return EntityUtils.toString(entity);
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    /**
     * JWE encryption
     *
     * @param publicKey a {@link java.security.interfaces.RSAPublicKey} object
     * @return String
     * @throws com.americanexpress.sdk.prefill.exception.PrefillException PrefillException
     * @param prefillData a {@link com.americanexpress.sdk.prefill.models.prefill.PrefillRequest} object
     */
    public static String encrypt(PrefillRequest prefillData, RSAPublicKey publicKey)
            throws PrefillException {
        String response = null;
        JWEHeader jweHeader = null;
        try {
            jweHeader = ApiClientFactory.createJWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM);
            JWEObject jweObject = ApiClientFactory.createJWEObject(jweHeader, new Payload(PrefillUtil.getJson(prefillData)));

            /** Encrypt with public key */
            JWEEncrypter encrypter = ApiClientFactory.createRSAEncrypter(publicKey);
            jweObject.encrypt(encrypter);

            /** Output JWE string */
            response = jweObject.serialize();
        } catch (JOSEException | IOException joseEx) {
            throw new PayloadEncryptionError(joseEx.getMessage(), joseEx);
        }
        return response;
    }

    /**
     * This method is to convert response based on content-type
     *
     * @param responseObject a {@link com.fasterxml.jackson.core.type.TypeReference} object
     * @param httpResponse a {@link org.apache.http.client.methods.CloseableHttpResponse} object
     * @throws java.io.IOException exception
     * @param <R> a R class
     * @return R object
     */
    public static <R> R generateResponse(TypeReference<R> responseObject, CloseableHttpResponse httpResponse)
            throws IOException {
        R response = null;
        final InputStream content = httpResponse.getEntity().getContent();
        ObjectMapper mapperForGetResponse = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapperForGetResponse.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        response = mapperForGetResponse.readValue(content, responseObject);
        return response;
    }
}
