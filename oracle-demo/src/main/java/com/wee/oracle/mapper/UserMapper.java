package com.wee.oracle.mapper;

import com.wee.oracle.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> findAllUser();
}
