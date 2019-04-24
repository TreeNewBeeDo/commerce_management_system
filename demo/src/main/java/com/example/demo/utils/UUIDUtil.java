package com.example.demo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author : chpyue@foxmail.com
 * @date :2018/11/5 6:59
 * @Description: UUID 工具类
 */
public class UUIDUtil {
    /**
     * 获取一个UUID值
     * @return UUID值[String]
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 获取多个UUID值
     * @param number 所需个数
     * @return UUID集合
     */
    public static List<String> getUUID(Integer number){
        List<String> list = new ArrayList<>();
        while (0 <= (number--)){
            list.add(getUUID());
        }
        return list;
    }
    public static void main(String[] args) {
        System.out.println(getUUID());
    }
}
