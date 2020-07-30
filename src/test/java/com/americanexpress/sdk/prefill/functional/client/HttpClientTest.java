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
package com.americanexpress.sdk.prefill.functional.client;


import com.americanexpress.sdk.prefill.client.core.utils.PrefillUtil;
import com.americanexpress.sdk.prefill.client.http.HttpClient;
import com.americanexpress.sdk.prefill.exception.PrefillApiError;
import com.americanexpress.sdk.prefill.exception.PrefillAuthenticationError;
import com.americanexpress.sdk.prefill.exception.PrefillRequestValidationError;
import com.americanexpress.sdk.prefill.exception.PrefillResourceNotFoundError;
import com.americanexpress.sdk.prefill.models.prefill.PrefillResponsePushResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PrefillUtil.class })
public class HttpClientTest {

	private CloseableHttpClient closeableHttpClient;
    private HttpClient httpClient;
    private HttpEntity httpEntity;
    private String apiUrl;
    private MultivaluedMap<String, Object> headers;
    private Map<String, String> responseHeaders;
    private CloseableHttpResponse response;
    private StatusLine statusLine;

	@Before
	public void setUp() throws Exception {
		closeableHttpClient = EasyMock.createNiceMock(CloseableHttpClient.class);
		httpClient = new HttpClient(closeableHttpClient);
		httpEntity = EasyMock.createNiceMock(HttpEntity.class);
		apiUrl = "apiUrl";
		headers = new MultivaluedHashMap<>();
		responseHeaders = new HashMap<>();
		response = EasyMock.createNiceMock(CloseableHttpResponse.class);
		statusLine = EasyMock.createNiceMock(StatusLine.class);

		EasyMock.expect(closeableHttpClient.execute(EasyMock.isA(HttpPost.class))).andReturn(response);
		EasyMock.replay(closeableHttpClient);

		EasyMock.expect(response.getEntity()).andReturn(httpEntity).anyTimes();
		EasyMock.expect(response.getStatusLine()).andReturn(statusLine).anyTimes();
		EasyMock.expect(response.getAllHeaders()).andReturn(new Header[0]);
		EasyMock.replay(response);

		PowerMock.mockStatic(PrefillUtil.class);
	}


	@Test
	public void testPostClientResource() throws Exception {
		EasyMock.expect(statusLine.getStatusCode()).andReturn(200);
		EasyMock.replay(statusLine);

		EasyMock.expect(PrefillUtil.generateResponse(EasyMock.anyObject(), EasyMock.isA(CloseableHttpResponse.class)))
				.andReturn(new PrefillResponsePushResponse());
		PowerMock.replay(PrefillUtil.class);

		PrefillResponsePushResponse result = httpClient.postClientResource(
				httpEntity, apiUrl, headers, new TypeReference<PrefillResponsePushResponse>() {}, responseHeaders);
		assertNotNull(result);
	}

	@Test (expected = PrefillResourceNotFoundError.class)
	public void testPostClientResource_ResourceNotFound() throws Exception{
		EasyMock.expect(statusLine.getStatusCode()).andReturn(404).anyTimes();
		EasyMock.replay(statusLine);

		EasyMock.expect(PrefillUtil.getResponseString(EasyMock.isA(HttpEntity.class)))
				.andReturn("response string");
		PowerMock.replay(PrefillUtil.class);

		httpClient.postClientResource(httpEntity, apiUrl, headers,
				new TypeReference<PrefillResponsePushResponse>() {}, responseHeaders);
	}

	@Test (expected = PrefillRequestValidationError.class)
	public void testPostClientResource_RequestValidationError() throws Exception{
		EasyMock.expect(statusLine.getStatusCode()).andReturn(400).anyTimes();
		EasyMock.replay(statusLine);

		EasyMock.expect(PrefillUtil.getResponseString(EasyMock.isA(HttpEntity.class)))
				.andReturn("response string");
		PowerMock.replay(PrefillUtil.class);

		httpClient.postClientResource(httpEntity, apiUrl, headers,
				new TypeReference<PrefillResponsePushResponse>() {}, responseHeaders);
	}

	@Test (expected = PrefillAuthenticationError.class)
	public void testPostClientResource_AuthenticationError() throws Exception{
		EasyMock.expect(statusLine.getStatusCode()).andReturn(401).anyTimes();
		EasyMock.replay(statusLine);

		EasyMock.expect(PrefillUtil.getResponseString(EasyMock.isA(HttpEntity.class)))
				.andReturn("response string");
		PowerMock.replay(PrefillUtil.class);

		httpClient.postClientResource(httpEntity, apiUrl, headers,
				new TypeReference<PrefillResponsePushResponse>() {}, responseHeaders);
	}

	@Test (expected = PrefillApiError.class)
	public void testPostClientResource_PrefillApiError() throws Exception{
		EasyMock.expect(statusLine.getStatusCode()).andReturn(500).anyTimes();
		EasyMock.replay(statusLine);

		EasyMock.expect(PrefillUtil.getResponseString(EasyMock.isA(HttpEntity.class)))
				.andReturn("response string");
		PowerMock.replay(PrefillUtil.class);

		httpClient.postClientResource(httpEntity, apiUrl, headers,
				new TypeReference<PrefillResponsePushResponse>() {}, responseHeaders);
	}

}
