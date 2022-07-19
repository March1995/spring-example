package com.wyb.test.java.generic;

import com.wyb.test.java.common.BaseEntity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Kunzite
 */
public class GenericDo<T> {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static void main(String[] args) {

        GenericDo<BaseEntity> name = new GenericDo<BaseEntity>(){};
        Class clzz = (Class) ((ParameterizedType) name.getClass().getGenericSuperclass()).getActualTypeArguments()[0];


        System.out.println(clzz.getName());
    }

}
