package com.teaching.service.impl;

import com.teaching.common.ErrorCode;
import com.teaching.common.JwtUtil;
import com.teaching.common.Result;
import com.teaching.common.UserContext;
import com.teaching.entity.User;
import com.teaching.mapper.UserMapper;
import com.teaching.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public Result<Map<String, Object>> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return Result.error(ErrorCode.UNAUTHORIZED.getCode(), "用户名或密码错误");
        }

        // 检查账号是否在锁定期内
        if (user.getStatus() != null && user.getStatus() == 0
                && user.getLockedUntil() != null
                && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            return Result.error(ErrorCode.FORBIDDEN.getCode(), "账号已被锁定，请30分钟后再试");
        }

        // 锁定期已过，自动解锁
        if (user.getStatus() != null && user.getStatus() == 0) {
            user.setStatus(1);
            user.setLoginFailCount(0);
            user.setLockedUntil(null);
        }

        // 验证密码
        if (!encoder.matches(password, user.getPassword())) {
            // 登录失败，更新失败次数
            int failCount = (user.getLoginFailCount() == null ? 0 : user.getLoginFailCount()) + 1;
            user.setLoginFailCount(failCount);
            if (failCount >= 5) {
                user.setStatus(0);
                user.setLockedUntil(LocalDateTime.now().plusMinutes(30));
            }
            userMapper.updateLoginFail(user);

            if (failCount >= 5) {
                return Result.error(ErrorCode.FORBIDDEN.getCode(), "账号已被锁定，请30分钟后再试");
            }
            return Result.error(ErrorCode.UNAUTHORIZED.getCode(), "用户名或密码错误");
        }

        // 登录成功
        userMapper.updateLoginSuccess(user);

        // 生成 token（admin 账号 teacherId 暂设为 null）
        String token = jwtUtil.generateToken(user.getId(), null);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> getCurrentUser() {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error(ErrorCode.UNAUTHORIZED.getCode(), "未登录");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("teacherId", UserContext.getCurrentTeacherId());
        return Result.success(data);
    }
}
