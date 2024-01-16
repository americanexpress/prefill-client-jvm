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
package com.americanexpress.sdk.prefill.client.http;

import com.americanexpress.sdk.prefill.configuration.Config;
import com.americanexpress.sdk.prefill.service.constants.PrefillApiConstants;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.security.interfaces.RSAPublicKey;

/**
 * The ApiClientFactory class creates the client instance based on client type
 */
@UtilityClass
public class ApiClientFactory {

	/**
	 * This method will be used create HTTP client instance
	 *
	 * @param config a {@link com.americanexpress.sdk.prefill.configuration.Config} object
	 * @return HttpClient
	 */
	public static HttpClient createHttpClient(Config config) {

		HttpHost proxy = null;
		SSLConnectionSocketFactory socketFactory = null;

		if (null != config.getSocketFactory()) {
			socketFactory = config.getSocketFactory();
		}
		if (null != config.getProxyConfig() && config.getProxyConfig().isProxyEnabled()) {

			proxy = new HttpHost(config.getProxyConfig().getHost(), config.getProxyConfig().getPort(),
					StringUtils.isEmpty(config.getProxyConfig().getProtocol()) ? PrefillApiConstants.API_CLIENT_TYPE_HTTP
							: config.getProxyConfig().getProtocol());

		}

		CloseableHttpClient client = HttpClients.custom().setProxy(proxy).setSSLSocketFactory(socketFactory).build();

		return new HttpClient(client);
	}

	/**
	 * This method will be used create JWEHeader instance
	 *
	 * @param alg a {@link com.nimbusds.jose.JWEAlgorithm} object
	 * @param enc a {@link com.nimbusds.jose.EncryptionMethod} object
	 * @return JWEHeader
	 */
	public static JWEHeader createJWEHeader(JWEAlgorithm alg, EncryptionMethod enc) {
		return new JWEHeader(alg, enc);
	}

	/**
	 * This method will be used create JWEObject instance
	 *
	 * @param header a {@link com.nimbusds.jose.JWEHeader} object
	 * @param payload a {@link com.nimbusds.jose.Payload} object
	 * @return JWEObject
	 */
	public static JWEObject createJWEObject(JWEHeader header, Payload payload) {
		return new JWEObject(header, payload);
	}

	/**
	 * This method will be used create RSAEncrypter instance
	 *
	 * @param publicKey a {@link java.security.interfaces.RSAPublicKey} object
	 * @return RSAEncrypter
	 */
	public static RSAEncrypter createRSAEncrypter(RSAPublicKey publicKey) {
		return new RSAEncrypter(publicKey);
	}

}
