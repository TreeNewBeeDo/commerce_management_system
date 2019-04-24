package com.example.demo.utils;



import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ProjectName: demo
 * @Package: com.example.demo.utils
 * @ClassName: ${TYPE_NAME}
 * @Description: java类作用描述
 * @Author: yueChunPeng
 * @CreateDate: 2019-01-06 18:47
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2019-01-06 18:47
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class DateToString {
    /**
     * 将日期类型转成格式化后的时间字符串
     * @param date 日期
     * @return 返回格式化后的时间字符串
     */
    public static String format(Date date){
        //传入的时间为空直接返回null
        if (date == null){
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);//返回格式化后的时间字符串
    }

    /**
     * 得到名字和工号字符串中的名字
     * @param str 名字和工号的字符串
     * @return 返回名字
     */
    public static String getName(String str){
        return str.split("\\(")[0];
    }

    public static String getId(String str){
        return str.split("\\(")[1].split("\\)")[0];
    }
}