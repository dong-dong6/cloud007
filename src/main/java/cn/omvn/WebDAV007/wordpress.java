package cn.omvn.pdfdownloader.controller.WebDAV007;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.TreeSet;

/**
 * 2024-02-26
 * 栋dong
 */
@RestController()
@RequestMapping("/007")
public class wordpress {
    @Autowired
    private TemplateEngine templateEngine;
    @GetMapping("wp")
    public String wordpressString(){
        Context context = new Context();
        TreeSet<Object_007> fileObject = FileList007.getFileObject();
        context.setVariable("objects", fileObject);
        String wordpress = templateEngine.process("007file", context);

        return wordpress;

    }
}
