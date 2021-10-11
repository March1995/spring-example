package com.wyb.mybatis.multidatasource.service.impl;

import com.wyb.mybatis.multidatasource.dao.mapper.master.MUserMapper;
import com.wyb.mybatis.multidatasource.dao.mapper.slave.SUserMapper;
import com.wyb.mybatis.multidatasource.dao.model.UserDo;
import com.wyb.mybatis.multidatasource.service.MasterSlaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Marcher丶
 */
@Slf4j
@Service()
public class MasterSlaveServiceImpl implements MasterSlaveService {

    @Resource
    MUserMapper mUserMapper;
    @Resource
    SUserMapper sUserMapper;

    // @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = {
    // java.lang.RuntimeException.class })
    @Transactional
    @Override
    public void testMasterTransaction(UserDo userDo) {
        int i = mUserMapper.insert(userDo);
        if (i > 0) {
            List<UserDo> users = mUserMapper.getAll();
            UserDo latestDo = users.get(users.size() -1);
            latestDo.setUsername("update username");
            testUpdateMasterTransaction(latestDo);
        }
        System.out.println("测试普通事务");
//        System.out.println(1 / 0);
    }

    @Transactional
    @Override
    public void testUpdateMasterTransaction(UserDo userDo) {
        mUserMapper.update(userDo);
    }

}
