package com.wyb.mq.mapper;


import com.wyb.mq.entity.ProductInfo;

public interface ProductInfoMapper {

    int updateProductStoreById(Integer productId);

    ProductInfo getById(Integer productId);
}


