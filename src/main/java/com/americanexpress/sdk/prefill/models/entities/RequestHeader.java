package com.americanexpress.sdk.prefill.models.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(description = "Request Header model")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RequestHeader implements Serializable {

    private String resourceId;

    private String requestId;

    private String dataProvider;

    private String clientId;

    private ConsentInfo consentInfo;

    private String redirectUri;

    private String messageTypeId;

    private String countryCode;

    private String dataProviderAuthToken;
}