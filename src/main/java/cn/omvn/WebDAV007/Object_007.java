package cn.omvn.pdfdownloader.controller.WebDAV007;

import cn.hutool.core.date.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.TreeSet;

/**
 * 2024-02-09
 * 栋dong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Object_007 implements Comparable<Object_007>{
    private String id;
    private String name;
    private String path;
    private boolean thumb;
    private Long size;
    private String type;
    private DateTime date;
    private DateTime create_date;
    private boolean source_enabled;
    /**
     * 当source_enabled为false，type为dir时，为目录，就在这里链接目录内文件
     */
    private TreeSet<Object_007> childrenFile;
    /**
     * 图片链接
     */
    private String imageUrl;
    /**
     * 007下载链接
     */
    private String url;


    @Override
    public int compareTo(Object_007 other) {
        return this.name.compareTo(other.name);
    }
}
