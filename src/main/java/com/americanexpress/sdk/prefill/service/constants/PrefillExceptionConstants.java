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
 * This class holds the Constants of Prefill Service Exception descriptions
 * and details
 */
@UtilityClass
public class PrefillExceptionConstants {

	/** services error constants */
	public static final String REQUEST_VALIDATION_FAILED = "Request validation failed";
	/** Constant <code>MANDATORY_REQUEST_PARAMETER_ERROR="Mandatory request parameters missing"</code> */
	public static final String MANDATORY_REQUEST_PARAMETER_ERROR = "Mandatory request parameters missing";
	/** Constant <code>INVALID_JWE_CONFIG="Invalid JWE configuration"</code> */
	public static final String INVALID_JWE_CONFIG = "Invalid JWE configuration";
	/** Constant <code>PREFILL_RESOURCE_NOT_FOUND="Prefill resource not found"</code> */
	public static final String PREFILL_RESOURCE_NOT_FOUND = "Prefill resource not found";

	/** validation message constants */
	public static final String USER_INFO_NOT_NULL = "User Info data is not present";
	/** Constant <code>APPLICANT_INFORMATION_MISSING="No Applicant Data Present"</code> */
	public static final String APPLICANT_INFORMATION_MISSING = "No Applicant Data Present";

	/**
	 * Error Messages
	 */
	public static final String INTERNAL_SDK_EXCEPTION = "Internal SDK Exception";
	/** Constant <code>INTERNAL_API_EXCEPTION="Internal API Exception"</code> */
	public static final String INTERNAL_API_EXCEPTION = "Internal API Exception";

}
