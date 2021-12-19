package com.wyb.test.cninfo;

import lombok.Data;

/**
 * Description:
 * "orgId":"gssz0000001","category":"A股","code":"000001","pinyin":"payh","zwjc":"平安银行"}
 *
 * @author: Marcher丶
 * @Date: 2021-12-19 22:06
 **/
@Data
public class Stock {

    private String orgId;
    private String category;
    private String code;
    private String payh;
    private String zwjc;
}
