package com.zsc.springboot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 疫情数据获取
 *
 *@Author：黄港团
 *@Since：2021/2/22 11:00
 */
public class EpidemicUtil {
    public static String getAllData() throws IOException {
        //1. 创建一个url对象。u1
        URL u1 = new URL("https://zaixianke.com/yq/all");
        //2、（建立连接），通过网址u1,打开网络连接，并得到连接对象conn
        URLConnection conn = u1.openConnection();
        //3、(获取数据传输的流),通过连接对象conn，获取输入流is
        InputStream is = conn.getInputStream();
        //4、将输入流is，装饰为一次能读取一行的  缓冲字符输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        //5、读取内容
        String allResult = br.readLine();
        //6、关闭流
        br.close();
        return allResult;
    }
}
