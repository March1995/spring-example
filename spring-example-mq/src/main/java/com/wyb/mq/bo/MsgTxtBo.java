package com.wyb.mq.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MsgTxtBo implements Serializable {

    private long orderNo;

    private int productNo;

    private String msgId;
}
