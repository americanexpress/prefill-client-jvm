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
package com.americanexpress.sdk.prefill.service;

import com.americanexpress.sdk.prefill.configuration.Config;
import com.americanexpress.sdk.prefill.service.impl.PrefillClientImpl;

/**
 * The Prefill Client Interface handles all the API operations required
 * for creating the Prefill API Client
 */
public interface PrefillClient {

	/**
	 * Creates a new Instance of Authentication Service to help get the Access Token
	 * required to make API calls
	 *
	 * @return Instance of {@link com.americanexpress.sdk.prefill.service.AuthenticationService}
	 */
	public AuthenticationService getAuthenticationService();

	/**
	 * Creates a new Instance of Prefill Service to help save Prefill data
	 *
	 * @return Instance of {@link com.americanexpress.sdk.prefill.service.PrefillService}
	 */
	public PrefillService getPrefillService();


	/**
	 * updates AccessToken
	 *
	 * @param accessToken a {@link java.lang.String} object
	 */
	public void setAccessToken(String accessToken);

	class Builder {
		public static PrefillClient build(final Config config) {
			return new PrefillClientImpl(config);
		}
	}

}
