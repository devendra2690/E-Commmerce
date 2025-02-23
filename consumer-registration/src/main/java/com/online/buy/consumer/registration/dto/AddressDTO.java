package com.online.buy.consumer.registration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes null fields from JSON output
public class AddressDTO {

    private Long addressId; // Optional field

    // Getters and Setters
    @JsonProperty("street1")
    @NotNull(message = "Street1 is required")
    private String street1;

    @JsonProperty("street2")
    private String street2; // Optional field

    @JsonProperty("landmark")
    private String landmark; // Optional field

    @JsonProperty("city")
    @NotNull(message = "City is required")
    private String city;

    @JsonProperty("postal_code")
    @NotNull(message = "Postal code is required")
    private String postalCode;

    @JsonProperty("state")
    @NotNull(message = "State is required")
    private String state;

    @JsonProperty("country")
    @NotNull(message = "Country is required")
    private String country;

}
