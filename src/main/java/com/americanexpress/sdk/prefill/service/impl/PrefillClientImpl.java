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

import com.americanexpress.sdk.prefill.client.http.ApiClientFactory;
import com.americanexpress.sdk.prefill.client.http.HttpClient;
import com.americanexpress.sdk.prefill.configuration.Config;
import com.americanexpress.sdk.prefill.service.PrefillClient;
import com.americanexpress.sdk.prefill.service.PrefillService;
import com.americanexpress.sdk.prefill.service.AuthenticationService;

/**
 *
 * The Prefill Client Implementation class is used to handle all Prefill service actions
 *
 */
public class PrefillClientImpl implements PrefillClient {

	/**
	 * Authentication service
	 */
	private AuthenticationService authenticationService;

	/**
	 * Prefill service
	 */
	private PrefillService prefillService;

	/**
	 * HTTP Client
	 */
	private HttpClient httpClient;

	/**
	 * configuration
	 */
	private Config config;

	/**
	 * constructor for Prefill client implementation
	 *
	 * @param config a {@link com.americanexpress.sdk.prefill.configuration.Config} object
	 */
	public PrefillClientImpl(Config config) {
		this.config = config;
		httpClient = ApiClientFactory.createHttpClient(config);
	}

	/** {@inheritDoc} */
	public void setAccessToken(String accessToken) {
		this.config.setAccessToken(accessToken);
	}

	/**
	 * create Authentication Service
	 *
	 * @return a {@link com.americanexpress.sdk.prefill.service.AuthenticationService} object
	 */
	public AuthenticationService getAuthenticationService() {

		if (authenticationService == null) {
			authenticationService = AuthenticationService.Builder.create(config, httpClient);
		}
		return authenticationService;
	}

	/**
	 * create Prefill Service
	 *
	 * @return a {@link com.americanexpress.sdk.prefill.service.PrefillService} object
	 */
	public PrefillService getPrefillService() {

		if (prefillService == null) {
			prefillService = PrefillService.Builder.create(config, httpClient);
		}
		return prefillService;
	}

}
