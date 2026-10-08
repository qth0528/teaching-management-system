package com.teaching.service;

import com.teaching.common.Result;

import java.util.Map;

public interface UserService {

    Result<Map<String, Object>> login(String username, String password);

    Result<Map<String, Object>> getCurrentUser();
}
