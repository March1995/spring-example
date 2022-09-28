package com.wyb.jpa.service;


import com.wyb.jpa.entity.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Marcher丶
 * @since 2021-06-07
 */
public interface UserService {

    User selectById(Long id);
}
