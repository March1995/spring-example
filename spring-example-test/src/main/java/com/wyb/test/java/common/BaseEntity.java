package com.wyb.test.java.common;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Kunzite
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -1618357536992740779L;

    protected Long id;

    protected String name;

    protected Integer isDelete;

    protected Date createTime;

    protected Date updateTime;

    public BaseEntity(Long id) {
        this.id = id;
    }

    public BaseEntity(String name) {
        this.name = name;
    }

    public static BaseEntity parseConfig(String str) {
        return JSONObject.parseObject(str, BaseEntity.class);
    }



    public static void main(String[] args) {
        BaseEntity xiaoZhang = new BaseEntity("小张");
        BaseEntity xiaoLi = new BaseEntity("小李");
        swap(xiaoZhang, xiaoLi);
        System.out.println("xiaoZhang:" + xiaoZhang.getName());
        System.out.println("xiaoLi:" + xiaoLi.getName());
    }

    public static void swap(BaseEntity person1, BaseEntity person2) {
        BaseEntity temp = person1;
        person1 = person2;
        person2 = temp;
        System.out.println("person1:" + person1.getName());
        System.out.println("person2:" + person2.getName());
    }

}
