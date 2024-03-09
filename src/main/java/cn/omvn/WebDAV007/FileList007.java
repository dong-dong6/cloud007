package cn.omvn.pdfdownloader.controller.WebDAV007;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.omvn.pdfdownloader.util.RedisUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpCookie;
import java.util.TreeSet;

/**
 * 2024-02-09
 * 栋dong
 */
@RestController()
@RequestMapping("/007")

public class FileList007 {

    @Autowired
    private RedisUtils redisUtils;


    @GetMapping("/directory")
    private TreeSet<Object_007> getFileList() {
        TreeSet<Object_007> fileObject1 = getFileObject();
        return fileObject1;
    }

    public static TreeSet<Object_007> getFileObject() {
        TreeSet<Object_007> fileObject1 = getFileObject("https://wp.007irs.com/api/v3/directory/学习资源");
        return fileObject1;
    }

    public static TreeSet<Object_007> getFileObject(String url) {
        System.out.println("本次解析的目录为" + url);
        HttpCookie httpCookie = login.getCloud_cookie();
        String result = HttpRequest.get(URLUtil.encode(url))
                .cookie(httpCookie)
                .timeout(2000)
                .execute().body();//进行http请求

        JSONObject resultJSON = JSONObject.parseObject(result);
        JSONArray objectsArray = resultJSON.getJSONObject("data").getJSONArray("objects");
        //将JSON中的data取出
        TreeSet<Object_007> thisFile = new TreeSet<>();

        for (int i = 0; i < objectsArray.size(); i++) {
            JSONObject obj = objectsArray.getJSONObject(i);
            Object_007 object = new Object_007();
            object.setId(obj.getString("id"));
            object.setName(obj.getString("name"));
            object.setPath(obj.getString("path"));
            object.setThumb(obj.getBoolean("thumb"));
            object.setSize(obj.getLong("size"));
            object.setType(obj.getString("type"));
            object.setDate(DateTime.of(obj.getString("date"), "yyyy-MM-dd'T'HH:mm:ssXXX"));
            object.setCreate_date(DateTime.of(obj.getString("create_date"), "yyyy-MM-dd'T'HH:mm:ssXXX"));
            object.setSource_enabled(obj.getBoolean("source_enabled"));

            thisFile.add(object);
        }//将JSON全转移至TreeSet
        //新建一个TreeSet用于返回
        TreeSet<Object_007> resultSet = new TreeSet<>();
        for (Object_007 object : thisFile) {
            //如果是目录，就接着获取里面的内容
            if (object.isSource_enabled()) {//文件
                if (object.getType().equals("file")) {
                    object.setImageUrl(DownloadUrl.getImageUrl(object.getId()));
                    object.setUrl(DownloadUrl.getObjectUrl(object.getId()));
                    resultSet.add(object);//添加 至结果
                }
            } else {//目录
                String NewUrl;
                if (url.endsWith("/")) {
                    NewUrl = url + object.getName();
                } else {
                    NewUrl = url + "/" + object.getName();
                }//获取新url
                object.setChildrenFile(getFileObject(NewUrl));//递归下一层目录
                resultSet.add(object);
            }
        }
        return resultSet;
    }
}
