package com.wyb.mybatis.multidatasource.service;

import com.wyb.mybatis.multidatasource.dao.model.UserDo;

/**
 * @author Marcher丶
 */
public interface MasterSlaveService {

    void testMasterTransaction(UserDo userDo);

}
