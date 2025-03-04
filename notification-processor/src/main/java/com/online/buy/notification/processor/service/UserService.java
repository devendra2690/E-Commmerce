package com.online.buy.notification.processor.service;

import com.online.buy.notification.processor.dto.UnverifiedUserModel;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    public List<UnverifiedUserModel> fetchUnVerifiedList() {

        return jdbcTemplate.query(
                "SELECT email, username FROM users WHERE is_email_verified = false",
                (rs, rowNum) -> new UnverifiedUserModel(rs.getString("email"), rs.getString("username"))
        );
    }

    public List<UnverifiedUserModel> fetchNotReceivedUserConsentList() {

        return jdbcTemplate.query(
                "SELECT email, username FROM users WHERE is_consent_received = false and is_email_verified = true",
                (rs, rowNum) -> new UnverifiedUserModel(rs.getString("email"), rs.getString("username"))
        );
    }
}
