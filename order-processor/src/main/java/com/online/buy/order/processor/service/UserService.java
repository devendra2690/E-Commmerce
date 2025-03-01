package com.online.buy.order.processor.service;

import com.online.buy.common.code.entity.User;

public interface UserService {
   User findById(String userId);
}
