package com.americanexpress.sdk.prefill.models.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Consent Info model")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-07-04T07:05:21.367-07:00")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ConsentInfo {
    /** Consent Status */
    private String consentStatus;

    /** Timestamp when Consent is accepted */
    private String consentTimestamp;

    /** Consent grant Id is the unique Id for consent information */
    private String consentGrantId;

    /** Consent Description i.e electronic in our use case */
    private String consentDescription;

    /** The consent T&C url link */
    private String consentUrl;
}