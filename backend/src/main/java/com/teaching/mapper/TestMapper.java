package com.teaching.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {

    Integer selectOne();
}
