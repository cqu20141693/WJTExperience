package com.wcc.excel.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * wcc 2022/8/23
 */
@Data
@Accessors(chain = true)
public class OtherInfo {
    private Date date;
    private String phone;
    private String marketName;

}
