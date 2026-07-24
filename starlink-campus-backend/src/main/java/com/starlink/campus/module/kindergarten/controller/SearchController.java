package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.starlink.campus.module.kindergarten.service.SearchService;
import com.starlink.campus.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SaCheckLogin
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public R<List<Map<String, Object>>> globalSearch(@RequestParam("q") String q) {
        List<Map<String, Object>> results = searchService.globalSearch(q);
        return R.ok(results);
    }
}
