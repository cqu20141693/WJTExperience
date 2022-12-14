package com.wcc.excel.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * wcc 2022/8/23
 */
@Data
@Accessors(chain = true)
public class VegetableInfo {
    private Integer id;
    private String name;
    private String unitPrice;
    private String tradePrice;
    private String placeOrigin;
    protected Date arrivalDate;

}
