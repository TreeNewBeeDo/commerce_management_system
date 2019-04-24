package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @ProjectName: demo
 * @Package: com.example.demo.utils
 * @ClassName: ${TYPE_NAME}
 * @Description: java类作用描述
 * @Author: yueChunPeng
 * @CreateDate: 2018-12-31 13:14
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2018-12-31 13:14
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */

public class FileUtil {

    //绝对根路径，从yml文件里获取
//    @Value("${filePath}")
    private static final String base = "./target/classes/static/update";
    private static final String base0 = "./src/main/resources/static/update";
    //相对路径
    private static final String portrait = "/portrait/";
    private static final String product = "/product/";

    /**
     * 保存文件
     * @param file
     * @return
     */
    public static String saveFile(MultipartFile file,String type) throws IOException {
        if ("".equals(file.getOriginalFilename())) {
            return null;   //如果没有上传文件返回null
        }
        String saveFileName = UUIDUtil.getUUID() +
                file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        System.out.println("生成的文件名"+saveFileName);

        String filePath = base;
        String filepath = base0;
        switch (type){
            case "portrait":
                filePath += portrait;
                filepath += portrait;
                break;
            case "product":
                filePath += product;
                filepath += product;
                break;
                default:break;
        }
        System.out.println(filePath);
        System.out.println(filepath);
        System.out.println("开始创建文件夹");
        File tempFilePath = new File(filePath);   //创建临时文件夹
        if (!tempFilePath.exists()) {
            tempFilePath.mkdirs();  //不存在文件夹就创建该文件夹
            System.out.println("创建文件夹成功");
        }else {
            System.out.println("文件夹已经存在！");
        }
        System.out.println("开始创建文件夹");
        File tempFilePath0 = new File(filepath);   //创建临时文件夹
        if (!tempFilePath0.exists()) {
            tempFilePath0.mkdirs();  //不存在文件夹就创建该文件夹
            System.out.println("创建文件夹成功");
        }else {
            System.out.println("文件夹已经存在！");
        }
        //将路径与文件名拼装
        filePath+=saveFileName;
        filepath+=saveFileName;

        write(file.getInputStream(), new FileOutputStream(filePath));  //将文件写入到磁盘中z
        write(file.getInputStream(), new FileOutputStream(filepath));  //将文件写入到磁盘中z
        return saveFileName;
    }

    /**
     * 得到去掉后缀的文件名称
     * @param fileName 文件名
     * @return 返回去掉后缀的文件名
     */
    public static String getRidSuffixName(String fileName){
        if (fileName ==null){
            return null;
        }
        return fileName.substring(0,fileName.lastIndexOf("."));
    }

    /**
     * 通过文件名得到文件后缀名，即文件类型
     * @param fileName 文件名
     * @return 返回文件后缀
     */
    public static String getSuffix(String fileName){
        //文件为空返回null
        if(fileName == null){
            return null;
        }
        return fileName.substring(fileName.lastIndexOf("."),fileName.length());
    }

    /**
     * 向磁盘中入数据
     *
     * @param in  输入流
     * @param out 输出流
     * @throws IOException 抛出IOException异常
     */
    private static void write(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();   //刷新此输出流并强制写出所有缓冲的输出字节。
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ignored) {
            }
        }
    }

    private static String getPortraitPath(){
        String filePath = base;
        return filePath + portrait;
    }
    private static String getProductPath(){
        String filePath = base;
        return filePath + product;
    }
}
