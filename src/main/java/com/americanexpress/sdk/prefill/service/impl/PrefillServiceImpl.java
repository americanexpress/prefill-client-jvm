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
package com.americanexpress.sdk.prefill.service.impl;

import com.americanexpress.sdk.prefill.client.core.utils.PrefillUtil;
import com.americanexpress.sdk.prefill.client.http.HttpClient;
import com.americanexpress.sdk.prefill.configuration.Config;
import com.americanexpress.sdk.prefill.exception.*;
import com.americanexpress.sdk.prefill.models.entities.RequestHeader;
import com.americanexpress.sdk.prefill.models.prefill.EncryptedPrefillRequest;
import com.americanexpress.sdk.prefill.models.prefill.PrefillRequest;
import com.americanexpress.sdk.prefill.models.prefill.PrefillResponsePushResponse;
import com.americanexpress.sdk.prefill.service.PrefillService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;

import static com.americanexpress.sdk.prefill.service.constants.PrefillApiConstants.*;
import static com.americanexpress.sdk.prefill.service.constants.PrefillExceptionConstants.*;


/**
 * The Prefill Service Implementation class handles all the API operations
 * required for Prefill feature
 */
public class PrefillServiceImpl implements PrefillService {

    private final HttpClient authClient;

    private final Config config;

    public PrefillServiceImpl(final Config config, final HttpClient authClient) {
        this.config = config;
        this.authClient = authClient;
    }

    /**
     * Save Prefill Data
     *
     * @return {@link PrefillResponse}
     * @throws PrefillException
     */
    public PrefillResponsePushResponse saveData(PrefillRequest prefillRequest, RequestHeader requestHeader) throws PrefillException {

        MultivaluedMap<String, Object> headers = PrefillUtil.buildHeaders(requestHeader,
                config);
        if (null == headers.get(AUTHORIZATION)) {
            throw new PrefillRequestValidationError(MANDATORY_REQUEST_PARAMETER_ERROR);
        }
        PrefillResponsePushResponse prefillResponsePushResponse = null;
        try {
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false).setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            HttpEntity requestEntity = new StringEntity(mapper.writeValueAsString(prefillRequest), CHARSET_UTF8);
            Map<String, String> responseHeaders = new HashMap<>();
            prefillResponsePushResponse = authClient.postClientResource(requestEntity,
                    config.getUrl().concat(PREFILL_POST_RESOURCE_PATH), headers,
                    new TypeReference<PrefillResponsePushResponse>() {
                    }, responseHeaders);
        } catch (PrefillResourceNotFoundError ex) {
            throw new PrefillApiError(PREFILL_RESOURCE_NOT_FOUND);
        } catch (PrefillException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new PrefillException(INTERNAL_SDK_EXCEPTION, ex);
        }
        return prefillResponsePushResponse;
    }

    /**
     * Save Encrypted Prefill Dataxs
     *
     * @return {@link PrefillResponse}
     * @throws PrefillException
     */
    public PrefillResponsePushResponse saveEncryptedData(PrefillRequest prefillRequest, RequestHeader requestHeader) throws PrefillException {
        MultivaluedMap<String, Object> headers = PrefillUtil.buildHeaders(requestHeader,
                config);
        if (null == headers.get(AUTHORIZATION)) {
            throw new PrefillRequestValidationError(MANDATORY_REQUEST_PARAMETER_ERROR);
        }
        PrefillResponsePushResponse prefillResponsePushResponse = null;
        try {
            EncryptedPrefillRequest encryptedPrefillRequest = new EncryptedPrefillRequest();
            if (null != config.getJweConfig() && null != config.getJweConfig().getPublicKey()) {
                String encryptedPayload = PrefillUtil.encrypt(prefillRequest, config.getJweConfig().getPublicKey());
                encryptedPrefillRequest.setUserInfo(encryptedPayload);
            } else {
                throw new PrefillInvalidStateError(INVALID_JWE_CONFIG);
            }
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false).setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            HttpEntity requestEntity = new StringEntity(mapper.writeValueAsString(encryptedPrefillRequest), CHARSET_UTF8);
            Map<String, String> responseHeaders = new HashMap<>();
            prefillResponsePushResponse = authClient.postClientResource(requestEntity,
                    config.getUrl().concat(PREFILL_POST_RESOURCE_PATH), headers,
                    new TypeReference<PrefillResponsePushResponse>() {
                    }, responseHeaders);
        } catch (PrefillResourceNotFoundError ex) {
            throw new PrefillApiError(PREFILL_RESOURCE_NOT_FOUND);
        } catch (PrefillException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new PrefillException(INTERNAL_SDK_EXCEPTION, ex);
        }
        return prefillResponsePushResponse;
    }

}
