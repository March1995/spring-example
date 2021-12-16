package com.wyb.mq.service.impl;

import com.wyb.mq.bo.MsgTxtBo;
import com.wyb.mq.entity.MessageContent;
import com.wyb.mq.entity.ProductInfo;
import com.wyb.mq.enumuration.MsgStatusEnum;
import com.wyb.mq.mapper.MsgContentMapper;
import com.wyb.mq.mapper.ProductInfoMapper;
import com.wyb.mq.rabbit.mq.exception.BizExp;
import com.wyb.mq.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private MsgContentMapper msgContentMapper;

    @Transactional
    @Override
    public boolean updateProductStore(MsgTxtBo msgTxtBo) {
        boolean updateFlag = true;
        try {
            //更新库存
            productInfoMapper.updateProductStoreById(msgTxtBo.getProductNo());
            log.info("修改库存成功，orderNo {}，productId {}", msgTxtBo.getOrderNo(), msgTxtBo.getProductNo());

            //更新消息表状态
            MessageContent messageContent = new MessageContent();
            messageContent.setMsgId(msgTxtBo.getMsgId());
            messageContent.setUpdateTime(new Date());
            messageContent.setMsgStatus(MsgStatusEnum.CONSUMER_SUCCESS.getCode());
            msgContentMapper.updateMsgStatus(messageContent);
            log.info("修改消息表状态为消费成功，msgId {}", msgTxtBo.getMsgId());

            System.out.println(1/0);
        } catch (Exception e) {
//            log.error("更新数据库失败:{}", e);
            throw new BizExp(0,"更新数据库异常");
        }
        return updateFlag;
    }

    @Override
    public ProductInfo getById(Integer productId) {
        return productInfoMapper.getById(productId);
    }
}
