package com.wyb.mybatis.plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyb.mybatis.plus.entity.User;
import com.wyb.mybatis.plus.mapper.UserMapper;
import com.wyb.mybatis.plus.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Marcher丶
 * @since 2021-06-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User selectById(Integer id) {
        return getById(id);
    }
}
