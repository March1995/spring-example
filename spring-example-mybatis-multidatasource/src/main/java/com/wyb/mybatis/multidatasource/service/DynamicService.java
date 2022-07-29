package com.wyb.mybatis.multidatasource.service;

import com.wyb.mybatis.multidatasource.dao.model.UserDo;

import java.util.List;

/**
 * @author Marcher丶
 */
public interface DynamicService {

    int masterAddUser(UserDo userDo);

    List<UserDo> getListByMaster();

    List<UserDo> getListBySlave();
}
