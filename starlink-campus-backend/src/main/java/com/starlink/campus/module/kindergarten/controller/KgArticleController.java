package com.starlink.campus.module.kindergarten.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgArticle;
import com.starlink.campus.module.kindergarten.service.KgArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 校园微官网 CMS 新闻文章 API
 * 遵循代码分析与优化报告 2.11 节规范
 */
@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/article")
@CrossOrigin
public class KgArticleController {

    @Autowired
    private KgArticleService articleService;

    @GetMapping("/list")
    public R<List<KgArticle>> list() {
        QueryWrapper<KgArticle> query = new QueryWrapper<>();
        query.orderByDesc("create_time");
        return R.ok(articleService.list(query));
    }

    @PostMapping("/add")
    public R<Boolean> add(@Valid @RequestBody KgArticle article) {
        article.setViews(0);
        article.setStatus("待审核");
        article.setSyncStatus("未同步");
        article.setCreateTime(LocalDateTime.now());
        return R.ok(articleService.save(article));
    }

    @PostMapping("/update")
    public R<Boolean> update(@Valid @RequestBody KgArticle article) {
        return R.ok(articleService.updateById(article));
    }

    @PostMapping("/delete/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        return R.ok(articleService.removeById(id));
    }

    @PostMapping("/approve/{id}")
    public R<Boolean> approve(@PathVariable Long id) {
        KgArticle article = articleService.getById(id);
        if (article != null && "待审核".equals(article.getStatus())) {
            article.setStatus("已发布");
            return R.ok(articleService.updateById(article));
        }
        return R.fail("文章不存在或状态不正确");
    }

    @PostMapping("/sync-wechat/{id}")
    public R<Boolean> syncWechat(@PathVariable Long id) {
        KgArticle article = articleService.getById(id);
        if (article != null && "已发布".equals(article.getStatus())) {
            // Mock Wechat Sync
            System.out.println("[Mock Wechat Sync] Successfully synced article to Wechat: " + article.getTitle());
            article.setSyncStatus("已同步");
            return R.ok(articleService.updateById(article));
        }
        return R.fail("文章不存在或尚未发布");
    }
}
