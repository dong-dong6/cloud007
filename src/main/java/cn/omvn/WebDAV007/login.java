package cn.omvn.pdfdownloader.controller.WebDAV007;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;

import java.net.HttpCookie;
import java.util.Scanner;

/**
 * 2024-03-02
 * 栋dong
 */
public class login {
    public static HttpCookie cloud_cookie;

    public static  HttpCookie getCloud_cookie() {
        if (cloud_cookie == null) {
            getCookie();
            return cloud_cookie;
        } else {
            return cloud_cookie;
        }
    }

    public static void getCookie() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入账号");
        String username = scanner.next();
        System.out.println("输入密码");
        String password = scanner.next();
        String url = "https://wp.007irs.com/api/v3/site/captcha";
        HttpCookie cookie = HttpRequest.get(url)
                .execute().getCookie("cloudreve-session");

        while (true) {
            String body = HttpRequest.get("https://wp.007irs.com/api/v3/site/captcha")
                    .execute().body();
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(body);
            String data = jsonObject.getString("data");
            System.out.println(data);
            String captcha = scanner.next();
            String loginUrl = new String("https://wp.007irs.com/api/v3/user/session");
            JSONObject json = new JSONObject();
            json.set("userName", username);
            json.set("Password", password);
            json.set("captchaCode", captcha);
            String login = HttpRequest.post(loginUrl)
                    .body(json.toString())
                    .execute().body();
            System.out.println(login);
            com.alibaba.fastjson.JSONObject loginInfo = com.alibaba.fastjson.JSONObject.parseObject(login);
            String code = loginInfo.getString("code");
            if (code.equals("203")) {
                while (true) {
                    System.out.println("请输入二步验证代码：");
                    String code_2fa = scanner.next();
                    JSONObject json_2fa = new JSONObject();
                    json_2fa.set("code", code_2fa);
                    String result = HttpRequest.post("https://wp.007irs.com/api/v3/user/2fa")
                            .body(json_2fa.toString())
                            .execute().body();
                    com.alibaba.fastjson.JSONObject loginInfo_2fa = com.alibaba.fastjson.JSONObject.parseObject(result);
                    String code_ = loginInfo_2fa.getString("code");
                    System.out.println(result);
                    if (code_.equals("0")) {
                        cloud_cookie = cookie;
                        break;
                    }
                }
                break;
            }
        }
    }
}
