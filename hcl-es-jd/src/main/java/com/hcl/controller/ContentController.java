package com.hcl.controller;

import com.hcl.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @program: hcl-es-api
 * @description:
 * @author: 作者
 * @create: 2022-02-07 16:44
 */
@RestController
public class ContentController {
    @Autowired
    private ContentService contentService;
    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("keyword")String keyword) throws IOException {
        return contentService.parsecontent(keyword);
    }
    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String,Object>> search(@PathVariable("keyword") String keyword,
                                           @PathVariable("pageNo") int pageNo,
                                           @PathVariable("pageSize") int pageSize) throws IOException {
        return contentService.searchPageHighlight(keyword, pageNo, pageSize);
    }
}
