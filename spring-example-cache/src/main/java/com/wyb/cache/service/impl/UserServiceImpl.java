package com.wyb.cache.service.impl;

import com.github.pagehelper.PageHelper;
import com.wyb.cache.dao.mapper.UserDoMapper;
import com.wyb.cache.dao.model.UserDo;
import com.wyb.cache.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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
    public UserDo getById(Integer id) {
        return userDoMapper.selectByPrimaryKey(id);
    }

    @Override
    @Cacheable(value = "user", key = "#ids")
    public List<UserDo> getByIds(List<Integer> ids) {
        Example example = new Example(UserDo.class);
        example.createCriteria().andIn("id", ids);
        return userDoMapper.selectByExample(example);
    }

    @Override
    public UserDo updateUserNameById(UserDo userDo) {
        userDoMapper.updateByPrimaryKeySelective(userDo);
        return userDoMapper.selectByPrimaryKey(userDo.getId());
    }

    @Override
    public int updateAgeById(int age, Integer id) {
        UserDo userDo = UserDo.builder().age(age).id(id).build();
        return userDoMapper.updateByPrimaryKeySelective(userDo);
    }

    @Override
    public Boolean removeUserById(UserDo userDo) {
        return userDoMapper.deleteByPrimaryKey(userDo) > 0;
    }
}
