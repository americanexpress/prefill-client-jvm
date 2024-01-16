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

import com.americanexpress.sdk.prefill.models.prefill.ErrorMessage;

/**
 * The PrefillException class handles how exceptions are thrown
 */
public class PrefillException extends Exception {

	/**
	 * message describing the error
	 */
	private String userMessage;

	/**
	 * detailed error description for developers
	 */
	private String developerMessage;
	/**
	 * throwable cause
	 */
	Throwable cause;
	/**
	 * generic error
	 */
	private transient ErrorMessage error;

	/**
	 * <p>Constructor for PrefillException.</p>
	 *
	 * @param message a {@link java.lang.String} object
	 * @param developerMessage a {@link java.lang.String} object
	 */
	public PrefillException(String message, String developerMessage) {
		this.userMessage = message;
		this.developerMessage = developerMessage;
	}

	/**
	 * <p>Constructor for PrefillException.</p>
	 *
	 * @param message a {@link java.lang.String} object
	 */
	public PrefillException(String message) {
		this.userMessage = message;
	}

	/**
	 * <p>Constructor for PrefillException.</p>
	 *
	 * @param userMessage a {@link java.lang.String} object
	 * @param developerMessage a {@link java.lang.String} object
	 * @param cause a {@link java.lang.Throwable} object
	 */
	public PrefillException(String userMessage, String developerMessage, Throwable cause) {
		this.userMessage = userMessage;
		this.developerMessage = developerMessage;
		this.cause = cause;
	}

	/**
	 * <p>Constructor for PrefillException.</p>
	 *
	 * @param error a {@link com.americanexpress.sdk.prefill.models.prefill.ErrorMessage} object
	 * @param cause a {@link java.lang.Throwable} object
	 * @param message a {@link java.lang.String} object
	 */
	public PrefillException(String message, Throwable cause, ErrorMessage error) {
		this.userMessage = message;
		this.error = error;
		this.cause = cause;
	}

	/**
	 * <p>Constructor for PrefillException.</p>
	 *
	 * @param message a {@link java.lang.String} object
	 * @param cause a {@link java.lang.Throwable} object
	 */
	public PrefillException(String message, Throwable cause) {
		this.userMessage = message;
		this.cause = cause;
	}

	/**
	 * <p>Getter for the field <code>error</code>.</p>
	 *
	 * @return a {@link com.americanexpress.sdk.prefill.models.prefill.ErrorMessage} object
	 */
	public ErrorMessage getError() {
		return error;
	}

	/**
	 * <p>Setter for the field <code>error</code>.</p>
	 *
	 * @return a {@link com.americanexpress.sdk.prefill.models.prefill.ErrorMessage} object
	 */
	public ErrorMessage setError() {
		return error;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized Throwable getCause() {
		return cause;
	}

	/**
	 * <p>Setter for the field <code>cause</code>.</p>
	 *
	 * @return a {@link java.lang.Throwable} object
	 */
	public Throwable setCause() {
		return cause;
	}

	/**
	 * <p>Getter for the field <code>userMessage</code>.</p>
	 *
	 * @return a {@link java.lang.String} object
	 */
	public String getUserMessage() {
		return userMessage;
	}

	/**
	 * <p>Setter for the field <code>userMessage</code>.</p>
	 *
	 * @param userMessage a {@link java.lang.String} object
	 */
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
}
