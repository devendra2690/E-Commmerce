package com.online.buy.product.processor.config;


import com.online.buy.security_api.adapter.SecurityConfigAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomSecurityConfiguration extends SecurityConfigAdapter {

    @Override
    public List<RequestMatcher> customPublicRequestMatchers() {
        List<RequestMatcher> list = new ArrayList<>();
        // Add paths that should not be covered by the Spring security
        // Example: list.add(new AntPathRequestMatcher("/login"));
        list.add(new AntPathRequestMatcher("/client/**"));
        list.add(new AntPathRequestMatcher("/product/**"));
        return list;
    }

}
