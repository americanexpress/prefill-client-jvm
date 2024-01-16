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

import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.americanexpress.sdk.prefill.client.core.utils.PrefillUtil;
import com.americanexpress.sdk.prefill.exception.*;
import com.americanexpress.sdk.prefill.service.constants.PrefillExceptionConstants;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * The HttpClient class implementation handles all the HTTP operations for PE
 * API clients
 */
public class HttpClient {

	/**
	 * Client interface to connect to external service
	 */
	private CloseableHttpClient client;

	/**
	 * <p>Constructor for HttpClient.</p>
	 *
	 * @param client a {@link org.apache.http.impl.client.CloseableHttpClient} object
	 */
	public HttpClient(CloseableHttpClient client) {
		this.client = client;
	}

	/**
	 * This method is used to connect to external service API to post resource to
	 * the client
	 *
	 * @param apiRequest a T object
	 * @param apiUrl a {@link java.lang.String} object
	 * @param headers a {@link javax.ws.rs.core.MultivaluedMap} object
	 * @param responseObject a {@link com.fasterxml.jackson.core.type.TypeReference} object
	 * @param responseHeaders a {@link java.util.Map} object
	 * @return  R An R object
	 * @throws com.americanexpress.sdk.prefill.exception.PrefillException PrefillException
	 * @param <R> a R class
	 * @param <T> a T class
	 */
	public <R, T> R postClientResource(T apiRequest, String apiUrl, MultivaluedMap<String, Object> headers,
			TypeReference<R> responseObject, Map<String, String> responseHeaders) throws PrefillException {
		R response = null;
		HttpPost request = new HttpPost(apiUrl);
		HttpEntity entity = (HttpEntity) apiRequest;
		request.setEntity(entity);
		addHeaders(request, headers);
		try (CloseableHttpResponse httpResponse = client.execute(request)) {
			if (null != httpResponse.getEntity() && (httpResponse.getStatusLine().getStatusCode() == Response.Status.OK
					.getStatusCode()
					|| httpResponse.getStatusLine().getStatusCode() == Response.Status.CREATED.getStatusCode())) {
				if (null != responseObject) {
					response = PrefillUtil.generateResponse(responseObject, httpResponse);
				}
				if (null != responseHeaders) {
					extractResponseHeaders(httpResponse, responseHeaders);
				}
				return response;
			} else {
				throw handleHttpStatusCodes(httpResponse);
			}
		} catch (PrefillException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new PrefillApiError(PrefillExceptionConstants.INTERNAL_API_EXCEPTION, ex);
		}
	}

	/**
	 * This method is to extract response based from the HTTP response
	 *
	 * @param httpResponse
	 * @param responseHeaders
	 */
	private void extractResponseHeaders(CloseableHttpResponse httpResponse, Map<String, String> responseHeaders) {
		Header[] headers = httpResponse.getAllHeaders();
		for (Header header : headers) {
			responseHeaders.put(header.getName(), header.getValue());
		}
	}

	/**
	 * This method adds required headers to the request
	 *
	 * @param request
	 * @param headers
	 */
	private void addHeaders(HttpRequest request, MultivaluedMap<String, Object> headers) {
		for (String str : headers.keySet()) {
			request.addHeader(str, String.valueOf(headers.getFirst(str)));
		}
	}

	/**
	 * This method provides proper exception handling
	 *
	 * @param httpResponse
	 */
	private PrefillException handleHttpStatusCodes(CloseableHttpResponse httpResponse) {
		String developerMessage = PrefillUtil.getResponseString(httpResponse.getEntity());
		Response.Status status = Response.Status.fromStatusCode(httpResponse.getStatusLine().getStatusCode());
		switch (status) {
		case NOT_FOUND:
			return new PrefillResourceNotFoundError();
		case BAD_REQUEST:
			return new PrefillRequestValidationError(developerMessage);
		case UNAUTHORIZED:
			return new PrefillAuthenticationError(developerMessage);
		default:
			return new PrefillApiError(developerMessage);
		}
	}
}
