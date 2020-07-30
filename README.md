# Prefill Java SDK
This Java SDK implementation allows Amex partners to integrate seamlessly to the Amex Prefill Service
and reduces complexity out of the coding service layer integration to Prefill API. It assumes you have already set up your credentials with American Express and have your certs prepared. 

Amex Prefill Service provides the capability to pre-populate application submission data. The Amex partner 
pushes an applicant's information to the Prefill API prior to launching the card application. 
The Amex partner will receive a Prefill Token in return, which can be passed during the card application 
journey to redeem the applicant's data.
​

<br>

## Table of Contents
  
- [Installation](#installation)
- [Compatibility](#compatibility)
- [Configuration](#configuring-sdk)
- [Authentication](#authentication)
- [Saving Prefill Data](#saving-prefill-data)
- [Error Handling](#error-handling)
- [Samples](#running-samples)
- [Open Source Contribution](#contributing)
- [License](#license)
- [Code of Conduct](#code-of-conduct)


<br/>


## Installation

- Install maven 
    ```
    brew install maven
    ```
- Clone repo
- Go inside px-prefill-java-sdk  and type
   ```
    $ mvn clean install
   ```
<br/>

## Compatibility

Prefill SDK will support Java Version 8 or higher.



<br/>

## Configuring SDK

The SDK needs to be configured with OAuth, Mutual Auth and Payload encryption configurations. 
Please see the `createPrefillClient()` method in PrefillSample.java for a sample configuration snippet.

```java
private PrefillClient createPrefillClient() throws IOException, CertificateException,
        NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
    Map<String, String> sampleConfig = buildClientConfig();
    PrefillClient prefillClient = null;

    InputStream inputStream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(sampleConfig.get(DEVELOPER_PORTAL_SDK));
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(inputStream, sampleConfig.get(OAUTH_KEYSTORE_PASSPHRASE_PROPERTY).toCharArray());
    KeyStore trustStore = KeyStore.getInstance(sampleConfig.get(KEYSTORE_JKS));
    InputStream trustStream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(sampleConfig.get(OAUTH_KEYSTORE_TRUST_STREAM));
    trustStore.load(trustStream, sampleConfig.get(OAUTH_KEYSTORE_LOAD_TRUST_STREAM).toCharArray());

    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
            new SSLContextBuilder().loadTrustMaterial(trustStore, (chain, authType) -> false)
                    .loadKeyMaterial(ks, sampleConfig.get(OAUTH_KEYSTORE_PASSPHRASE_PROPERTY).toCharArray(),
                            (aliases, socket) -> sampleConfig.get(OAUTH_KEYSTORE_ALIAS_PROPERTY))
                    .build());

    CertificateFactory certificateFactory =
            CertificateFactory.getInstance(sampleConfig.get(CERTIFICATE_FACTORY_INSTANCE));
    Certificate certificate =
            certificateFactory.generateCertificate(new FileInputStream(sampleConfig.get(CERTIFICATE_FILE)));
    RSAPublicKey publicKey = (RSAPublicKey) certificate.getPublicKey();

    prefillClient = PrefillClient.Builder
            .build(Config.builder().url(sampleConfig.get(OAUTH_PREFILL_API_ENDPOINT))
                    .apiKey(sampleConfig.get(OAUTH_API_KEY))
                    .apiSecret(sampleConfig.get(OAUTH_API_SECRET))
                    .accessToken(null)
                    .socketFactory(socketFactory)
                    .jweConfig(new JWEConfig(true, publicKey))
                    .proxyConfig(new ProxyConfig(Boolean.parseBoolean(sampleConfig.get(PROXY_ENABLED)),
                            sampleConfig.get(PROXY_PROTOCOL), sampleConfig.get(PROXY_HOST),
                            Integer.valueOf(sampleConfig.get(PROXY_PORT))))
                    .build());

    return prefillClient;
}
```

<br/>

## Authentication

Amex Prefill API uses token based authentication. The following examples demonstrates how
 to generate bearer tokens using the SDK :

```java
AccessTokenResponse accessTokenResponse = getAuthenticationToken(prefillClient); //success response

prefillClient.setAccessToken(accessTokenResponse.getAccessToken()); //set the Access Token for further API calls 
```
Sample Response : 

```java
{
  scope: 'default',
  status: 'approved',
  expires_in: '3599', // token expiry in seconds, you can cache the token for the amount of time specified.
  token_type: 'BearerToken',
  access_token: 'wJeW9CPT0DbrqBjrTN1xbMQZkae2'
}
```
Note : you can skip this call if you have an active Token in your cache. if you have an
active token, you can just set the bearerToken in config under authentication
or call `setAccessToken('access_token')` method to update the config.



<br/>

## Saving Prefill Data

Below is a sample snippet to save Prefill data. To save unencrypted data, use the `saveData()` method.
If the data needs to be encrypted, please ensure that the encryption configurations are set up properly, 
and use the `saveEncryptedData()` method.
```java

PrefillClient prefillClient = createPrefillClient();

PrefillService prefillService = prefillClient.getPrefillService();

RequestHeader requestHeader = objectMapper.readValue(Thread.currentThread()
                                .getContextClassLoader().getResourceAsStream("prefillRequestHeader.json"),
                                RequestHeader.class);

PrefillRequest prefillRequest = objectMapper.readValue(Thread.currentThread()
                                .getContextClassLoader().getResourceAsStream("prefillRequest.json"),
                                PrefillRequest.class);

prefillResponse = prefillService.saveData(prefillRequest, requestHeader);

//see src/main/resources for a sample header and body

return prefillResponse; //successful response
```

A successful response will return a Prefill token, which can be used to access the applicant's
 during the Card Application Journey.
 
 Sample Success Response:
 
 ```java
{
    applicantRequestToken: eyJhbGciOiJSUzI1NiJ9.eyJjbGFpbXNfcmVxIjoidHJ1ZSIsImFjY2Vzc190eXBlIjoiUyIsInSva2VuX2lkIjoiYmM0MmJiOGItOTFjMi00ODc4LWFjN2YtYTE3MzgwZjZjYjc2IcwicmVzb3VyY2VfaWQiOiJiYzQyYmI4Yi05MWMyLTQ4NzgtYWM3Zi1hMTczODBmNmNiNzYiLCJleHAiOjE1ODk5OTk1ODcsInJlc291cmNlX3N0cmF0ZWd5Ijoic2luZ2xlX2FjY2Vzc18zNjAwIiwianRpIjoiZWI1OWJiN2YtOGMyOS00OGU2LTk3MDItMzFjOTM2MDI1OGZjIiwiY2xpZW50X2lkIjoiQ2xpZW50X0lEIn0.j5liCirYX3EqTc2BKAbQmHf54ski9_X13CJZyrJ4zN2h3-1ZAob_6YPG6ltqsnFrKDwKJvQVK0prTfMCW0XeNgedNl_D4zuyzTt_UK5CPv-V_gR6PqFi9buIUeHE44hcr0n6WqsMSRk4ysguiRoKCP5Q_YredEewwjz5NHoejKlFdH7hijhHjoUBbS30jzPXVJ7wt1DK21qGK_3sSaCDLPveSjDAdqkP26N-eB-3Xvcn3aeGcwX6lAlxe8GSQZD1lEbQmB3dRiXeRrW8xil5ZFS6ssuAD-x0IkGLzT07zI7EWr6tH7B4bnX8aKjJsAE-ZG4EbTl0tRW34F9Toxygng
    applicantRequestTokenExpiresIn: 3600000
    redirectUrl: null
}
 ```


<br/>

## Error Handling

In case of exceptions encountered while calling Prefill API, the SDK will throw Errors.
 

Possible exceptions : 

- `PrefillAPIError`: a generic type of error, it will be raised when there is an internal server
 error or any other error which is not covered by any of the named errors.

- `PrefillAuthenticationError`: invalid API Key or Secret is sent to the API

- `PrefillInvalidStateError`: JWE configuration is not valid

- `PrefillRequestValidationError`: request or configs provided to the SDK are invalid

- `PrefillResourceNotFoundError`: Prefill resource cannot be found

- `PayloadEncryptionError`: a generic type of error for Encryption errors, it will be raised when 
there is an Exception raised at the Payload encryption. More information will be present in the error message.


<br/>

## Running Samples 
See PrefillSample.java for sample usages of Prefill SDK.
The "sample.properties" resource file contains the variables needed to run the sample.
Replace the example values with actual values before running the sample.

```java
developer.portal.sdk = jks_file_example.jks             // SDK keystore
keystore.jks = jks                                      // Java keystore format type
oauth.keystore.trust.stream = trust_file_example.jks    // Path to trust store file
oauth.keystore.load.trust.stream = trust_user_example   // Keystore username
oauth.keystore.passphrase.property = password_example   // Keystore password
oauth.keystore.alias.property = alias_example           // Alias (or name) under which the key is stored in the keystore
oauth.prefill.api.endpoint = https://example.americanexpress.com    // Prefill API endpoint
oauth.api.key = auth_key_example                        // OAuth Client ID/Key
oauth.api.secret = auth_key_secret_example              // OAuth Secret
proxy.protocol = http                                   // Protocol Client uses to connect to proxy/load balancer
proxy.enabled = true                                    // Enabled or disabled proxy
proxy.host = proxy.example.com                          // Proxy host
proxy.port = 8080                                       // Proxy port
certificate.factory.instance = X.509                    // Certificate factory instance
certificate.file = certificate_file_example.cer         // Path to certificate file
```

<br/>

## Contributing

We welcome Your interest in the American Express Open Source Community on Github. Any Contributor to
any Open Source Project managed by the American Express Open Source Community must accept and sign
an Agreement indicating agreement to the terms below. Except for the rights granted in this 
Agreement to American Express and to recipients of software distributed by American Express, You
reserve all right, title, and interest, if any, in and to Your Contributions. Please
[fill out the Agreement](https://cla-assistant.io/americanexpress/prefill-client-jvm).
## License

Any contributions made under this project will be governed by the
[Apache License 2.0](./LICENSE.txt).
## Code of Conduct

This project adheres to the [American Express Community Guidelines](./CODE_OF_CONDUCT.md). By
participating, you are expected to honor these guidelines.