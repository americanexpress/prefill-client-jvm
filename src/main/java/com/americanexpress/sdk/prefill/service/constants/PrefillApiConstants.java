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
package com.americanexpress.sdk.prefill.service.constants;

import lombok.experimental.UtilityClass;

/**
 * This class holds the constants required for Prefill API calls
 *
 * @author jramio
 */
@UtilityClass
public class PrefillApiConstants {
	/**
	 * APIGEE related String
	 */
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";
	public static final String MAC_ID = "X-AMEX-API-KEY";

	/**
	 * Retrieve Auth Token API
	 */
	public static final String AUTH_TOKEN_GRANT_TYPE = "grant_type";
	public static final String AUTH_TOKEN_GRANT_TYPE_VALUE = "client_credentials";
	public static final String AUTH_TOKEN_API_PATH = "/apiplatform/v1/oauth/token_provisioning/bearer_tokens";
	public static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";

	/**
	 * API constants
	 */
	public static final String API_CLIENT_TYPE_HTTP = "http";
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

	/**
	 * Request,Response headers, and Header fields
	 */
	public static final String REQUEST_HEADER_CLIENT_ID = "client_id";
	public static final String REQUEST_HEADER_REQUEST_ID = "request_id";
	public static final String REQUEST_HEADER_MESSAGE_TYPE_ID = "message_type_id";
	public static final String REQUEST_HEADER_CONTENT_TYPE = "content-type";
	public static final String REQUEST_HEADER_RESOURCE_ID = "resource_id";
	public static final String REQUEST_HEADER_DATA_PROVIDER = "data_provider";
	public static final String REQUEST_HEADER_CONSENT_INFO = "consent_info";
	public static final String REQUEST_HEADER_REDIRECT_URI = "redirect_uri";
	public static final String REQUEST_HEADER_COUNTRY_CODE = "country_code";
	public static final String REQUEST_HEADER_DATA_PROVIDER_AUTH_TOKEN = "data_provider_auth_token";
	public static final String PREFILL_POST_RESOURCE_PATH = "/acquisition/digital/v1/applications_prefillinfo";

	/**
	 * Prefill Sample Configuration keys
	 */
	public static final String DEVELOPER_PORTAL_SDK = "developer.portal.sdk";
	public static final String KEYSTORE_JKS = "keystore.jks";
	public static final String OAUTH_KEYSTORE_TRUST_STREAM = "oauth.keystore.trust.stream";
	public static final String OAUTH_KEYSTORE_LOAD_TRUST_STREAM = "oauth.keystore.load.trust.stream";
	public static final String OAUTH_KEYSTORE_PASSPHRASE_PROPERTY = "oauth.keystore.passphrase.property";
	public static final String OAUTH_KEYSTORE_ALIAS_PROPERTY = "oauth.keystore.alias.property";
	public static final String OAUTH_PREFILL_API_ENDPOINT = "oauth.prefill.api.endpoint";
	public static final String OAUTH_API_KEY = "oauth.api.key";
	public static final String OAUTH_API_SECRET = "oauth.api.secret";
	public static final String PROXY_PROTOCOL = "proxy.protocol";
	public static final String PROXY_HOST = "proxy.host";
	public static final String PROXY_PORT = "proxy.port";
	public static final String PROXY_ENABLED = "proxy.enabled";
	public static final String CERTIFICATE_FACTORY_INSTANCE = "certificate.factory.instance";
	public static final String CERTIFICATE_FILE = "certificate.file";
}