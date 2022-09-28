package com.wyb.jpa.repository;

import com.wyb.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Marcher丶
 * @since 2021-06-07
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, Serializable {

    List<User> findByUsername(String username);

    List<User> findByUsernameLike(String username);

}
