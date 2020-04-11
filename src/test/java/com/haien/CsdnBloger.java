package com.haien;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author haien
 * @Description 模拟CSDN发博文，失败
 * @Date 2019/5/1
 **/
public class CsdnBloger {

    /**
     * @Author haien
     * @Description 测试
     * @Date 2019/5/1
     * @Param [args]
     * @return void
     **/
    public static void main(String[] args) throws Exception{
        Map<String, String> cookies = login("13670319954", "jiachun844511");
        String title = "测试";
        String content = "- test-content";
        String tags = "backstage";
        publishBlog(cookies, title, content, tags);
    }

    /**
     * @Author haien
     * @Description 发文之前先登录
     * @Date 2019/5/1
     * @Param [userName, password]
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    public static Map<String, String> login(String userName, String password) throws Exception{
        Map<String, String> map = new HashMap<>();
        map.put("u", userName);
        map.put("p", password);
        map.put("t", "log");
        map.put("remember", "0");
        map.put("f", "http%3A%2F%2Fwww.csdn.net%2F");
        map.put("rand", "0.4835865827484527");

        Connection conn = Jsoup.connect("https://passport.csdn.net/ajax/accounthandler.ashx");
        conn.header("Accept", "*/*");
        conn.header("Accept-Encoding", "gzip, deflate");
        conn.header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        conn.header("Host", "passport.csdn.net");
        conn.header("Referer", "https://passport.csdn.net/account/login");
        conn.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
        conn.header("X-Requested-With", "XMLHttpRequest");

        Response response = conn.ignoreContentType(true).method(Method.POST).data(map).execute();
        System.out.println("用户登录返回信息："+response.body());
        Map<String, String> cookies = response.cookies();
        System.out.println("*******************************************************cookies start:");
        cookies.keySet().stream().forEach((cookie) -> {
            System.out.println(cookie+":"+cookies.get(cookie));
        });
        System.out.println("*******************************************************cookies end:");
        return cookies;
    }

    /**
     * @Author haien
     * @Description 发文
     * @Date 2019/5/1
     * @Param [cookies, title, content, tags]
     * @return void
     **/
    public static void publishBlog(Map<String, String> cookies, String title, String content, String tags)
            throws Exception{
        String url = "http://write.blog.csdn.net/postedit?edit=1&isPub=1";

        //设置报头和数据
        Connection conn = Jsoup.connect(url)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3")
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Host", "write.blog.csdn.net")
                .header("Pragma", "no-cache")
                .header("Referer", "http://write.blog.csdn.net/postedit")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
                .header("X-Requested-With", "XMLHttpRequest")
                .data("tags", tags)
                .data("titl", title) //标题
                .data("typ", "1")
                .data("cont", content) //文章内容
                .data("desc", "")
                .data("flnm", "")
                .data("chnl", "0")
                .data("comm", "2")
                .data("level", "0")
                .data("tag2", "")
                .data("artid", "0")
                .data("stat", "publish")
                .ignoreContentType(true);

        //设置cookie
        for(String cookie : cookies.keySet()){
            conn.cookie(cookie, cookies.get(cookie));
        }

        String text = conn.post().text();
        System.out.println("conn.post().text()返回值："+text);
    }
}
