
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
package com.americanexpress.sdk.prefill.functional.service;

import com.americanexpress.sdk.prefill.configuration.Config;
import com.americanexpress.sdk.prefill.service.AuthenticationService;
import com.americanexpress.sdk.prefill.service.PrefillService;
import com.americanexpress.sdk.prefill.service.impl.PrefillClientImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PrefillClientImplTest {

    private Config config = Config.builder().build();
    private PrefillClientImpl prefillClient = new PrefillClientImpl(config);

    @Test
    public void testSetAccessToken() {
        prefillClient.setAccessToken("accessToken");
        assertEquals("accessToken", config.getAccessToken());
    }

    @Test
    public void testGetAuthenticationService() {
        AuthenticationService authenticantionService = prefillClient.getAuthenticationService();
        assertNotNull(authenticantionService);
    }

    @Test
    public void testGetPrefillService() {
        PrefillService prefillservice = prefillClient.getPrefillService();
        assertNotNull(prefillservice);
    }

}
