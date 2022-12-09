package com.wcc.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class FastjsonTest {

    @Test
    public void testConcurrentHashMap(){
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
        map.put("id","sdfa");
        map.put("_uid","sdfa");
        map.put("d-double",new BigDecimal("12.1213123"));
        map.put("f-bool",true);
        map.put("a-int",1);
        JSONArray array = new JSONArray();
        array.add("key1");
        array.add("key2");
        map.put("arr1", array);
        map.put("long",System.currentTimeMillis());
        JSONObject obj = new JSONObject();
        obj.put("a-int",1);
        JSONObject value = new JSONObject();
        value.put("double",12);
        value.put("str","ke");
        obj.put("objs", value);
        obj.put("arr1",array);
        map.put("obj",obj);
        String jsonString = JSONObject.toJSONString(map);
        ConcurrentHashMap object = JSONObject.parseObject(jsonString, map.getClass());
        System.out.println(object);

        // fastjson ref 解析错误
        String text="{\"a-int\":91,\"obj\":{\"$ref\":\"$.trigger0.obj\"},\"_uid\":\"1600744591392980992\",\"d-double\":2583234.5126367128}";
      //  ConcurrentHashMap concurrentHashMap = JSONObject.parseObject(text, map.getClass());
     //   System.out.println(concurrentHashMap);
        String txt="{\"f-bool\":false,\"deviceName\":\"上数测试\",\"deviceId\":\"1590179804503773184\",\"b-float\":72455.28,\"productName\":\"上数测试\",\"messageType\":\"REPORT_PROPERTY\",\"record\":true,\"property\":\"a\",\"alarmId\":\"1597102379976302593\",\"alarmLevel\":2,\"id\":\"27dbb402c20d65fe1094d1ff7ba7f5b0\",\"e-text\":\"OoW\",\"value\":17,\"arr1\":[\"good\",\"lucky\"],\"timestamp\":1670482225643,\"totalAlarms\":2,\"a\":17,\"e-long\":8634695920,\"productId\":\"1590179485069774848\",\"alarmName\":\"次数窗口\",\"trigger0\":{\"a\":17,\"e-long\":8634695920,\"obj\":{\"a\":91,\"b\":100076},\"f-bool\":false,\"e-text\":\"OoW\",\"b-float\":72455.28,\"arr1\":[\"good\",\"lucky\"],\"d-double\":2583234.5126367128,\"a-int\":91},\"a-int\":91,\"obj\":{\"a\":91,\"b\":100076},\"_uid\":\"1600744591392980992\",\"d-double\":2583234.5126367128}";
        ConcurrentHashMap map1 = JSONObject.parseObject(txt, map.getClass());
        System.out.println(map1);
    }
}
