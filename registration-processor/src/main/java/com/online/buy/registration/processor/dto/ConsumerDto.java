package com.online.buy.registration.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerDto {

    @JsonProperty("id")
    private Long consumerId;

    @NotNull(message = "First name can not be null")
    @JsonProperty("first_name")
    private String firstName;

    @NotNull(message = "Last name can not be null")
    @JsonProperty("last_name")
    private String lastName;

    @Email
    @JsonProperty("email")
    private String email;

    @NotNull
    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("password")
    @NotNull(message = "Email cannot be null")
    private String password;

    @JsonProperty("address")
    @Size(min = 1, message = "At least one address is required")
    @Valid
    private List<AddressDTO> addressList;
}
