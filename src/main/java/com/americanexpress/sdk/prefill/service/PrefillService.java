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
import com.americanexpress.sdk.prefill.models.entities.RequestHeader;
import com.americanexpress.sdk.prefill.models.prefill.PrefillRequest;
import com.americanexpress.sdk.prefill.models.prefill.PrefillResponsePushResponse;
import com.americanexpress.sdk.prefill.service.impl.PrefillServiceImpl;

/**
 * 
 * The Prefill Service Interface handles all the API operations required for
 * Prefill service
 * 
 * @author jramio
 */
public interface PrefillService {
	/**
	 * Save prefill data to Prefill API
	 *
	 * @return {@link PrefillResponse}
	 * @throws PrefillException
	 */
	public PrefillResponsePushResponse saveData(PrefillRequest prefillRequest, RequestHeader requestHeader) throws PrefillException;


	/**
	 * Save encrypted prefill data to Prefill API
	 *
	 * @return {@link PrefillResponse}
	 * @throws PrefillException
	 */
	public PrefillResponsePushResponse saveEncryptedData(PrefillRequest prefillRequest, RequestHeader requestHeader) throws PrefillException;



	class Builder {
		public static PrefillService create(final Config config, final HttpClient authClient) {
			return new PrefillServiceImpl(config, authClient);
		}

		private Builder() {

		}
	}
}
