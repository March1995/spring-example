package com.wyb.jpa.service.impl;

import com.wyb.jpa.entity.User;
import com.wyb.jpa.repository.UserRepository;
import com.wyb.jpa.service.UserService;
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
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User selectById(Long id) {
        return userRepository.getOne(id);
    }
}
