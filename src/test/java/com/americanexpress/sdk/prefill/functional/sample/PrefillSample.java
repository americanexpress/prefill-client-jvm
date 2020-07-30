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
package com.americanexpress.sdk.prefill.functional.sample;

import com.americanexpress.sdk.prefill.configuration.Config;
import com.americanexpress.sdk.prefill.configuration.JWEConfig;
import com.americanexpress.sdk.prefill.configuration.ProxyConfig;
import com.americanexpress.sdk.prefill.exception.PrefillException;
import com.americanexpress.sdk.prefill.models.entities.AccessTokenResponse;
import com.americanexpress.sdk.prefill.models.entities.RequestHeader;
import com.americanexpress.sdk.prefill.models.prefill.PrefillRequest;
import com.americanexpress.sdk.prefill.models.prefill.PrefillResponsePushResponse;
import com.americanexpress.sdk.prefill.service.AuthenticationService;
import com.americanexpress.sdk.prefill.service.PrefillClient;
import com.americanexpress.sdk.prefill.service.PrefillService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.americanexpress.sdk.prefill.service.constants.PrefillApiConstants.*;
import static org.junit.Assert.assertNotNull;

public class PrefillSample {

    /**
     * Sample for saving Prefill data
     *
     * @throws PrefillException
     */
    @Test
    public void saveDataSample() {
        PrefillResponsePushResponse prefillResponse = null;
        try {
            PrefillClient prefillClient = createPrefillClient();
            /**
             * if a valid access Token is already available in the cache, please build the
             * configuration with the available token. getting Authentication Token call is
             * not needed.
             */
            AccessTokenResponse accessTokenResponse = getAuthenticationToken(prefillClient);

            if (null != accessTokenResponse && StringUtils.isNotEmpty(accessTokenResponse.getAccessToken())) {
                prefillClient.setAccessToken(accessTokenResponse.getAccessToken());

                System.out.println("AccessToken: " + accessTokenResponse.getAccessToken());

                PrefillService prefillService = prefillClient.getPrefillService();

                ObjectMapper objectMapper = new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                RequestHeader requestHeader = objectMapper.readValue(Thread.currentThread()
                                .getContextClassLoader().getResourceAsStream("prefillRequestHeader.json"),
                        RequestHeader.class);

                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
                PrefillRequest prefillRequest = objectMapper.readValue(Thread.currentThread()
                                .getContextClassLoader().getResourceAsStream("prefillRequest.json"),
                        PrefillRequest.class);

                prefillResponse = prefillService.saveData(prefillRequest, requestHeader);
                assertNotNull(prefillResponse);

                System.out.println("Prefill Response: " + prefillResponse.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sample for saving encrypted Prefill data
     *
     * @throws PrefillException
     */
    @Test
    public void saveEncryptedDataSample() {
        PrefillResponsePushResponse prefillResponsePushResponse = null;
        try {
            PrefillClient prefillClient = createPrefillClient();
            /**
             * if a valid access Token is already available in the cache, please build the
             * configuration with the available token. getting Authentication Token call is
             * not needed.
             */
            AccessTokenResponse accessTokenResponse = getAuthenticationToken(prefillClient);

            if (null != accessTokenResponse && StringUtils.isNotEmpty(accessTokenResponse.getAccessToken())) {
                prefillClient.setAccessToken(accessTokenResponse.getAccessToken());

                System.out.println("AccessToken: " + accessTokenResponse.getAccessToken());

                PrefillService prefillService = prefillClient.getPrefillService();

                ObjectMapper objectMapper = new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                RequestHeader requestHeader = objectMapper.readValue(Thread.currentThread()
                                .getContextClassLoader().getResourceAsStream("encryptedPrefillRequestHeader.json"),
                        RequestHeader.class);

                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
                PrefillRequest prefillRequest = objectMapper.readValue(Thread.currentThread()
                                .getContextClassLoader().getResourceAsStream("prefillRequest.json"),
                        PrefillRequest.class);


                prefillResponsePushResponse = prefillService.saveEncryptedData(prefillRequest, requestHeader);
                assertNotNull(prefillResponsePushResponse);

                System.out.println("Prefill Response: " + prefillResponsePushResponse.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the HTTP Client instance to get AAM details bdaas API
     *
     * @throws IOException
     */
    private Map<String, String> buildClientConfig() throws IOException {
        Properties prefillSampleConfiguration = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sample.properties");
        prefillSampleConfiguration.load(inputStream);
        return new HashMap(prefillSampleConfiguration);
    }

    /**
     * Sample Client Builder
     *
     * @return PrefillClient
     * @throws PrefillException
     * @throws IOException
     */
    private PrefillClient createPrefillClient() throws IOException, CertificateException,
            NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        Map<String, String> sampleConfig = buildClientConfig();
        PrefillClient prefillClient = null;

        InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(sampleConfig.get(DEVELOPER_PORTAL_SDK));
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(inputStream, sampleConfig.get(OAUTH_KEYSTORE_PASSPHRASE_PROPERTY).toCharArray());
        KeyStore trustStore = KeyStore.getInstance(sampleConfig.get(KEYSTORE_JKS));
        InputStream trustStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(sampleConfig.get(OAUTH_KEYSTORE_TRUST_STREAM));
        trustStore.load(trustStream, sampleConfig.get(OAUTH_KEYSTORE_LOAD_TRUST_STREAM).toCharArray());

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                new SSLContextBuilder().loadTrustMaterial(trustStore, (chain, authType) -> false)
                        .loadKeyMaterial(ks, sampleConfig.get(OAUTH_KEYSTORE_PASSPHRASE_PROPERTY).toCharArray(),
                                (aliases, socket) -> sampleConfig.get(OAUTH_KEYSTORE_ALIAS_PROPERTY))
                        .build());

        CertificateFactory certificateFactory =
                CertificateFactory.getInstance(sampleConfig.get(CERTIFICATE_FACTORY_INSTANCE));
        Certificate certificate =
                certificateFactory.generateCertificate(new FileInputStream(sampleConfig.get(CERTIFICATE_FILE)));
        RSAPublicKey publicKey = (RSAPublicKey) certificate.getPublicKey();

        prefillClient = PrefillClient.Builder
                .build(Config.builder().url(sampleConfig.get(OAUTH_PREFILL_API_ENDPOINT))
                        .apiKey(sampleConfig.get(OAUTH_API_KEY))
                        .apiSecret(sampleConfig.get(OAUTH_API_SECRET))
                        .accessToken(null)
                        .socketFactory(socketFactory)
                        .jweConfig(new JWEConfig(true, publicKey))
                        .proxyConfig(new ProxyConfig(Boolean.parseBoolean(sampleConfig.get(PROXY_ENABLED)),
                                sampleConfig.get(PROXY_PROTOCOL), sampleConfig.get(PROXY_HOST),
                                Integer.valueOf(sampleConfig.get(PROXY_PORT))))
                        .build());

        return prefillClient;
    }

    /**
     * This method will get the access Token from the SDK.
     *
     * @param prefillClient
     * @return AccessTokenResponse
     * @throws PrefillException
     */
    private AccessTokenResponse getAuthenticationToken(PrefillClient prefillClient)
            throws PrefillException {
        AccessTokenResponse accessTokenResponse = null;
        AuthenticationService authenticationService = prefillClient.getAuthenticationService();
        accessTokenResponse = authenticationService.getAccessToken();
        return accessTokenResponse;
    }
}
