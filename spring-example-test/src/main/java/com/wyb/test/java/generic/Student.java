package com.wyb.test.java.generic;

/**
 * @author Kunzite
 */
public class Student<T> extends Person<T> {

    private String className;

    private T description;

    public Student(String sex, String name, T des) {
        super(sex, name, des);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public T getDescription() {
        return description;
    }

    public void setDescription(T description) {
        this.description = description;
    }

}
