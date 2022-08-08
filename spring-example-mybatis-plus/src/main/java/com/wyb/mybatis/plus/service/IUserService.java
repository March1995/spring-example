package com.wyb.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyb.mybatis.plus.entity.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Marcher丶
 * @since 2021-06-07
 */
public interface IUserService extends IService<User> {

    User selectById(Integer id);
}
