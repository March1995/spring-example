package com.wyb.test.java.reflect;

import java.io.*;

public class WybClassLoader extends ClassLoader {

    private String root;


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = loadClassData(name);
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            return defineClass(name, classData, 0, classData.length);

        }
    }

    private byte[] loadClassData(String className) {

        String fileName = root + File.separatorChar + className.replace('.', File.separatorChar) + ".class";
        try {
            InputStream ins = new FileInputStream(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            while ((length = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }


    public static void main(String[] args) {
        WybClassLoader wybClassLoader = new WybClassLoader();
        // class 需要用javac编译  class不能放在类路径下

        wybClassLoader.setRoot("E:\\marcher\\spring-example\\spring-example-test\\temp");
        try {
            Class clazz = wybClassLoader.loadClass("WybClass");
            Object obj = clazz.newInstance();
            System.out.println(obj.getClass().getClassLoader());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
