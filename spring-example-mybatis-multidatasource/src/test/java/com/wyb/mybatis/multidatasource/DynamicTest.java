package com.wyb.mybatis.multidatasource;

import com.wyb.mybatis.multidatasource.dao.model.UserDo;
import com.wyb.mybatis.multidatasource.service.DynamicService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Marcher丶
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicTest {
    @Resource
    private DynamicService dynamicService;

    @Test
    public void testSwitch() {
        UserDo user = new UserDo();
        user.setUsername("move ");
        user.setPassword("12345611");
        user.setSex(1);
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        dynamicService.masterAddUser(user);

        List<UserDo> list1 = dynamicService.getListBySlave();
        for (int i = 0; i < list1.size(); i++) {
            if (i == list1.size() -1) {
                System.out.println(list1.get(i).getUsername());
            }
        }
//        list1.stream().filter(v -> v.get)forEach(userDo -> System.out.println(userDo.getUsername()));

//        List<UserDo> list2 = dynamicService.getListByMaster();
//
//        for (int i = 0; i < list2.size(); i++) {
//            if (i == list2.size() -1) {
//                System.out.println(list2.get(i).getUsername());
//            }
//        }
//        list2.forEach(userDo -> System.out.println(userDo.getUsername()));

    }
}
