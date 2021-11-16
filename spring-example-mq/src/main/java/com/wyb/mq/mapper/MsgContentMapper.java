package com.wyb.mq.mapper;


import com.wyb.mq.entity.MessageContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MsgContentMapper {

    /**
     * 方法实现说明:保存消息
     *
     * @param messageContent:消息对象
     */
    int saveMsgContent(MessageContent messageContent);

    int updateMsgStatus(MessageContent messageContent);

    List<MessageContent> qryNeedRetryMsg(@Param("msgStatus") Integer status, @Param("timeDiff") Integer timeDiff);

    void updateMsgRetryCount(String msgId);
}
