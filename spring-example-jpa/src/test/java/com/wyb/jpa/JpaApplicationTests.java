package com.wyb.jpa;

import com.wyb.jpa.entity.User;
import com.wyb.jpa.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaApplicationTests {

    @Resource
    UserRepository userRepository;

    @Test
    public void test4() {
        User user = userRepository.findById(1L).get();
        System.out.println("查询成功：" + user);

        user.setPassword("11111111");
        userRepository.save(user);
        System.out.println("更新成功：" + user);
    }

    @Test
    public void test3() {
        List<User> all = userRepository.findAll();
        System.out.println("查询成功：" + all);
    }

    @Test
    public void test2() {
        User user = new User();
        user.setId(3L);
        userRepository.delete(user);
        System.out.println("删除成功：");
    }

    @Test
    public void test1() {
        User user = new User().setUsername("tom").setPassword("1111").setSex(1).setAge(1).setPhone("121").setStatus(0);
        User save = userRepository.save(user);
        System.out.println("插入成功：" + save);
    }

}
