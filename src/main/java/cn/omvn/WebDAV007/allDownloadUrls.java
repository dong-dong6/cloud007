package cn.omvn.pdfdownloader.controller.WebDAV007;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 2024-02-22
 * 栋dong
 */
@RestController()
@RequestMapping("/007")
public class allDownloadUrls {
    //获取所有外链的list集合
    @GetMapping("/url")
    public List<String> getUrlList() {
        // Assume jsonString is your JSON content
        String jsonString = JSONUtil.toJsonPrettyStr(FileList007.getFileObject()); // Replace this with your actual JSON string

        //System.out.println(jsonString);
        JSONArray jsonArray = JSON.parseArray(jsonString);
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            extractUrls(jsonObject, urls);
        }
        return urls;
    }

    private static void extractUrls(JSONObject jsonObject, List<String> urls) {
        if (jsonObject.get("type").equals("file") && jsonObject.containsKey("url")) {
            urls.add(jsonObject.getString("url"));
        }
        if (jsonObject.containsKey("childrenFile")) {
            JSONArray children = jsonObject.getJSONArray("childrenFile");
            if (children != null) {
                for (int i = 0; i < children.size(); i++) {
                    extractUrls(children.getJSONObject(i), urls);
                }
            }
        }
    }
}
