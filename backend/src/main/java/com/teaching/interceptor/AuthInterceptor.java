package com.teaching.interceptor;

import com.teaching.common.ErrorCode;
import com.teaching.common.JwtUtil;
import com.teaching.common.Result;
import com.teaching.common.UserContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeError(response, ErrorCode.UNAUTHORIZED);
            return false;
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtUtil.parseToken(token);
            // parseToken 内部已校验过期（parseClaimsJws 会抛出 ExpiredJwtException），
            // 此处二次校验作为防御性编程保留
            if (jwtUtil.isTokenExpired(claims)) {
                writeError(response, ErrorCode.UNAUTHORIZED, "token已过期");
                return false;
            }
            Long userId = jwtUtil.getUserId(claims);
            Long teacherId = jwtUtil.getTeacherId(claims);
            UserContext.set(userId, teacherId);
            return true;
        } catch (ExpiredJwtException e) {
            writeError(response, ErrorCode.UNAUTHORIZED, "token已过期");
            return false;
        } catch (JwtException e) {
            writeError(response, ErrorCode.UNAUTHORIZED, "token无效");
            return false;
        }
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) {
        UserContext.clear();
    }

    /**
     * 根据 ErrorCode 映射 HTTP 状态码并写回 JSON 错误响应
     */
    private void writeError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        writeError(response, errorCode, errorCode.getMessage());
    }

    private void writeError(HttpServletResponse response, ErrorCode errorCode, String message) throws IOException {
        // 优先根据 ErrorCode 映射 HTTP 状态码
        int httpStatus = resolveHttpStatus(errorCode);
        response.setStatus(httpStatus);
        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = Result.error(errorCode.getCode(), message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
        response.getWriter().flush();
    }

    /**
     * 将业务错误码映射为 HTTP 状态码
     */
    private int resolveHttpStatus(ErrorCode errorCode) {
        switch (errorCode) {
            case BAD_REQUEST:   return HttpStatus.BAD_REQUEST.value();
            case FORBIDDEN:     return HttpStatus.FORBIDDEN.value();
            case NOT_FOUND:     return HttpStatus.NOT_FOUND.value();
            case CONFLICT:      return HttpStatus.CONFLICT.value();
            default:            return HttpStatus.UNAUTHORIZED.value();
        }
    }
}
