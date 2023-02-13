package com.wee.oracle.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wee.oracle.domain.User;
import com.wee.oracle.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("oracle/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/getAllUser")
    public String getUsers() {
        List<User> allUser = userMapper.findAllUser();
        if (allUser != null) {
            return toJson(allUser);
        }
        return "error";
    }

    public static String toJson(Object obj) {
        return JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
    }

}
