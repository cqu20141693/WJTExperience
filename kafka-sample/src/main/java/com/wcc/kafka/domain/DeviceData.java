package com.wcc.kafka.domain;

import com.wcc.kafka.domain.enums.DataType;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceData {


    private String id;

    private String deviceId;
    //属性标识
    private String property;
    // 属性名称
    private String propertyName;
    // 数据类型
    private String type;
    // 单位
    private String unit;
    // 数字类型值
    private Object numberValue;
    //结构体类型值
    private Object objectValue;
    // 时间类型值
    private Date timeValue;
    // 地理位置类型值
    private GeoPoint geoValue;
    // 字符串值
    private String stringValue;
    // 原始值
    private Object value;
    // 格式化值
    private Object formatValue;
    // 创建时间
    private Long createTime;
    // 上报时序时间
    private Long timestamp;
    //值状态
    private String state;

    public static DeviceData mockData(String deviceId, String property, DataType type) {
        DeviceData deviceData = new DeviceData();
        deviceData.setDeviceId(deviceId);
        deviceData.setProperty(property);
        deviceData.setType(type.name());
        deviceData.setValue(DataType.mockValue(type));
        return deviceData;

    }
}
