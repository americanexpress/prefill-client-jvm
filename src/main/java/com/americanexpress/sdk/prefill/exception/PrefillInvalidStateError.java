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
package com.americanexpress.sdk.prefill.exception;

/**
 * The PrefillInvalidStateError class raises an error when the JWE configuration is not valid
 *
 */
public class PrefillInvalidStateError extends PrefillException {

	private static final String USER_MESSAGE = "JWE configuration is invalid";

	public PrefillInvalidStateError(String developerMessage) { super(USER_MESSAGE, developerMessage); }
}
