package cn.omvn.pdfdownloader.controller.WebDAV007;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import cn.omvn.pdfdownloader.util.RedisUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.HttpCookie;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2024-02-15
 * 栋dong
 */

public class DownloadUrl {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private FileList007 fileList007;
    TreeSet<String> urls;


    //根据ID获取下载外链
    public static String getObjectUrl(String id) {
        JSONObject items = new JSONObject();
        JSONArray item = new JSONArray();
        item.add(id);
        items.put("items", item);
        System.out.println(items);
        HttpCookie httpCookie = login.getCloud_cookie();
        String result = HttpRequest.post("https://wp.007irs.com/api/v3/file/source")
                .body(items.toString())
                .cookie(httpCookie)
                .timeout(2000)
                .execute().body();

        JSONObject resultJSON = JSONObject.parseObject(result);
        System.out.println(result);
        JSONObject objects = resultJSON.getJSONArray("data").getJSONObject(0);
        return objects.getString("url");
    }

    //根据ID获取预览图
    public static String getImageUrl(String id) {
        HttpCookie httpCookie = login.getCloud_cookie();
        HttpResponse httpResponse = HttpRequest.get("https://wp.007irs.com/api/v3/file/thumb/" + id)
                .cookie(httpCookie)
                .timeout(2000)
                .execute();
        String result = httpResponse.body();
        //System.out.println(result);
        String href = null;
        if (result == null) {
            return href;
        }
        String regex = "<a\\s+[^>]*href\\s*=\\s*\"([^\"]*)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);
        //System.out.println(result);
        if (matcher.find()) {
            href = matcher.group(1);
        }
        if (href != null) {
            String url = href.replace("amp;", "");
            System.out.println("成功获取图片url" + url);
            return url;
        } else {
            return null;
        }
    }
}
