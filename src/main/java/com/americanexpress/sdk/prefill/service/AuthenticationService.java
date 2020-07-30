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

import com.americanexpress.sdk.prefill.client.http.HttpClient;
import com.americanexpress.sdk.prefill.configuration.Config;
import com.americanexpress.sdk.prefill.exception.PrefillException;
import com.americanexpress.sdk.prefill.models.entities.AccessTokenResponse;
import com.americanexpress.sdk.prefill.service.impl.AuthenticationServiceImpl;

/**
 * The AuthenticationService interface handles the OAuth Token Service
 * operations required for Prefill service
 * 
 * @author jramio
 */
public interface AuthenticationService {
	/**
	 * Get AccessToken and the list of API Products approved for the token
	 * 
	 * @return {@link AccessTokenResponse}
	 * @throws PrefillException
	 */
	public AccessTokenResponse getAccessToken() throws PrefillException;

	class Builder {
		public static AuthenticationService create(final Config config, final HttpClient authClient) {
			return new AuthenticationServiceImpl(config, authClient);
		}

		private Builder() {

		}
	}
}
