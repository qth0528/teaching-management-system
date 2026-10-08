package com.teaching.mapper;

import com.teaching.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User selectByUsername(@Param("username") String username);

    void updateLoginFail(User user);

    void updateLoginSuccess(User user);
}
