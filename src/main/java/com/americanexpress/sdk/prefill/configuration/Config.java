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
package com.americanexpress.sdk.prefill.configuration;

import lombok.Builder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import lombok.Getter;
import lombok.Setter;

/**
 * This configuration class holds the required builder configuration to startup
 * the Prefill SDK application
 * 
 * @author jramio
 */
@Getter
@Setter
@Builder
public class Config {

	/**
	 * URL for establishing connection
	 */
	private String url;

	/**
	 * OAuth Api Key
	 */
	private String apiKey;

	/**
	 * OAuth Api Secret
	 */
	private String apiSecret;

	/**
	 * AccessToken
	 */
	private String accessToken;

	/**
	 * (Optional if Server is taking care of TLS SSL Connection Socket Factory for
	 * enabling 2 way SSL Configuration
	 */
	private SSLConnectionSocketFactory socketFactory;

	/**
	 * Proxy Configuration (Optional, if needs to be tested through a corporate
	 * proxy
	 * 
	 */
	private ProxyConfig proxyConfig;

	/**
	 * PayLoad Encryption config (Optional, if Payload Encryption is required)
	 */
	private JWEConfig jweConfig;

}
