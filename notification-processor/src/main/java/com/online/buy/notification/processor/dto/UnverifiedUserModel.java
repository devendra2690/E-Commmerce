package com.online.buy.notification.processor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UnverifiedUserModel {
    private String email;
    private String username;
}
