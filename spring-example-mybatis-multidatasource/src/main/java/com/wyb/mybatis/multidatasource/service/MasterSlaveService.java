package com.wyb.mybatis.multidatasource.service;

import com.wyb.mybatis.multidatasource.dao.model.UserDo;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Marcher丶
 */
public interface MasterSlaveService {

    void testMasterTransaction(UserDo userDo);

    @Transactional
    void testUpdateMasterTransaction(UserDo userDo);
}
