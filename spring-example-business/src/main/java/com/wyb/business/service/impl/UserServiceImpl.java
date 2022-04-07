package com.wyb.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.wyb.business.dao.mapper.UserDoMapper;
import com.wyb.business.dao.model.UserDo;
import com.wyb.business.service.UserService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Description:
 *
 * @author: Kunzite
 * @Date: 2018-01-07 19:23
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDoMapper userDoMapper;

    @Override
    public List<UserDo> listAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return userDoMapper.selectAll();
    }


    @Override
    public String updateById(Integer userId) {
        UserDo newUserDo = new UserDo();
        newUserDo.setId(userId);
        newUserDo.setUsername("test tx");
        try {
            int i = userDoMapper.updateByPrimaryKeySelective(newUserDo);
            newUserDo.setId(1);
            newUserDo.setUsername("11111");
            UserService aop = ((UserService) AopContext.currentProxy());
            this.listAll(1, 10);
            int q = userDoMapper.updateByPrimaryKeySelective(newUserDo);
            int a = 1 / 0;
            return i > 0 ? "success" : "fail";
        } catch (Exception e) {
            throw new RuntimeException("更新失败");
        }

    }


}
