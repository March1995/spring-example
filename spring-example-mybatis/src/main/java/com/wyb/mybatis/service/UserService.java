package com.wyb.mybatis.service;

import com.wyb.mybatis.dao.model.UserDo;

/**
 * @author: Marcher丶
 */
public interface UserService extends IService<UserDo> {

    String selectUserNameById(Integer id);

    void update(UserDo userDo);

    void updateAgePlusOne();
}
