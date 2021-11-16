package com.wyb.mq.service;


import com.wyb.mq.bo.MsgTxtBo;
import com.wyb.mq.entity.ProductInfo;

public interface IProductService {

    boolean updateProductStore(MsgTxtBo msgTxtBo);

    ProductInfo getById(Integer productId);
}
