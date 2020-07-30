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

import lombok.Getter;
import lombok.Setter;

/**
 * This Proxy configuration class holds the HTTP Proxy builder configuration
 * 
 * @author jramio
 */
@Getter
@Setter
public class ProxyConfig {

	/**
	 * is JWE Payload Encryption enabled
	 */
	private boolean isProxyEnabled;

	/**
	 * http/https
	 */
	private String protocol;

	/**
	 * Proxy Host
	 */
	private String host;

	/**
	 * Proxy Port
	 */
	private int port;


	public ProxyConfig(boolean isProxyEnabled, String protocol, String host, int port) {
		this.isProxyEnabled = isProxyEnabled;
		this.protocol = protocol;
		this.host = host;
		this.port = port;
	}

	public ProxyConfig(boolean isProxyEnabled) {
		this.isProxyEnabled = isProxyEnabled;
	}

}
